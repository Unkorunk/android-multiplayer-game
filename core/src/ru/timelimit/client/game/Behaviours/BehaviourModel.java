package ru.timelimit.client.game.Behaviours;

import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.Pair;

public interface BehaviourModel {
    enum Command {
        JUMP,
        SLIP,
        RUN
    }

    Command update();

    void sendPos(float x, float y, int xt, int yt);

    boolean receivePos(Vector2 pos);
}
