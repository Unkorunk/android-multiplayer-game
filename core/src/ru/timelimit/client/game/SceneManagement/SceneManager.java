package ru.timelimit.client.game.SceneManagement;

import java.awt.*;

public class SceneManager {
    public Scene currentScene;

    public void setup() {
        currentScene = new PreparationScene();
        currentScene.instantiate();
    }

    public void checkScene() {
        if (currentScene.isOver() != 0) {
            nextLevel(currentScene.isOver());
        }
    }

    public void nextLevel(int code) {
        if (code == 1) {
            currentScene = new MenuScene();
        } else if (code == 2) {
            currentScene = new PreparationScene();
        } else if (code == 3) {
            currentScene = new GameScene();
        }
        currentScene.instantiate();
    }

    public void exitToMenu() {

    }
}
