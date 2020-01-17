package ru.timelimit.client.game.Behaviours;

import ru.timelimit.client.game.GameClient;

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
}
