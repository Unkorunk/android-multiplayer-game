package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.GlobalSettings;

import java.util.HashMap;

public abstract class UI {
    protected OrthographicCamera cameraInst;
    protected static UI currentUI;

    public void init() {
        UI.currentUI = this;
        btnMap = new HashMap<>();
        btnSettings = new HashMap<>();
    }

    public Label errorLabel;

    public void setCamera(OrthographicCamera camera) {
        cameraInst = camera;
    }

    public static boolean isClicked(String elementName) {
        if (!currentUI.btnMap.containsKey(elementName)) {
            return false;
        }

        if (GlobalSettings.checkForType(currentUI.btnMap.get(elementName), Button.class)) {
            return ((Button)currentUI.btnMap.get(elementName)).checkClick(GameClient.lastClick);
        }
        if (GlobalSettings.checkForType(currentUI.btnMap.get(elementName), TextFieldWrapper.class)) {
            return ((TextFieldWrapper)currentUI.btnMap.get(elementName)).checkClick(GameClient.lastClick);
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

    public UIElement getElement(String name) {
        if (btnMap.containsKey(name)) {
            return btnMap.get(name);
        }
        return null;
    }

    public void render(Batch batch) {
        for (HashMap.Entry<String, UIElement> it : btnMap.entrySet()) {
            if (btnSettings == null || btnSettings.getOrDefault(it.getKey(), true)) {
                it.getValue().render(batch, cameraInst);
            }
        }
    }

    public static void hideElement(String nameElement) {
        currentUI.hideEl(nameElement);
    }
    public static void showElement(String nameElement) {
        currentUI.showEl(nameElement);
    }

    protected void hideEl(String nameElement) {
        btnSettings.put(nameElement, false);
    }
    protected void showEl(String nameElement) {
        btnSettings.put(nameElement, true);
    }

    protected void addElement(String name, UIElement element, boolean visibility) {
        btnMap.put(name, element);
        btnSettings.put(name, visibility);
    }

    protected static HashMap<String, Boolean> btnSettings;
    protected static HashMap<String, UIElement> btnMap;
}
