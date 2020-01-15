package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import javafx.util.Pair;

public abstract class GameObject {
    public Vector2 position;
    public Sprite sprite;

    public abstract void update();

    public void render(Batch spriteBatch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(spriteBatch);
    }

    public Pair<Integer, Integer> getCell() {
        return new Pair<>(
                (int) Math.ceil((sprite.getX() + sprite.getWidth() / 2.0f) / GlobalSettings.WIDTH_CELL),
                (int) Math.ceil((sprite.getY() + sprite.getHeight() / 2.0f) / GlobalSettings.HEIGHT_CELL)
        );
    }
}
