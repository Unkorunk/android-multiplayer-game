package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.GlobalSettings;

import java.util.HashMap;

public abstract class UI {
    public abstract void init();

    public Label errorLabel;

    public boolean isClicked(String elementName) {
        if (!btnMap.containsKey(elementName)) {
            return false;
        }

        if (GlobalSettings.checkForType(btnMap.get(elementName), Button.class)) {
            return ((Button)btnMap.get(elementName)).checkClick(GameClient.lastClick);
        }
        if (GlobalSettings.checkForType(btnMap.get(elementName), TextFieldWrapper.class)) {
            return ((TextFieldWrapper)btnMap.get(elementName)).checkClick(GameClient.lastClick);
        }
        return false;
    }

    public String findClicked() {
        for (HashMap.Entry<String, UIElement> it : btnMap.entrySet()) {
            if (isClicked(it.getKey())) {
                return it.getKey();
            }
        }
        return null;
    }

    public void render(Batch batch) {
        for (HashMap.Entry<String, UIElement> it : btnMap.entrySet()) {
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
    protected static HashMap<String, UIElement> btnMap;
}
