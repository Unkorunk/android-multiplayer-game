package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public class Entity extends GameObject {

    private Vector2 speed = new Vector2(1, 0);

    @Override
    public void update() {
        BehaviourModel.Command cmd = bm.update();

        var curSpeed = new Vector2(speed.x, speed.y);

        // TODO: Normal command handling
        switch(cmd) {
            case JUMP:
                curSpeed.y += 1;
                break;

            case SLIP:
                curSpeed.y -= 1;
                break;

            default:
                break;
        }

        position.x += curSpeed.x;
        position.y += curSpeed.y;
    }

    public void setBehaviour(BehaviourModel model){
        bm = model;
    }

    private BehaviourModel bm;
}
