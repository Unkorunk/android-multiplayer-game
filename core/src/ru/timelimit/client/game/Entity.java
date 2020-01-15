package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public class Entity extends GameObject {

    @Override
    public void update() {
        bm.update();
    }

    public void setBehaviour(BehaviourModel model){
        bm = model;
    }

    private BehaviourModel bm;
}
