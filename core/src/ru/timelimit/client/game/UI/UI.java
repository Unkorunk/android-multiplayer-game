package ru.timelimit.client.game.UI;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public abstract class UI {
    public static Vector2 lastClick;

    public abstract void init();

    public boolean isClicked(String elementName) {
        if (!btnMap.containsKey(elementName)) {
            return false;
        }

        return btnMap.get(elementName).checkClick(lastClick);
    }

    protected static HashMap<String, Button> btnMap;
}
