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


public class PreparationScene implements Scene {
    private int exitCode = 0;

    private int money = 100;

    private OrthographicCamera camera;
    private UI gui = new PreparationUI();

    private ArrayList<Sprite> skyBackground;
    private ArrayList<Sprite> groundBackground;
    private ArrayList<Sprite> cityBackground;

    private ArrayList<Trap> trapTypes;

    public int currentTrap = -1;

    private void trapInit() {
        trapTypes = new ArrayList<>();

        trapTypes.add(Trap.laserTrap.clone());

        trapTypes.add(Trap.flyTrap.clone());
        trapTypes.get(1).setOffset(0, 32);
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

        Scene.super.instantiate();

        skyBackground = new ArrayList<>();
        groundBackground = new ArrayList<>();
        cityBackground = new ArrayList<>();

        skyBackground.add(new Sprite(ResourceManager.getTexture("BackgroundSky")));
        skyBackground.get(0).setSize(camera.viewportWidth * 1.5f, camera.viewportHeight);
        skyBackground.get(0).setPosition(camera.viewportWidth * (1.0f - 1.5f) / 2, 0);

        int x = 0;
        cityBackground = new ArrayList<>();
        groundBackground = new ArrayList<>();
        while (x < GlobalSettings.WORLD_WIDTH) {
            var gSprite = new Sprite(ResourceManager.getTexture("BackgroundGround"));
            var cSprite = new Sprite(ResourceManager.getTexture("BackgroundCity"));
            gSprite.setPosition(x, -1);
            cSprite.setPosition(x, 16);
            groundBackground.add(gSprite);
            cityBackground.add(cSprite);
            x += gSprite.getWidth();
        }

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
            Scene.super.dispose();
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
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void translateCamera(float delta) {
        GlobalSettings.parallaxTranslate(camera, getFirstPlane(), 1f, getSecondPlane(), 0.25f, getThirdPlane(), 0f, delta);
    }

    @Override
    public ArrayList<Sprite> getFirstPlane() {
        return groundBackground;
    }

    @Override
    public ArrayList<Sprite> getSecondPlane() {
        return cityBackground;
    }

    @Override
    public ArrayList<Sprite> getThirdPlane() {
        return skyBackground;
    }

    private void renderObjects(SpriteBatch batch) {
        GlobalSettings.locker.lock();
        for (var gameObj : GlobalSettings.getObjects()) {
            gameObj.render(batch);
        }
        GlobalSettings.locker.unlock();;
    }

    private void renderBackground(SpriteBatch batch) {
        for (var sprite : skyBackground) {
            sprite.draw(batch);
        }

        for (var sprite : cityBackground) {
            sprite.draw(batch);
        }

        for (var sprite : groundBackground) {
            sprite.draw(batch);
        }
        ((PreparationUI)gui).updateMoney(money);
    }

    @Override
    public void render(SpriteBatch batch) {
        renderBackground(batch);

        String clickedBtn = null;
        if (Gdx.input.justTouched())
            clickedBtn = gui.findClicked();

        if (clickedBtn != null) {
            System.out.println(2);
        }
        if (clickedBtn == null && currentTrap != -1 && Gdx.input.justTouched()) {
            var pos = Pair.vectorToPair(GameClient.lastClick);
            if (trapTypes.get(currentTrap).validator(pos) && pos.x > 0 && pos.x < GlobalSettings.WORLD_WIDTH / GlobalSettings.WIDTH_CELL
                && money >= trapTypes.get(currentTrap).getCost()) {
                money -= trapTypes.get(currentTrap).getCost();
                var trapPos = Pair.vectorToPair(GameClient.lastClick);
                GameClient.sendTrap(trapPos.x, trapPos.y, currentTrap);
            }
        }

        renderObjects(batch);
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
