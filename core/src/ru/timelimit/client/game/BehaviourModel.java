package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public interface BehaviourModel {
    enum Command {
        JUMP,
        SLIP,
        RUN
    }

    Command update();

}
