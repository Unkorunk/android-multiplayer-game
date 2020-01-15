package ru.timelimit.client.game;

public class PlayerBehaviour implements BehaviourModel {
    @Override
    public Command update() {
        if (GameClient.gui.isClicked("JumpBtn")) {
            return Command.JUMP;
        } else if (GameClient.gui.isClicked("SlipBtn")) {
            return Command.SLIP;
        } else {
            return Command.RUN;
        }
    }
}
