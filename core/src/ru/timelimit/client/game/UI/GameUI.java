package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;

import java.util.HashMap;

public final class GameUI extends UI {

    @Override
    public void init() {
        var width = GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth;
        var height = GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight;

        btnMap = new HashMap<>();
        btnSettings = new HashMap<>();

        errorLabel = new Label(100, height - 20, 0, 0, "");
        errorLabel.background = null;
        btnMap.put("errorLabel", errorLabel);

        var jumpBtn = new Button(30, 10, 40, 40, () -> {});
        var slipBtn = new Button(width - 70, 10, 40, 40, () -> {});
        var menuBtn = new Button(width - 40, height - 50, 40, 40, () -> {
            GameClient.sendDisconnect();
            GameClient.instance.sceneManager.currentScene.setState(1);
        });


        jumpBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnUp")));
        slipBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnDown")));
        menuBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnExit")));

        btnMap.put("JumpBtn", jumpBtn);
        btnSettings.put("JumpBtn", false);
        btnMap.put("SlipBtn", slipBtn);
        btnSettings.put("SlipBtn", false);
        btnMap.put("MenuBtn", menuBtn);
    }
}
