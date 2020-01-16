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
            if (btnSettings == null || btnSettings.getOrDefault(it.getKey(), true)) {
                it.getValue().render(batch);
            }
        }
    }

    public void hideElement(String nameElement) {
        btnSettings.put(nameElement, false);
    }
    public void showElement(String nameElement) {
        btnSettings.put(nameElement, true);
    }

    protected static HashMap<String, Boolean> btnSettings;
    protected static HashMap<String, Button> btnMap;
}
