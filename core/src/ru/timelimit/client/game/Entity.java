package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public class Entity extends GameObject {
    public Pair targetCell = null;

    private long lastTime = 0;
    private long chooseTimer = 0;
    private long delaySeconds = 10;

    private enum TaskState {
        UNLOCK,
        UNLOCKED,
        LOCKED,
        LOCK
    };

    private TaskState stateNow = TaskState.UNLOCKED;

    @Override
    public void update() {
        if (targetCell == null) {
            targetCell = getCell();
        }

        var nowCell = getCell();
        Trap trapObj = null;

        if (stateNow == TaskState.UNLOCKED) {
            if (nowCell.equals(targetCell)) {
                if (chooseTimer == 0 && GlobalSettings.checkObjectOnCell(new Pair(nowCell.x + 1, nowCell.y))) {
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
            // TODO: show choose buttons
            chooseTimer = delaySeconds * 1000;
            lastTime = System.currentTimeMillis();
            stateNow = TaskState.LOCKED;
        }

        if (stateNow == TaskState.LOCKED) {
            BehaviourModel.Command cmd = bm.update();

            if (chooseTimer == 0 || cmd != BehaviourModel.Command.RUN) {
                chooseTimer = 0;
                stateNow = TaskState.UNLOCK;
                switch (cmd) {
                    case JUMP:
                        targetCell = new Pair(nowCell.x + 1, nowCell.y + 1);
                        break;

                    case SLIP:
                        targetCell = new Pair(nowCell.x + 2, nowCell.y);
                        break;

                    default:
                        targetCell = new Pair(nowCell.x + 1, nowCell.y); // TODO: Lose or minus health
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
            // TODO: hide choose buttons
            stateNow = TaskState.UNLOCKED;
        } else if (nowCell.equals(targetCell)) {
            targetCell = new Pair(nowCell.x + 1, nowCell.y);
        }

        if (stateNow == TaskState.UNLOCKED) {
            Vector2 curSpeed = new Vector2(targetCell.x - nowCell.x, targetCell.y - nowCell.y);

            position.x += curSpeed.x;
            position.y += curSpeed.y;
        }
    }

    public void setBehaviour(BehaviourModel model){
        bm = model;
    }

    private BehaviourModel bm;
}
