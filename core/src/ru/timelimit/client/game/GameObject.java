package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    public Vector2 position = new Vector2();
    public Sprite sprite;

    public boolean isEnabled = true;

    public abstract void update();

    public void render(Batch spriteBatch) {
        if (!isEnabled)
            return;

        sprite.setPosition(position.x - sprite.getWidth() / 2.0f, position.y - sprite.getHeight() / 2.0f);
        sprite.draw(spriteBatch);

        // TODO: comment that
        var nowCell = getCell();
        Pixmap pixmap = new Pixmap((int)GlobalSettings.WIDTH_CELL, (int)GlobalSettings.HEIGHT_CELL, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.drawRectangle(0, 0, (int)GlobalSettings.WIDTH_CELL, (int)GlobalSettings.HEIGHT_CELL);
        var texture = new Texture(pixmap);
        spriteBatch.draw(texture, GlobalSettings.WIDTH_CELL * nowCell.x, GlobalSettings.HEIGHT_CELL * nowCell.y);
    }

    public void setCell(Pair pair) {
        position = new Vector2(
                pair.x * GlobalSettings.WIDTH_CELL + sprite.getWidth() / 2.0f,
                pair.y * GlobalSettings.HEIGHT_CELL + sprite.getHeight() / 2.0f
        );
    }

    public Pair getCell() {
        return new Pair(
                (int) Math.floor(position.x / GlobalSettings.WIDTH_CELL),
                (int) Math.floor(position.y / GlobalSettings.HEIGHT_CELL)
        );
    }

    public int getX() {
        return (int) Math.floor(position.x / GlobalSettings.WIDTH_CELL);
    }

    public int getY() {
        return (int) Math.floor(position.y / GlobalSettings.HEIGHT_CELL);
    }
}
