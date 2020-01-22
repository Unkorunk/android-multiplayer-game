package ru.timelimit.client.game.Behaviours;

import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.Pair;
import ru.timelimit.client.game.UI.UI;

public final class PlayerBehaviour implements BehaviourModel {
    @Override
    public Command update() {
        if (UI.isClicked("JumpBtn")) {
            return Command.JUMP;
        } else if (UI.isClicked("SlipBtn")) {
            return Command.SLIP;
        } else {
            return Command.RUN;
        }
    }

    @Override
    public void sendPos(float x, float y, int xt, int yt) {
        GameClient.sendTarget(x, y, xt, yt);
    }

    @Override
    public Vector2 receivePos() { return null; }

}
