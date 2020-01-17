package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.GlobalSettings;
import ru.timelimit.client.game.TextureManager;
import ru.timelimit.client.game.UI.MenuUI;
import ru.timelimit.client.game.UI.UI;

import java.util.Objects;

public class MenuScene implements Scene {
    public int exitCode = 0;
    private OrthographicCamera camera;
    private static UI gui = new MenuUI();

    private Sprite background;

    @Override
    public void instantiate() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(600, 600 * (height / width));
        camera.position.set(0 + camera.viewportWidth / 2, 0 + camera.viewportHeight / 2, 0);
        camera.update();
        gui.init();

        background = new Sprite(Objects.requireNonNull(TextureManager.getTexture("BackgroundSky")));

        background.setSize(camera.viewportWidth * 1.5f, camera.viewportHeight);
        background.setPosition(camera.viewportWidth * (1.0f - 1.5f) / 2, 0);
    }

    @Override
    public void dispose() {
        GlobalSettings.gameObjects.clear();
    }

    @Override
    public UI getUI() {
        return gui;
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public Sprite getBackground() {
        return background;
    }

    @Override
    public void render(SpriteBatch batch) {
        background.draw(batch);
        gui.render(batch);
    }
    @Override
    public int isOver() {
        return 0;
    }
}
