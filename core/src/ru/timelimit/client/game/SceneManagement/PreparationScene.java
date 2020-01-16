package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.*;
import ru.timelimit.client.game.UI.PreparationUI;
import ru.timelimit.client.game.UI.UI;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PreparationScene implements Scene {
    public int exitCode = 0;

    private Timer preparationTimer;
    private OrthographicCamera camera;
    private static UI gui = new PreparationUI();
    private Sprite background;

    private ArrayList<Trap> trapTypes;
    private ArrayList<Trap> trapList;

    private Trap currentTrap = null;

    private void trapInit() {
        trapTypes = new ArrayList<>();

        var laser = new Trap();
        laser.sprite = new Sprite(TextureManager.get("Laser"));
        laser.setCell(new Pair(0, 0));

        trapTypes.add(laser);
    }

    @Override
    public void instantiate() {
        trapInit();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(600, 600 * (height / width));
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
        }, GlobalSettings.preparationTime);

        ((PreparationUI)gui).initChooser(trapTypes, (Trap trap) -> {
            currentTrap = trap;
        });
    }

    @Override
    public void dispose() {
        if (exitCode != 3) {
            GlobalSettings.gameObjects.clear();
        }
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

        String clickedBtn = gui.findClicked();
        if (clickedBtn != null) {
            System.out.println(2);
        }
        if (clickedBtn == null && currentTrap != null && Gdx.input.justTouched()) {
            var newTrap = currentTrap.clone();
            newTrap.setCell(Pair.vectorToCell(GameClient.lastClick));
            GlobalSettings.gameObjects.add(newTrap);
        }

        gui.render(batch);

        renderObjects(batch);
    }

    @Override
    public int isOver() {
        return exitCode;
    }
}
