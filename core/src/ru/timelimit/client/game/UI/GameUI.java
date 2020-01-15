package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.TextureManager;

import java.util.HashMap;

public final class GameUI extends UI {

    @Override
    public void init() {
        btnMap = new HashMap<>();

        //var t = TextureManager.addTexture("JumpBtn", "badlogic.jpg");
        var jumpBtn = new Button(50, 10, 10, 10, () -> {});
        var slipBtn = new Button(90, 10, 10, 10, () -> {});
        var menuBtn = new Button(130, 10, 10, 10, () -> {});

        jumpBtn.setSprite(new Sprite(TextureManager.get("test")));
        slipBtn.setSprite(new Sprite(TextureManager.get("test")));
        menuBtn.setSprite(new Sprite(TextureManager.get("test")));

        btnMap.put("JumpBtn", jumpBtn);
        btnMap.put("SlipBtn", slipBtn);
        btnMap.put("MenuBtn", menuBtn);
    }
}
