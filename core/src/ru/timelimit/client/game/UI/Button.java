package ru.timelimit.client.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;

public final class Button extends UIElement {
    public Button(float x, float y, float width, float height, Runnable callback) {
        btnRect = new Rectangle(x, y, width, height);
        renderRect = new Rectangle(GameClient.instance.sceneManager.currentScene.getCamera().position.x + x,
                GameClient.instance.sceneManager.currentScene.getCamera().position.y + y,
                width, height);

        cb = callback;
        background = new Sprite(ResourceManager.getTexture("BtnEmpty"));
    }


    public boolean checkClick(Vector2 touch) {
        if (touch == null) {
            return false;
        }

        if (Gdx.input.justTouched()){
            if (touch.x >= renderRect.x && touch.x <= renderRect.x + renderRect.width
                    && touch.y >= renderRect.y && touch.y <= renderRect.y + renderRect.height) {

                cb.run();
                return true;
            }
        }

        return false;
    }


    private Runnable cb;
}
