package ru.timelimit.client.game.Behaviours;

import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.Pair;

public final class PlayerBehaviour implements BehaviourModel {
    @Override
    public Command update() {
        if (GameClient.instance.sceneManager.currentScene.getUI().isClicked("JumpBtn")) {
            return Command.JUMP;
        } else if (GameClient.instance.sceneManager.currentScene.getUI().isClicked("SlipBtn")) {
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
    public boolean receivePos(Vector2 target) { return false; }

}
