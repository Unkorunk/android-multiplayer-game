package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;

public class GlobalSettings {
    public static final int WORLD_HEIGHT = 360;
    public static final int WORLD_WIDTH = 1280;

    public static float WIDTH_CELL = 64.0f;
    public static float HEIGHT_CELL = 64.0f;

    public static int defaultHP = 100;

    public static int preparationTime = 10 * 1000;

    public static ArrayList<GameObject> gameObjects = new ArrayList<>();
    public static ArrayList<GameObject> getObjectsOnCell(Pair cell) {
        ArrayList<GameObject> result = new ArrayList<>();
        for (var gameObj : gameObjects) {
            if (gameObj.getCell().equals(cell)) {
                result.add(gameObj);
            }
        }
        return result;
    }
    public static boolean checkObjectOnCell(Pair cell) {
        return (getObjectsOnCell(cell).size() > 0);
    }

    public static boolean checkForType(Object candidate, Class<?> type){
        return type.isInstance(candidate);
    }

    public static void translateCamera(float deltaX, float deltaY, OrthographicCamera camera) {
        if (GlobalSettings.WORLD_HEIGHT - camera.viewportHeight / 2 - camera.position.y < deltaY) {
            deltaY = GlobalSettings.WORLD_HEIGHT - camera.viewportHeight / 2 - camera.position.y;
        }

        if (0 + camera.viewportHeight / 2 - camera.position.y > deltaY){
            deltaY = 0 + camera.viewportHeight / 2 - camera.position.y;
        }

        if (GlobalSettings.WORLD_WIDTH - camera.viewportWidth / 2 - camera.position.x < deltaX) {
            deltaX = GlobalSettings.WORLD_WIDTH - camera.viewportWidth / 2 - camera.position.x;
        }

        if (0 + camera.viewportWidth / 2 - camera.position.x > deltaX){
            deltaX = 0 + camera.viewportWidth / 2 - camera.position.x;
        }

        camera.translate(deltaX, deltaY);
    }
}
