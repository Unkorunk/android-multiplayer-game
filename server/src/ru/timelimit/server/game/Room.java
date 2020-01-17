package ru.timelimit.server.game;

import ru.timelimit.network.Trap;

import java.util.ArrayList;
import java.util.Timer;

public class Room {
    public ArrayList<User> users = new ArrayList<>();
    public boolean gameInProcess = false;

    public ArrayList<Trap> traps = new ArrayList<>();

    public Timer preparationTimer = new Timer();
}
