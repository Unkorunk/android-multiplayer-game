package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public abstract class UI {
    public static Vector2 lastClick;

    public abstract void init();

    public boolean IsClicked(String elementName) throws Exception {
        if (!btnMap.containsKey(elementName)) {
            return false;
        }

        return btnMap.get(elementName).checkClick(lastClick);
    }

    protected static HashMap<String, Button> btnMap;
}
