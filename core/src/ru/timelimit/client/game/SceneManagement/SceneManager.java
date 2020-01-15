package ru.timelimit.client.game.SceneManagement;

public class SceneManager {
    public Scene currentScene;

    public void setup() {
        currentScene = new GameScene();
        currentScene.instantiate();
    }

    public void nextLevel() {

    }

    public void exitToMenu() {

    }
}
