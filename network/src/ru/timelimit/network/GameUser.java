package ru.timelimit.network;

public class GameUser {
    public boolean isPlayer;
    public int targetX;
    public int targetY;
    public float positionX;
    public float positionY;

    public GameUser(boolean isPlayer, int targetX, int targetY, float positionX, float positionY) {
        this.isPlayer = isPlayer;
        this.targetX = targetX;
        this.targetY = targetY;
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
