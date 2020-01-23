package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.GlobalSettings;
import ru.timelimit.client.game.UI.UI;

import java.util.ArrayList;

public interface Scene {
    default void instantiate() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        setCamera(new OrthographicCamera(600, 600 * (height / width)));
        getCamera().position.set(0 + getCamera().viewportWidth / 2, 0 + getCamera().viewportHeight / 2, 0);
        getCamera().update();
        getUI().setCamera(getCamera());
        getUI().init();
    }

    default void dispose() {
        GlobalSettings.clearObjects();
    }

    UI getUI();

    OrthographicCamera getCamera();

    void setCamera(OrthographicCamera camera);

    void translateCamera(float delta);

    ArrayList<Sprite> getFirstPlane();
    ArrayList<Sprite> getSecondPlane();
    ArrayList<Sprite> getThirdPlane();

    void render(SpriteBatch batch);

    int isOver();

    void setState(int state);
}
