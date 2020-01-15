package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public class Entity extends GameObject {

    private Vector2 speed = new Vector2(1, 0);

    private int jumpTimer = 0;

    @Override
    public void update() {
        BehaviourModel.Command cmd = bm.update();

        var curSpeed = new Vector2(speed.x, speed.y);

        if (jumpTimer == 0){
            // TODO: Normal command handling
            switch(cmd) {
                case JUMP:
                    position.y += 25;
                    jumpTimer = 60;
                    break;

                case SLIP:
                    // TODO: ???
                    break;

                default:
                    break;
            }
        } else {
            jumpTimer--;
            if (jumpTimer == 0) {
                position.y -= 25;
            }
        }


        position.x += curSpeed.x;
        position.y += curSpeed.y;
    }

    public void setBehaviour(BehaviourModel model){
        bm = model;
    }

    private BehaviourModel bm;
}
