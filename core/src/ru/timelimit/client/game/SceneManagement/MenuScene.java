package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.timelimit.client.game.GlobalSettings;
import ru.timelimit.client.game.UI.UI;

public class MenuScene implements Scene {
    public int exitCode = 0;

    @Override
    public void instantiate() {
        // TODO: implement MenuScene
    }

    @Override
    public void dispose() {
        GlobalSettings.gameObjects.clear();
    }

    @Override
    public UI getUI() {
        // TODO: implement MenuScene
        return null;
    }

    @Override
    public OrthographicCamera getCamera() {
        // TODO: implement MenuScene
        return null;
    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO: implement MenuScene
    }

    @Override
    public int isOver() {
        return 0;
    }
}
