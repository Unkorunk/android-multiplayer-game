package ru.timelimit.server.game;

public class User {
    public int connectionId;
    public Room room;
    public int targetX, targetY;
    public float positionX, positionY;
    public User() {
        room = null;
        targetX = 0;
        targetY = 0;
        positionX = 0.0f;
        positionY = 0.0f;
    }
}