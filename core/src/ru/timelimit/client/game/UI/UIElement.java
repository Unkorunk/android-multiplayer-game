package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.css.Rect;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.GlobalSettings;

import java.util.ArrayList;

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

        for (var child : children) {
            child.render(batch);
        }
    }

    public void setBackground(Sprite sprite) { background = sprite; }

    public void setSprite(Sprite sprite) {
        sprt = sprite;
    }

    public void setBounds(Rectangle rect) {
        btnRect = rect;
        renderRect = rect;
    }

    public Rectangle getBounds(){
        return btnRect;
    }

    public Sprite getSprite() { return sprt; }

    public void addChildren(UIElement child) {
        children.add(child);
    }

    public UIElement getChildren(Class<?> c) {
        for (var child : children) {
            if (GlobalSettings.checkForType(child, c)){
                return child;
            }
        }
        return null;
    }

    protected Rectangle btnRect;
    protected Rectangle renderRect;
    protected Sprite sprt;
    protected Sprite background;

    protected ArrayList<UIElement> children = new ArrayList<>();
}
