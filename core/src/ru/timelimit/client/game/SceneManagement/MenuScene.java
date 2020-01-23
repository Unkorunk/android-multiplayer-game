package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.GlobalSettings;
import ru.timelimit.client.game.ResourceManager;
import ru.timelimit.client.game.UI.MenuUI;
import ru.timelimit.client.game.UI.UI;

import java.util.ArrayList;
import java.util.Objects;


public class MenuScene implements Scene {
    private int exitCode = 0;
    private OrthographicCamera camera;
    private UI gui = new MenuUI();

    private ArrayList<Sprite> skyBackground;
    private ArrayList<Sprite> citybackground;
    private ArrayList<Sprite> groundBackground;

    private float flowSpeed = 0.5f;

    @Override
    public void instantiate() {
        Scene.super.instantiate();

        skyBackground = new ArrayList<>();
        citybackground = new ArrayList<>();
        groundBackground = new ArrayList<>();

        skyBackground.add(new Sprite(Objects.requireNonNull(ResourceManager.getTexture("BackgroundSky"))));

        skyBackground.get(0).setSize(camera.viewportWidth * 1.5f, camera.viewportHeight);
        skyBackground.get(0).setPosition(camera.viewportWidth * (1.0f - 1.5f) / 2, 0);

        var backgroundCity1 = new Sprite(ResourceManager.getTexture("BackgroundCity"));
        backgroundCity1.setSize(camera.viewportWidth, camera.viewportHeight + 64);
        backgroundCity1.setPosition(0, -64);

        var backgroundCity2 = new Sprite(ResourceManager.getTexture("BackgroundCity"));
        backgroundCity2.setSize(camera.viewportWidth, camera.viewportHeight + 64);
        backgroundCity2.setPosition(camera.viewportWidth, -64);

        citybackground.add(backgroundCity1);
        citybackground.add(backgroundCity2);
    }

    @Override
    public void dispose() {
        Scene.super.dispose();
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
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void translateCamera(float delta) {
        citybackground.get(0).translate(-delta, 0);
        citybackground.get(1).translate(-delta, 0);

        if (citybackground.get(0).getBoundingRectangle().x < -camera.viewportWidth) {
            citybackground.get(0).setX(camera.viewportWidth - 1);
        } else if (citybackground.get(1).getBoundingRectangle().x < -camera.viewportWidth) {
            citybackground.get(1).setX(camera.viewportWidth - 1);
        }
    }

    @Override
    public ArrayList<Sprite> getFirstPlane() {
        return groundBackground;
    }

    @Override
    public ArrayList<Sprite> getSecondPlane() {
        return citybackground;
    }

    @Override
    public ArrayList<Sprite> getThirdPlane() {
        return skyBackground;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (Gdx.input.justTouched()) {
            gui.findClicked();
        }

        translateCamera(flowSpeed);

        for (var sprite : skyBackground) {
            sprite.draw(batch);
        }

        for (var sprite : citybackground) {
            sprite.draw(batch);
        }

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
