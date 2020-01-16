package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.*;
import ru.timelimit.client.game.UI.GameUI;
import ru.timelimit.client.game.UI.UI;

public class GameScene implements Scene {
    public int exitCode = 0;

    private Entity player;

    private OrthographicCamera camera;
    private static UI gui = new GameUI();
    private Sprite background;

    private void objectsInit() {
        player = new Entity();
        player.setBehaviour(new PlayerBehaviour());
        player.sprite = new Sprite(TextureManager.get("Character"));
        player.setCell(new Pair(1, 1));

        var laser = new Trap();
        laser.sprite = new Sprite(TextureManager.get("Laser"));
        laser.commands.add(BehaviourModel.Command.JUMP);
        laser.setCell(new Pair(5, 1));

        GlobalSettings.gameObjects.add(player);
        GlobalSettings.gameObjects.add(laser);
    }

    @Override
    public void instantiate() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(600, 600 * (height / width));
        // camera.setToOrtho(false, 600, 600 * (height / width));
        camera.position.set(GlobalSettings.WORLD_WIDTH / 2f, GlobalSettings.WORLD_HEIGHT / 2f, 0);
        camera.update();
        gui.init();

        background = new Sprite(TextureManager.get("BackgroundSky"));
        background.setPosition(0, 0);
        background.setSize(GlobalSettings.WORLD_WIDTH, GlobalSettings.WORLD_HEIGHT);

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

        GlobalSettings.translateCamera(player.position.x - camera.position.x, player.position.y - camera.position.y, camera);

        if (player.position.x >= GlobalSettings.gameObjects.get(0).position.x){
            exitCode = 2;
        }
    }

    @Override
    public int isOver() {
        return exitCode;
    }
}
