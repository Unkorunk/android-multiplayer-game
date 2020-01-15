package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public class Entity extends GameObject {
    public Pair targetCell = null;

    private int chooseTimer = 0;

    @Override
    public void update() {
        if (targetCell == null) {
            targetCell = getCell();
        }

        var nowCell = getCell();

        if (nowCell.equals(targetCell)) {
            Trap trapObj = null;
            if (chooseTimer == 0 && GlobalSettings.checkObjectOnCell(new Pair(nowCell.x + 1, nowCell.y))) {
                for (var gameObj : GlobalSettings.getObjectsOnCell(new Pair(nowCell.x + 1, nowCell.y))) {
                    if (gameObj instanceof Trap) {
                        trapObj = (Trap) gameObj;
                        break;
                    }
                }
                if (trapObj != null) {
                    chooseTimer = 60;
                }
                // TODO: show buttons with choose
            }

            BehaviourModel.Command cmd = bm.update();

            if (chooseTimer == 0 || cmd != BehaviourModel.Command.RUN) {
                // TODO: hide buttons with choose
                chooseTimer = 0;
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
                chooseTimer--;
            }
        }

        Vector2 curSpeed = new Vector2(targetCell.x - nowCell.x, targetCell.y - nowCell.y);

        position.x += curSpeed.x;
        position.y += curSpeed.y;
    }

    public void setBehaviour(BehaviourModel model){
        bm = model;
    }

    private BehaviourModel bm;
}
