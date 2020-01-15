package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.timelimit.client.game.GameClient;

import java.util.HashMap;

public abstract class UI {
    public abstract void init();

    public boolean isClicked(String elementName) {
        if (!btnMap.containsKey(elementName)) {
            return false;
        }

        return btnMap.get(elementName).checkClick(GameClient.lastClick);
    }

    public String findClicked() {
        for (HashMap.Entry<String, Button> it : btnMap.entrySet()) {
            if (it.getValue().checkClick(GameClient.lastClick)) {
                return it.getKey();
            }
        }
        return null;
    }

    public void render(Batch batch) {
        for (HashMap.Entry<String, Button> it : btnMap.entrySet()) {
            it.getValue().render(batch);
        }
    }

    protected static HashMap<String, Button> btnMap;
}
