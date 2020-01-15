package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    public Vector2 position;
    public Sprite sprite;

    public abstract void update();

    public void render(Batch spriteBatch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(spriteBatch);
    }
}
