package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;
import ru.timelimit.network.ActionClient;
import ru.timelimit.network.ActionClientEnum;
import ru.timelimit.network.ConnectRequest;
import ru.timelimit.client.game.Behaviours.BehaviourModel;

import java.util.HashMap;

public final class Entity extends GameObject {
    public Pair targetCell = null;

    private int hp = GlobalSettings.defaultHP;

    private long lastTime = 0;
    private long chooseTimer = 0;
    private long delaySeconds = 1;

    private int speed = 4;

    private boolean fallDown = false;

    private enum TaskState {
        UNLOCK,
        UNLOCKED,
        LOCKED,
        LOCK
    }

    private TaskState stateNow = TaskState.UNLOCKED;
    private Trap trapObj = null;
    private HashMap<BehaviourModel.Command, String> commandToString = new HashMap<>();

    private boolean initCalled = false;
    private void init() {
        commandToString.put(BehaviourModel.Command.JUMP, "JumpBtn");
        commandToString.put(BehaviourModel.Command.SLIP, "SlipBtn");
    }

    @Override
    public void update() {
        if (!initCalled) {
            init();
            initCalled = true;
        }

        var received = bm.receivePos();

        if (received != null) {
            System.out.println("Position received: " + position.x + " " + position.y);
            position = new Vector2(received.x, received.y);
            return;
        }

        if (targetCell == null) {
            targetCell = getCell();
        }

        var nowCell = getCell();

        if (Math.abs(position.y - Pair.pairToVector(getCell()).y) <= GlobalSettings.gravitySpeed / 2f) {
            fallDown = false;
        }

        if (!fallDown) {
            if (stateNow == TaskState.UNLOCKED) {
                if (nowCell.equals(targetCell)) {
                    if (nowCell.y > 1 && !GlobalSettings.checkObjectOnCell(new Pair(nowCell.x, nowCell.y - 1))) {
                        targetCell = new Pair(nowCell.x, nowCell.y - 1);
                        fallDown = true;
                    } else if (chooseTimer == 0 && GlobalSettings.checkObjectOnCell(new Pair(nowCell.x + 1, nowCell.y))) {
                        for (var gameObj : GlobalSettings.getObjectsOnCell(new Pair(nowCell.x + 1, nowCell.y))) {
                            if (gameObj instanceof Trap) {
                                trapObj = (Trap) gameObj;
                                stateNow = TaskState.LOCK;
                                break;
                            }
                        }
                    }
                }
            }

            if (stateNow == TaskState.LOCK) {

                for (var cmd : commandToString.values()) {
                    GameClient.instance.sceneManager.currentScene.getUI().showElement(cmd);
                }
                chooseTimer = delaySeconds * 1000;
                lastTime = System.currentTimeMillis();
                stateNow = TaskState.LOCKED;
            }

            if (stateNow == TaskState.LOCKED) {
                BehaviourModel.Command cmd = bm.update();

                if (chooseTimer == 0 || cmd != BehaviourModel.Command.RUN) {
                    chooseTimer = 0;
                    stateNow = TaskState.UNLOCK;

                    if (!trapObj.commands.contains(cmd)) {
                        cmd = trapObj.commands.get(0);
                        hp -= trapObj.dmg;
                        if (hp <= 0) {
                            isEnabled = false;
                        }
                    }

                    switch (cmd) {
                        case JUMP:
                            targetCell = new Pair(nowCell.x + 1, nowCell.y + 1);
                            break;
                        case SLIP:
                            targetCell = new Pair(nowCell.x + 2, nowCell.y);
                            break;
                        case RUN:
                            targetCell = new Pair(nowCell.x + 1, nowCell.y);
                            break;
                        default:
                            System.err.println("No handler for this command");
                            break;
                    }
                } else {
                    var nowTime = System.currentTimeMillis();

                    var deltaTime = nowTime - lastTime;
                    if (deltaTime >= chooseTimer) {
                        chooseTimer = 0;
                    } else {
                        chooseTimer -= deltaTime;
                    }

                    lastTime = nowTime;
                }
            }

            if (stateNow == TaskState.UNLOCK) {
//            for (var cmd : trapObj.commands) {
//                if (commandToString.containsKey(cmd)) {
//                    GameClient.instance.sceneManager.currentScene.getUI().hideElement(commandToString.get(cmd));
//                }
//            }
                for (var cmd : commandToString.values()) {
                    GameClient.instance.sceneManager.currentScene.getUI().hideElement(cmd);
                }
                stateNow = TaskState.UNLOCKED;
            } else if (nowCell.equals(targetCell)) {
                targetCell = new Pair(nowCell.x + 1, nowCell.y);
            }
        }


        if (stateNow == TaskState.UNLOCKED) {
            Vector2 curSpeed = new Vector2(targetCell.x - nowCell.x, targetCell.y - nowCell.y);

            if (curSpeed.y == 0 && Math.abs(position.y - Pair.pairToVector(getCell()).y) > GlobalSettings.gravitySpeed / 2f)  {
                curSpeed.y = Pair.pairToVector(getCell()).y - position.y;
            }

            curSpeed = curSpeed.nor();

            position.x += curSpeed.x * speed;
            position.y += curSpeed.y * GlobalSettings.gravitySpeed;
        }

        bm.sendPos(position.x, position.y, targetCell.x, targetCell.y);
    }

    public void setBehaviour(BehaviourModel model){
        bm = model;
    }

    public BehaviourModel getBehaviour() { return bm; }

    private BehaviourModel bm;
}
