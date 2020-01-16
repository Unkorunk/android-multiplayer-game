package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class Trap extends GameObject {
    public ArrayList<BehaviourModel.Command> commands;

    private Trap(ArrayList<BehaviourModel.Command> commands, Sprite sprite) {
        this.commands = commands;
        this.sprite = sprite;
    }

    public static final Trap laserTrap = new Trap(
            new ArrayList<>(Arrays.asList(BehaviourModel.Command.JUMP)),
            new Sprite(TextureManager.get("Laser"))
    );

    public int dmg = 25;

    @Override
    public void update() {
    }

    public Trap clone() {
        Trap newTrap = new Trap(this.commands, this.sprite);
        newTrap.setCell(this.getCell());

        return newTrap;
    }
}