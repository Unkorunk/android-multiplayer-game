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
import java.util.concurrent.locks.ReentrantLock;

public class PreparationScene implements Scene {
    public int exitCode = 0;

    private OrthographicCamera camera;
    private static UI gui = new PreparationUI();

    private Sprite background;
    private ArrayList<Sprite> ground;
    private ArrayList<Sprite> parallaxCity;

    private ArrayList<Trap> trapTypes;

    public int currentTrap = -1;

    private void trapInit() {
        trapTypes = new ArrayList<>();

        trapTypes.add(Trap.laserTrap.clone());
        trapTypes.add(Trap.flyTrap.clone());
    }

    public void updateGame(ru.timelimit.network.GameUser[] users, ru.timelimit.network.Trap[] traps) {
        for (int i = 0; i < traps.length; i ++) {
            if (GlobalSettings.checkObjectOnCell(new Pair(traps[i].x, traps[i].y))) {
                continue;
            }

            var newTrap = trapTypes.get(traps[i].trapId).clone();
            newTrap.setCell(new Pair(traps[i].x, traps[i].y));
            GlobalSettings.addObject(newTrap);
        }
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

        background = new Sprite(ResourceManager.getTexture("BackgroundSky"));
        background.setSize(camera.viewportWidth * 1.5f, camera.viewportHeight);
        background.setPosition(camera.viewportWidth * (1.0f - 1.5f) / 2, 0);

        int x = 0;
        ground = new ArrayList<>();
        parallaxCity = new ArrayList<>();
        while (x < GlobalSettings.WORLD_WIDTH) {
            var gSprite = new Sprite(ResourceManager.getTexture("BackgroundGround"));
            var cSprite = new Sprite(ResourceManager.getTexture("BackgroundCity"));
            gSprite.setPosition(x, -1);
            cSprite.setPosition(x, 16);
            ground.add(gSprite);
            parallaxCity.add(cSprite);
            x += gSprite.getWidth();
        }

//        preparationTimer = new Timer();
//        preparationTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                exitCode = 3;
//            }
//        }, GlobalSettings.preparationTime);

        ((PreparationUI)gui).initChooser(trapTypes, (Integer trap) -> {
            currentTrap = trap;
        });

        var finishObj = new Finish();
        finishObj.position = new Vector2(GlobalSettings.WORLD_WIDTH - 30, GlobalSettings.HEIGHT_CELL * 1.5f);
        finishObj.setSprite(new Sprite(ResourceManager.getTexture("Finish")), true);

        GlobalSettings.addObject(finishObj);
    }

    @Override
    public void dispose() {
        if (exitCode != 3) {
            GlobalSettings.clearObjects();
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
        GlobalSettings.locker.lock();
        for (var gameObj : GlobalSettings.getObjects()) {
            gameObj.render(batch);
        }
        GlobalSettings.locker.lock();
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
        if (clickedBtn == null && currentTrap != -1 && Gdx.input.justTouched()) {
            var pos = Pair.vectorToPair(GameClient.lastClick);
            if (trapTypes.get(currentTrap).validator(pos) && pos.x > 0 && pos.x < GlobalSettings.WORLD_WIDTH / GlobalSettings.WIDTH_CELL) {
                var trapPos = Pair.vectorToPair(GameClient.lastClick);
                GameClient.sendTrap(trapPos.x, trapPos.y, currentTrap);
//                var newTrap = trapTypes.get(currentTrap).clone();
//                newTrap.setCell(Pair.vectorToPair(GameClient.lastClick));
//                GlobalSettings.gameObjects.add(newTrap);
            }
        }

        gui.render(batch);

        renderObjects(batch);
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
