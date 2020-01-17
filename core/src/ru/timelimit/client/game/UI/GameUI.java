package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;

import java.util.HashMap;

public final class GameUI extends UI {

    @Override
    public void init() {
        btnMap = new HashMap<>();
        btnSettings = new HashMap<>();

        var jumpBtn = new Button(30, 10, 40, 40, () -> {});
        var slipBtn = new Button(GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth - 70, 10, 40, 40, () -> {});
        var menuBtn = new Button(10, GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight - 50, 40, 40, () -> {});


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
