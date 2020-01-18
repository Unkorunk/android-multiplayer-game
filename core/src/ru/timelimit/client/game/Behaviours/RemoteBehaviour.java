package ru.timelimit.client.game.Behaviours;

import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.Pair;

import java.util.concurrent.locks.ReentrantLock;


public class RemoteBehaviour implements BehaviourModel {
    public Vector2 lazyPos = Pair.pairToVector(new Pair(0, 1));
    public ReentrantLock locker = new ReentrantLock();

    @Override
    public Command update() {
        return null;
    }

    @Override
    public void sendPos(float x, float y, int xt, int yt) { }

    public void setLazyPos(Vector2 pos) {
        locker.lock();
        lazyPos = pos;
        locker.unlock();
    }

    @Override
    public boolean receivePos(Vector2 pos) {
        locker.lock();
        System.out.println("Pos received: " + lazyPos.x + " " + lazyPos.y);
        pos = lazyPos;
        locker.unlock();
        return true;
    }
}
