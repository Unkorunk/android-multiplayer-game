package ru.timelimit.client.game.Behaviours;

public interface BehaviourModel {
    enum Command {
        JUMP,
        SLIP,
        RUN
    }

    Command update();

}
