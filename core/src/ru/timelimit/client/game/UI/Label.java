package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.TextureManager;

public final class Label extends UIElement {
    public Label(float x, float y, float width, float height, String text) {
        btnRect = new Rectangle(x, y, width, height);
        renderRect = new Rectangle(GameClient.instance.sceneManager.currentScene.getCamera().position.x + x,
                GameClient.instance.sceneManager.currentScene.getCamera().position.y + y,
                width, height);

        background = new Sprite(TextureManager.getTexture("BtnEmpty"));

        bFont = TextureManager.getFont("defaultFont");

        labelText = text;

        labelLayout = new GlyphLayout();
        labelLayout.setText(bFont, labelText);
        margin = 10;
    }

    public void setFont(BitmapFont font) {
        bFont = font;
        labelLayout.setText(bFont, labelText);
    }

    @Override
    public void render(Batch batch) {
        renderRect.x = GameClient.instance.sceneManager.currentScene.getCamera().position.x
                - GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth / 2 + btnRect.x;
        renderRect.y = GameClient.instance.sceneManager.currentScene.getCamera().position.y
                - GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight / 2 + btnRect.y;

        renderRect.x -= labelLayout.width / 2 + margin;
        renderRect.y -= labelLayout.height / 2 + margin;

        renderRect.width = labelLayout.width + margin * 2;
        renderRect.height = labelLayout.height + margin * 2;

        if (background != null) {
            background.setBounds(renderRect.x, renderRect.y, renderRect.width, renderRect.height);
            background.draw(batch);
        }
        if (sprt != null){
            sprt.setBounds(renderRect.x, renderRect.y, renderRect.width, renderRect.height);
            sprt.draw(batch);
        }

        if (bFont != null) {
            bFont.draw(batch, labelText, renderRect.x + margin, renderRect.y + labelLayout.height + margin);
        }

    }

    private String labelText;
    private BitmapFont bFont;
    private GlyphLayout labelLayout;
    private int margin;
}
