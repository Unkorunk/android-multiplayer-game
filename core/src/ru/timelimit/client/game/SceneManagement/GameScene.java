package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.timelimit.client.game.*;
import ru.timelimit.client.game.UI.GameUI;
import ru.timelimit.client.game.UI.UI;

public class GameScene implements Scene {

    private OrthographicCamera camera;
    private static UI gui = new GameUI();
    private Sprite background;

    private void objectsInit() {
        var player = new Entity();
        player.setBehaviour(new PlayerBehaviour());
        player.position = new Vector2(0, 0);
        player.sprite = new Sprite(TextureManager.get("Character"));

        GlobalSettings.gameObjects.add(player);
    }

    @Override
    public void instantiate() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(600, 600 * (height / width));
        // camera.setToOrtho(false, 600, 600 * (height / width));
        camera.position.set(GameClient.WORLD_WIDTH / 2f, GameClient.WORLD_HEIGHT / 2f, 0);
        camera.update();
        gui.init();

        background = new Sprite(TextureManager.get("BackgroundSky"));
        background.setPosition(0, 0);
        background.setSize(GameClient.WORLD_WIDTH, GameClient.WORLD_HEIGHT);

        objectsInit();
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

    @Override
    public void render(SpriteBatch batch) {
        updateObjects();

        background.draw(batch);

        gui.render(batch);

        renderObjects(batch);
    }
}
