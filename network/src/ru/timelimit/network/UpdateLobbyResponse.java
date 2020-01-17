package ru.timelimit.network;

public class UpdateLobbyResponse extends Response {
    public int countInRoom;
    public int[] usersInRoom;
    public boolean gameInProcess;
}