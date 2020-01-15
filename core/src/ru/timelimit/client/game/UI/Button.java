package ru.timelimit.client.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.function.Function;

public class Button {
    public Button(int x, int y, int width, int height, Runnable callback) {
        btnRect = new Rectangle(x, y, width, height);
        cb = callback;
    }

    public boolean checkClick(Vector2 touch) {
        if (touch == null) {
            return false;
        }

        if (Gdx.input.justTouched()){
            if (touch.x >= btnRect.x && touch.x <= btnRect.x + btnRect.width
                    && touch.y >= btnRect.y && touch.y <= btnRect.y + btnRect.height) {

                cb.run();
                return true;
            }
        }


        return false;
    }

    public void render(Batch batch) {
        if (sprt != null){
            sprt.setBounds(btnRect.x, btnRect.y, btnRect.width, btnRect.height);
            sprt.draw(batch);
        }

    }

    public void setSprite(Sprite sprite) {
        sprt = sprite;
    }

    private Rectangle btnRect;
    private Runnable cb;
    private Sprite sprt;
}
