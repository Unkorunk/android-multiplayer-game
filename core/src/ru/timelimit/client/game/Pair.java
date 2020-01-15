package ru.timelimit.client.game;

public class Pair {
    public int x, y;
    public Pair() {
        x = y = 0;
    }
    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object other)
    {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Pair pair = (Pair) other;
        return (this.x == pair.x && this.y == pair.y);
    }
}
