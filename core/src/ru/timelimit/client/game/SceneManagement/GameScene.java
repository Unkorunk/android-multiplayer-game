package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.*;
import ru.timelimit.client.game.Behaviours.PlayerBehaviour;
import ru.timelimit.client.game.UI.GameUI;
import ru.timelimit.client.game.UI.UI;

import java.util.ArrayList;

public class GameScene implements Scene {
    public int exitCode = 0;

    private Entity player;

    private OrthographicCamera camera;
    private static UI gui = new GameUI();
    private Sprite background;
    private ArrayList<Sprite> ground;
    private ArrayList<Sprite> parallaxCity;

    private void objectsInit() {
        player = new Entity();
        player.setBehaviour(new PlayerBehaviour());
        player.setSprite(new Sprite(ResourceManager.getTexture("Character")), true);
        player.setCell(new Pair(0, 1));

        GlobalSettings.gameObjects.add(player);
    }

    @Override
    public void instantiate() {
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

        objectsInit();
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

    private void updateObjects() {
        for (var gameObj : GlobalSettings.gameObjects) {
            gameObj.update();
        }
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
        updateObjects();

        renderBackground(batch);

        gui.render(batch);

        renderObjects(batch);

        GlobalSettings.translateCamera(player.position.x - camera.position.x,
                player.position.y - camera.position.y, camera, background);

        if (player.position.x >= GlobalSettings.gameObjects.get(0).position.x || !player.isEnabled){
            exitCode = 2;
        }
        String clickedBtn = gui.findClicked();
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
