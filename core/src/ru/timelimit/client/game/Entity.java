package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.Behaviours.BehaviourModel;

import java.util.HashMap;

public final class Entity extends GameObject {
    public Pair targetCell = null;

    private int hp = GlobalSettings.defaultHP;

    private long lastTime = 0;
    private long chooseTimer = 0;
    private long delaySeconds = 1;

    private int speed = 4;

    private enum TaskState {
        UNLOCK,
        UNLOCKED,
        LOCKED,
        LOCK
    };

    private TaskState stateNow = TaskState.UNLOCKED;
    private Trap trapObj = null;
    private HashMap<ru.timelimit.client.game.Behaviours.BehaviourModel.Command, String> commandToString = new HashMap<>();

    private boolean initCalled = false;
    private void init() {
        commandToString.put(ru.timelimit.client.game.Behaviours.BehaviourModel.Command.JUMP, "JumpBtn");
        commandToString.put(ru.timelimit.client.game.Behaviours.BehaviourModel.Command.SLIP, "SlipBtn");

        GameClient.ActionClient actionClient = new GameClient.ActionClient();
        actionClient.accessToken = "Some USER";
        actionClient.actionType = GameClient.ActionClientEnum.CONNECT;

        if (GameClient.client.isConnected()) {
            GameClient.client.sendTCP(actionClient);
            System.out.println("SEND");
        }
    }

    @Override
    public void update() {
        if (!initCalled) {
            init();
            initCalled = true;
        }

        if (targetCell == null) {
            targetCell = getCell();
        }

        var nowCell = getCell();
        if (stateNow == TaskState.UNLOCKED) {
            if (nowCell.equals(targetCell)) {
                position = Pair.pairToVector(targetCell); // TODO: Create normal fix
                if (nowCell.y > 1 && !GlobalSettings.checkObjectOnCell(new Pair(nowCell.x, nowCell.y - 1))) {
                    targetCell = new Pair(nowCell.x, nowCell.y - 1);
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
//            for (var cmd : trapObj.commands) {
//                if (commandToString.containsKey(cmd)) {
//                    GameClient.instance.sceneManager.currentScene.getUI().showElement(commandToString.get(cmd));
//                }
//            }
            for (var cmd : commandToString.values()) {
                GameClient.instance.sceneManager.currentScene.getUI().showElement(cmd);
            }
            chooseTimer = delaySeconds * 1000;
            lastTime = System.currentTimeMillis();
            stateNow = TaskState.LOCKED;
        }

        if (stateNow == TaskState.LOCKED) {
            ru.timelimit.client.game.Behaviours.BehaviourModel.Command cmd = bm.update();

            if (chooseTimer == 0 || cmd != ru.timelimit.client.game.Behaviours.BehaviourModel.Command.RUN) {
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

        if (stateNow == TaskState.UNLOCKED) {
            Vector2 curSpeed = new Vector2(targetCell.x - nowCell.x, targetCell.y - nowCell.y);
            curSpeed = curSpeed.nor();

            position.x += curSpeed.x * speed;
            position.y += curSpeed.y * speed;
        }
    }

    public void setBehaviour(ru.timelimit.client.game.Behaviours.BehaviourModel model){
        bm = model;
    }

    private BehaviourModel bm;
}
