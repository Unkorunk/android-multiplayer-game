package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.*;
import ru.timelimit.client.game.UI.PreparationUI;
import ru.timelimit.client.game.UI.UI;

import java.util.Timer;
import java.util.TimerTask;

public class PreparationScene implements Scene {
    public int exitCode = 0;
    private Timer preparationTimer;
    private OrthographicCamera camera;
    private static UI gui = new PreparationUI();
    private Sprite background;

    @Override
    public void instantiate() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(800, 800 * (height / width));
        camera.position.set(GameClient.WORLD_WIDTH / 2f, GameClient.WORLD_HEIGHT / 2f, 0);
        camera.update();
        gui.init();

        background = new Sprite(TextureManager.get("BackgroundSky"));
        background.setPosition(0, 0);
        background.setSize(GameClient.WORLD_WIDTH, GameClient.WORLD_HEIGHT);

        preparationTimer = new Timer();
        preparationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                exitCode = 3;
            }
        }, 10 * 1000);
    }

    @Override
    public void dispose() {

    }

    @Override
    public UI getUI() {
        return gui;
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }


    private void renderObjects(SpriteBatch batch) {
        for (var gameObj : GlobalSettings.gameObjects) {
            gameObj.render(batch);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        background.draw(batch);

        gui.findClicked();

        gui.render(batch);

        renderObjects(batch);
    }

    @Override
    public int isOver() {
        return exitCode;
    }
}
