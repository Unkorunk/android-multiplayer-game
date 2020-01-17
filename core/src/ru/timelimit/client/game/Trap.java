package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.Behaviours.BehaviourModel;

import java.util.ArrayList;
import java.util.Arrays;

public class Trap extends GameObject implements Cloneable {
    public ArrayList<ru.timelimit.client.game.Behaviours.BehaviourModel.Command> commands;

    protected Trap(ArrayList<ru.timelimit.client.game.Behaviours.BehaviourModel.Command> commands, Sprite sprite) {
        this.commands = commands;
        this.objSprite = sprite;
    }

    public static final Trap laserTrap = new Trap(
            new ArrayList<>(Arrays.asList(ru.timelimit.client.game.Behaviours.BehaviourModel.Command.JUMP)),
            new Sprite(TextureManager.getTexture("Laser"))
    ) {
        @Override
        public boolean validator(Pair pos) {
            return pos.y == 1 && super.validator(pos);
        }
    };

    public static final Trap flyTrap = new Trap(
            new ArrayList<>(Arrays.asList(BehaviourModel.Command.SLIP)),
            new Sprite(TextureManager.getTexture("FlyTrap"))
    );

    public int dmg = 25;

    @Override
    public void update() {
    }

    public boolean validator(Pair pos) {
        boolean verdict = true;
        if (GlobalSettings.checkObjectOnCell(pos) ||
                GlobalSettings.checkObjectOnCell(new Pair(pos.x + 1, pos.y)) ||
                GlobalSettings.checkObjectOnCell(new Pair(pos.x - 1, pos.y)) ||
                GlobalSettings.checkObjectOnCell(new Pair(pos.x, pos.y + 1)) ||
                GlobalSettings.checkObjectOnCell(new Pair(pos.x, pos.y - 1))) {
            verdict = false;
        }

        return pos.y > 0 && verdict;
    }

    public Trap clone() {
        Trap newTrap = null;
        try {
            newTrap = (Trap) super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return newTrap;
    }
}