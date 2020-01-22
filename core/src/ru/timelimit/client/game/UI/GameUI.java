package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;

import java.util.HashMap;

public final class GameUI extends UI {
    private Label hpDisplay;

    public void updateHp(int hp) {
        hpDisplay.setText("HP: " + hp);
    }

    @Override
    public void init() {
        UI.currentUI = this;

        var width = cameraInst.viewportWidth;
        var height = cameraInst.viewportHeight;

        btnMap = new HashMap<>();
        btnSettings = new HashMap<>();

        errorLabel = new Label(100, height - 20, 0, 0, "");
        errorLabel.background = null;
        btnMap.put("errorLabel", errorLabel);

        var jumpBtn = new Button(30, 10, 40, 40, () -> {});
        var slipBtn = new Button(width - 70, 10, 40, 40, () -> {});
        var menuBtn = new Button(10, height - 50, 40, 40, () -> {
            GameClient.sendDisconnect();
        });


        jumpBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnUp")));
        slipBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnDown")));
        menuBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnExit")));

        hpDisplay = new Label(width - 70 - 20, height - 50 - 20, 0, 0, "HP: ");

        btnMap.put("JumpBtn", jumpBtn);
        btnSettings.put("JumpBtn", false);
        btnMap.put("SlipBtn", slipBtn);
        btnSettings.put("SlipBtn", false);
        btnMap.put("MenuBtn", menuBtn);
        btnMap.put("HpBar", hpDisplay);
    }
}
