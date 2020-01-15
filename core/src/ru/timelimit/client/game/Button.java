package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Function;

public class Button {
    public Button(int x, int y, int width, int height, Runnable callback, Sprite sprite) {
        btnRect = new Rectangle(x, y, width, height);
        cb = callback;
        sprt = sprite;
    }

    public boolean checkClick(Vector2 touch) {
        if (touch.x >= btnRect.x && touch.x <= btnRect.x + btnRect.width
                && touch.y >= btnRect.y && touch.y <= btnRect.y + btnRect.height) {

            cb.run();
            return true;
        }

        return false;
    }

    public void render(Batch batch) {
        sprt.draw(batch);
    }

    private Rectangle btnRect;
    private Runnable cb;
    private Sprite sprt;
}
