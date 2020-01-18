package ru.timelimit.client.game.Behaviours;

import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.Pair;


public class RemoteBehaviour implements BehaviourModel {
    public Vector2 lazyPos = Pair.pairToVector(new Pair(0, 1));

    @Override
    public Command update() {
        return null;
    }

    @Override
    public void sendPos(float x, float y, int xt, int yt) { }

    @Override
    public boolean receivePos(Vector2 pos) {
        System.out.println("Pos received: " + lazyPos.x + " " + lazyPos.y);
        pos = lazyPos;
        return true;
    }
}
