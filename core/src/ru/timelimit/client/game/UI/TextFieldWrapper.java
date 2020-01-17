package ru.timelimit.client.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import ru.timelimit.client.game.GameClient;

public final class TextFieldWrapper extends UIElement {
    TextFieldWrapper(TextField textField) {
        origin = textField;
    }

    public TextField origin;

    @Override
    public void render(Batch batch) {
        origin.draw(batch, 1);
    }

    public boolean checkClick(Vector2 touch) {
        if (touch == null) {
            return false;
        }

        if (Gdx.input.justTouched()){
            if (touch.x >= origin.getX() && touch.x <= origin.getX() + origin.getWidth()
                    && touch.y >= origin.getY() && touch.y <= origin.getY() + origin.getHeight()) {
                System.out.println("Clicked!");
                Gdx.input.setOnscreenKeyboardVisible(true);

                ((MenuUI) GameClient.instance.sceneManager.currentScene.getUI()).activeField = this;

                return true;
            }
        }

        Gdx.input.setOnscreenKeyboardVisible(false);

        return false;
    }
}
