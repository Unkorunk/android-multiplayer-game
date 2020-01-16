package ru.timelimit.client.game;

import java.util.ArrayList;

public class GlobalSettings {
    public static float WIDTH_CELL = 64.0f;
    public static float HEIGHT_CELL = 64.0f;

    public static int defaultHP = 100;

    public static int preparationTime = 100 * 1000;

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
}
