package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.UI.UI;

public interface Scene {
    void instantiate();

    void dispose();

    UI getUI();

    OrthographicCamera getCamera();

    Sprite getBackground();

    void render(SpriteBatch batch);

    int isOver();

    void setState(int state);
}
