package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.GlobalSettings;
import ru.timelimit.client.game.ResourceManager;
import ru.timelimit.client.game.UI.MenuUI;
import ru.timelimit.client.game.UI.UI;

import java.util.Objects;

public class MenuScene implements Scene {
    public int exitCode = 0;
    private OrthographicCamera camera;
    private static UI gui = new MenuUI();

    private Sprite background;
    private Sprite backgroundCity1;
    private Sprite backgroundCity2;

    private float flowSpeed = 0.5f;

    @Override
    public void instantiate() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(600, 600 * (height / width));
        camera.position.set(0 + camera.viewportWidth / 2, 0 + camera.viewportHeight / 2, 0);
        camera.update();
        gui.init();

        background = new Sprite(Objects.requireNonNull(ResourceManager.getTexture("BackgroundSky")));

        background.setSize(camera.viewportWidth * 1.5f, camera.viewportHeight);
        background.setPosition(camera.viewportWidth * (1.0f - 1.5f) / 2, 0);

        backgroundCity1 = new Sprite(ResourceManager.getTexture("BackgroundCity"));
        backgroundCity1.setSize(camera.viewportWidth, camera.viewportHeight + 64);
        backgroundCity1.setPosition(0, -64);

        backgroundCity2 = new Sprite(ResourceManager.getTexture("BackgroundCity"));
        backgroundCity2.setSize(camera.viewportWidth, camera.viewportHeight + 64);
        backgroundCity2.setPosition(camera.viewportWidth, -64);
    }

    @Override
    public void dispose() {
        GlobalSettings.clearObjects();
        ResourceManager.disposeTempFonts();
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
        if (Gdx.input.justTouched()) {
            gui.findClicked();
        }

        backgroundCity1.translate(flowSpeed, 0);
        backgroundCity2.translate(flowSpeed, 0);

        if (backgroundCity1.getBoundingRectangle().x > camera.viewportWidth) {
            backgroundCity1.setX(-camera.viewportWidth + 1);
        } else if (backgroundCity2.getBoundingRectangle().x > camera.viewportWidth) {
            backgroundCity2.setX(-camera.viewportWidth + 1);
        }

        background.draw(batch);
        backgroundCity1.draw(batch);
        backgroundCity2.draw(batch);
        gui.render(batch);
    }

    @Override
    public int isOver() {
        return exitCode;
    }

    @Override
    public void setState(int state) {
        exitCode = state;
    }
}
