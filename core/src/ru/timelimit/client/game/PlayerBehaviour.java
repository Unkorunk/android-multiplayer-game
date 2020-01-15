package ru.timelimit.client.game;

public class PlayerBehaviour extends BehaviourModel {
    @Override
    public Command update() throws Exception {
        if (GameClient.gui.IsClicked("JumpBtn")) {
            return Command.JUMP;
        } else if (GameClient.gui.IsClicked("SlipBtn")) {
            return Command.SLIP;
        } else {
            return Command.RUN;
        }
    }
}
