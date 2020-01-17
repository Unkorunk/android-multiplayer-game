package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.TextureManager;

import java.util.HashMap;

public final class GameUI extends UI {

    @Override
    public void init() {
        btnMap = new HashMap<>();
        btnSettings = new HashMap<>();

        var jumpBtn = new Button(50, 10, 40, 40, () -> {});
        var slipBtn = new Button(90, 10, 40, 40, () -> {});
        var menuBtn = new Button(130, 10, 40, 40, () -> {});

        jumpBtn.setSprite(new Sprite(TextureManager.get("BtnUp")));
        slipBtn.setSprite(new Sprite(TextureManager.get("BtnDown")));
        menuBtn.setSprite(new Sprite(TextureManager.get("BtnMenu")));

        btnMap.put("JumpBtn", jumpBtn);
        btnSettings.put("JumpBtn", false);
        btnMap.put("SlipBtn", slipBtn);
        btnSettings.put("SlipBtn", false);
        btnMap.put("MenuBtn", menuBtn);
    }
}
