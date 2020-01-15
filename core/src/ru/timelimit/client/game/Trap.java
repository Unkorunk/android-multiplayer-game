package ru.timelimit.client.game;

import java.util.ArrayList;

public class Trap extends GameObject {
    public ArrayList<BehaviourModel.Command> commands = new ArrayList<>();

    @Override
    public void update() {}

    public Trap clone() {
        Trap newTrap = new Trap();
        newTrap.sprite = this.sprite;
        newTrap.commands = this.commands;
        newTrap.setCell(this.getCell());


        return newTrap;
    }
}
