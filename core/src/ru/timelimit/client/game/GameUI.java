package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;

public final class GameUI extends UI {

    @Override
    public void init() {
        btnMap = new HashMap<>();

        //var t = TextureManager.addTexture("JumpBtn", "badlogic.jpg");

        btnMap.put("JumpBtn",
                  new Button(10, 10, 10, 10, () -> {}, new Sprite(TextureManager.addTexture("JumpBtn", "badlogic.jpg")))
        );

        btnMap.put("SlipBtn",
                new Button(30, 10, 10, 10, () -> {}, new Sprite(TextureManager.addTexture("SlipBtn", "badlogic.jpg")))
        );

        btnMap.put("MenuBtn",
                new Button(50, 10, 10, 10, () -> {}, new Sprite(TextureManager.addTexture("MenuBtn", "badlogic.jpg")))
        );

    }
}
