package ru.timelimit.client.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.SceneManagement.MenuScene;
import ru.timelimit.client.game.SceneManagement.PreparationScene;
import ru.timelimit.client.game.UI.MenuUI;

public final class CustomInputProcessor implements InputProcessor, GestureDetector.GestureListener {
    CustomInputProcessor(GameClient gameClient) {
        gc = gameClient;
    }
    private GameClient gc;
    private OrthographicCamera cam;
    private Sprite bg;

    public void updateCamera(OrthographicCamera camera, Sprite background) {
        cam = camera;
        bg = background;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (GlobalSettings.checkForType(gc.sceneManager.currentScene, MenuScene.class)) {
            var tf = ((MenuUI)gc.sceneManager.currentScene.getUI()).activeField.origin;

            if (character == 8){
                if (tf.getText().length() > 0) {
                    tf.setText(tf.getText().substring(0, tf.getText().length() - 1));
                }
            } else if (tf.getText().length() < 20 && ((character > 64 && character < 91) || (character > 96 && character < 123)
                    || (character > 47 && character < 58))) {
                tf.setText(tf.getText() + character);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (GlobalSettings.checkForType(gc.sceneManager.currentScene, PreparationScene.class)) {
            deltaX = -deltaX * (cam.viewportWidth / Gdx.graphics.getWidth());
            deltaY = deltaY * (cam.viewportHeight / Gdx.graphics.getHeight());

            GlobalSettings.translateCamera(deltaX, deltaY, cam, bg);
            ((PreparationScene) gc.sceneManager.currentScene).currentTrap = -1;
            return true;
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
