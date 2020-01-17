package ru.timelimit.server.game;

import ru.timelimit.network.Trap;

import java.util.ArrayList;
import java.util.Timer;

public class Room {
    public int countInRoom = 0;
    public int[] usersInRoom = {-1, -1};
    public boolean gameInProcess = false;

    public ArrayList<Trap> traps = new ArrayList<>();

    private Timer preparationTimer;
}
