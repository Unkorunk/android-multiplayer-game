package ru.timelimit.client.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.GameClient;

public class Button {
    public Button(int x, int y, int width, int height, Runnable callback) {
        btnRect = new Rectangle(x, y, width, height);
        renderRect = new Rectangle((int)(GameClient.instance.sceneManager.currentScene.getCamera().position.x + x),
                (int)(GameClient.instance.sceneManager.currentScene.getCamera().position.y + y),
                width, height);

        cb = callback;
    }

    public Rectangle getBounds() {
        return btnRect;
    }

    public void setRenderBounds(int x, int y) {
        renderRect = new Rectangle(x, y, btnRect.width, btnRect.height);
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

    public void render(Batch batch) {
        if (sprt != null){
            renderRect.x = (int)(GameClient.instance.sceneManager.currentScene.getCamera().position.x
                    - GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth / 2 + btnRect.x);
            renderRect.y = (int)(GameClient.instance.sceneManager.currentScene.getCamera().position.y
                    - GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight / 2 + btnRect.y);
            sprt.setBounds(renderRect.x, renderRect.y, renderRect.width, renderRect.height);
            sprt.draw(batch);
        }
    }

    public void setSprite(Sprite sprite) {
        sprt = sprite;
    }

    private Rectangle btnRect;
    private Rectangle renderRect;
    private Runnable cb;
    private Sprite sprt;
}
