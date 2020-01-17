package ru.timelimit.client.game;

import com.badlogic.gdx.math.Vector2;

public final class Pair {
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

    public static Vector2 pairToVector(Pair pair) {
        return new Vector2(
                pair.x * GlobalSettings.WIDTH_CELL + GlobalSettings.WIDTH_CELL / 2.0f,
                pair.y * GlobalSettings.HEIGHT_CELL + GlobalSettings.HEIGHT_CELL / 2.0f
        );
    }

    public static Pair vectorToPair(Vector2 vec) {
        return new Pair((int) Math.floor(vec.x / GlobalSettings.WIDTH_CELL),
                        (int) Math.floor(vec.y / GlobalSettings.HEIGHT_CELL));
    }
}
