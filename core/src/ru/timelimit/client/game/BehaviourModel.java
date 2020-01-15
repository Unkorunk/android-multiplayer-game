package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public abstract class BehaviourModel {
    public static enum Command {
        JUMP,
        SLIP,
        RUN
    }

    public abstract Command update();

}
