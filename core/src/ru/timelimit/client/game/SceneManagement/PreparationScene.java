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
    private ArrayList<Sprite> ground;
    private ArrayList<Sprite> parallaxCity;

    private ArrayList<Trap> trapTypes;
    private ArrayList<Trap> trapList;

    private Trap currentTrap = null;

    private void trapInit() {
        trapTypes = new ArrayList<>();

        trapTypes.add(Trap.laserTrap.clone());
    }

    @Override
    public void instantiate() {
        trapInit();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(600, 600 * (height / width));
        camera.position.set(0 + camera.viewportWidth / 2, 0 + camera.viewportHeight / 2, 0);
        camera.update();
        gui.init();

        background = new Sprite(TextureManager.get("BackgroundSky"));
        background.setPosition(0, 0);
        background.setSize(camera.viewportWidth, camera.viewportHeight);

        int x = 0;
        ground = new ArrayList<>();
        parallaxCity = new ArrayList<>();
        while (x < GlobalSettings.WORLD_WIDTH) {
            var gSprite = new Sprite(TextureManager.get("BackgroundGround"));
            var cSprite = new Sprite(TextureManager.get("BackgroundCity"));
            gSprite.setPosition(x, 0);
            cSprite.setPosition(x, 0);
            ground.add(gSprite);
            parallaxCity.add(cSprite);
            x += gSprite.getWidth();
        }

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

        var finishObj = new Finish();
        finishObj.position = new Vector2(GlobalSettings.WORLD_WIDTH - 20, GlobalSettings.HEIGHT_CELL);
        finishObj.sprite = new Sprite(TextureManager.get("test"));

        GlobalSettings.gameObjects.add(finishObj);
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

    @Override
    public Sprite getBackground() {
        return background;
    }


    private void renderObjects(SpriteBatch batch) {
        for (var gameObj : GlobalSettings.gameObjects) {
            gameObj.render(batch);
        }
    }

    private void renderBackground(SpriteBatch batch) {
        background.draw(batch);

        for (var sprite : parallaxCity) {
            sprite.draw(batch);
        }

        for (var sprite : ground) {
            sprite.draw(batch);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        renderBackground(batch);

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
