package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import ru.timelimit.client.game.GameClient;

public abstract class UIElement {
    public void render(Batch batch) {
        renderRect.x = GameClient.instance.sceneManager.currentScene.getCamera().position.x
                - GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth / 2 + btnRect.x;
        renderRect.y = GameClient.instance.sceneManager.currentScene.getCamera().position.y
                - GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight / 2 + btnRect.y;

        if (background != null) {
            background.setBounds(renderRect.x, renderRect.y, renderRect.width, renderRect.height);
            background.draw(batch);
        }
        if (sprt != null){
            sprt.setBounds(renderRect.x, renderRect.y, renderRect.width, renderRect.height);
            sprt.draw(batch);
        }
    }

    public void setBackground(Sprite sprite) { background = sprite; }

    public void setSprite(Sprite sprite) {
        sprt = sprite;
    }

    protected Rectangle btnRect;
    protected Rectangle renderRect;
    protected Sprite sprt;
    protected Sprite background;
}
