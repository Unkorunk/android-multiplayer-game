package ru.timelimit.client.game.SceneManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.timelimit.client.game.*;
import ru.timelimit.client.game.Behaviours.PlayerBehaviour;
import ru.timelimit.client.game.Behaviours.RemoteBehaviour;
import ru.timelimit.client.game.UI.GameUI;
import ru.timelimit.client.game.UI.UI;

import java.util.ArrayList;

public class GameScene implements Scene {
    private int exitCode = 0;

    private Entity player;
    private Entity remotePlayer;

    private OrthographicCamera camera;
    private UI gui = new GameUI();
    private ArrayList<Sprite> skyBackground;
    private ArrayList<Sprite> groundBackground;
    private ArrayList<Sprite> cityBackground;

    private void objectsInit() {
        player = new Entity();
        player.setBehaviour(new PlayerBehaviour());
        player.setSprite(new Sprite(ResourceManager.getTexture("Character")), true);
        player.setCell(new Pair(0, 1));

        remotePlayer = new Entity();
        remotePlayer.setBehaviour(new RemoteBehaviour());
        var sprite =  new Sprite(ResourceManager.getTexture("Character"));
        sprite.setColor(1, 0.5f, 0.5f, 0.5f);
        remotePlayer.setSprite(sprite, true);
        remotePlayer.setCell(new Pair(0, 1));

        GlobalSettings.addObject(player);
        GlobalSettings.addObject(remotePlayer);
    }

    @Override
    public void instantiate() {
        Scene.super.instantiate();

        int x = 0;
        skyBackground = new ArrayList<>();
        skyBackground.add(new Sprite(ResourceManager.getTexture("BackgroundSky")));
        skyBackground.get(0).setSize(camera.viewportWidth * 1.5f, camera.viewportHeight);
        skyBackground.get(0).setPosition(camera.viewportWidth * (1.0f - 1.5f) / 2, 0);

        groundBackground = new ArrayList<>();
        cityBackground = new ArrayList<>();
        while (x < GlobalSettings.WORLD_WIDTH) {
            var gSprite = new Sprite(ResourceManager.getTexture("BackgroundGround"));
            var cSprite = new Sprite(ResourceManager.getTexture("BackgroundCity"));
            gSprite.setPosition(x, -1);
            cSprite.setPosition(x, 16);
            groundBackground.add(gSprite);
            cityBackground.add(cSprite);
            x += gSprite.getWidth();
        }

        objectsInit();
    }

    @Override
    public UI getUI() {
        return gui;
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void translateCamera(float delta) {
        GlobalSettings.parallaxTranslate(camera, getFirstPlane(), 1f, getSecondPlane(), 0.25f, getThirdPlane(), 0f, delta);
    }

    @Override
    public ArrayList<Sprite> getFirstPlane() {
        return groundBackground;
    }

    @Override
    public ArrayList<Sprite> getSecondPlane() {
        return cityBackground;
    }

    @Override
    public ArrayList<Sprite> getThirdPlane() {
        return skyBackground;
    }

    public void updateGame(ru.timelimit.network.GameUser[] users, ru.timelimit.network.Trap[] traps) {
        for (int i = 0; i < users.length; i++) {
            if (!users[i].isPlayer) {
                ((RemoteBehaviour)remotePlayer.getBehaviour()).setLazyPos(new Vector2(users[i].positionX, users[i].positionY));
            }
        }
    }



    private void updateObjects() {
        GlobalSettings.locker.lock();
        for (var gameObj : GlobalSettings.getObjects()) {
            gameObj.update();
        }
        GlobalSettings.locker.unlock();
    }

    private void renderObjects(SpriteBatch batch) {
        GlobalSettings.locker.lock();
        for (var gameObj : GlobalSettings.getObjects()) {
            gameObj.render(batch);
        }
        GlobalSettings.locker.unlock();
    }

    private void renderBackground(SpriteBatch batch) {
        for (var sprite : skyBackground) {
            sprite.draw(batch);
        }

        for (var sprite : cityBackground) {
            sprite.draw(batch);
        }

        for (var sprite : groundBackground) {
            sprite.draw(batch);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (Gdx.input.justTouched())
            gui.findClicked();

        ((GameUI)gui).updateHp(player.getHp());
        updateObjects();

        renderBackground(batch);

        renderObjects(batch);

        translateCamera(player.position.x - camera.position.x);

        gui.render(batch);

        if (player.position.x >= GlobalSettings.getObject(0).position.x || !player.isEnabled){
            System.out.println("Disconnect: " + player.position.x + " " + player.position.y + " " + player.isEnabled);
            exitCode = 1;
            GameClient.sendDisconnect();
        }
    }

    @Override
    public int isOver() {
        return exitCode;
    }

    @Override
    public void setState(int state) {
        exitCode = state;
    }
}
