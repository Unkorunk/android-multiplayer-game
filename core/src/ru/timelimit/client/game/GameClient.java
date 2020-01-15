package ru.timelimit.client.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.timelimit.client.game.UI.UI;
import ru.timelimit.client.game.UI.GameUI;

import java.util.ArrayList;

public class GameClient extends ApplicationAdapter {
	public static UI gui = new GameUI();
	public static final int WORLD_HEIGHT = 360;
	public static final int WORLD_WIDTH = 640;

	private OrthographicCamera camera;

	private SpriteBatch batch;
	private Sprite background;

	private ArrayList<GameObject> gameObjects;

	private void texturesInit() {
		TextureManager.addTexture("test", "badlogic.jpg");
		TextureManager.addTexture("BackgroundSky", "Background/Background_sky.png");
		TextureManager.addTexture("Character", "Character/idle.gif");
	}

	private void objectsInit() {
		gameObjects = new ArrayList<>();

		var player = new Entity();
		player.setBehaviour(new PlayerBehaviour());
		player.position = new Vector2(0, 0);
		player.sprite = new Sprite(TextureManager.get("Character"));

		gameObjects.add(player);
	}

	@Override
	public void create () {
		texturesInit();

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(600, 600 * (height / width));
        // camera.setToOrtho(false, 600, 600 * (height / width));
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
        gui.init();
		batch = new SpriteBatch();

		background = new Sprite(TextureManager.get("BackgroundSky"));
		background.setPosition(0, 0);
		background.setSize(WORLD_WIDTH, WORLD_HEIGHT);

		objectsInit();
	}

	private void updateObjects() {
		for (var gameObj : gameObjects) {
			gameObj.update();
		}
	}

	private void renderObjects() {
		for (var gameObj : gameObjects) {
			gameObj.render(batch);
		}
	}

	@Override
	public void render () {
		var touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(touch);

		UI.lastClick = new Vector2(touch.x, touch.y);

		updateObjects();

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);

		gui.render(batch);

		renderObjects();

		batch.end();
	}
	
	@Override
	public void dispose () {
		TextureManager.disposeAll();
		batch.dispose();
	}
}
