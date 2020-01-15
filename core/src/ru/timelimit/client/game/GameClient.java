package ru.timelimit.client.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.timelimit.client.game.SceneManagement.SceneManager;
import ru.timelimit.client.game.UI.UI;
import ru.timelimit.client.game.UI.GameUI;

import java.util.ArrayList;

public class GameClient extends ApplicationAdapter {
	public static UI gui = new GameUI();
	public static final int WORLD_HEIGHT = 360;
	public static final int WORLD_WIDTH = 640;

	public SceneManager sceneManager;

	private OrthographicCamera camera;

	private SpriteBatch batch;
	private Sprite background;

	public static Vector2 lastClick;

	//private ArrayList<GameObject> gameObjects;

	private void texturesInit() {
		TextureManager.addTexture("test", "badlogic.jpg");
		TextureManager.addTexture("BackgroundSky", "Background/Background_sky.png");
		TextureManager.addTexture("Character", "Character/idle.gif");
		TextureManager.addTexture("Laser", "Sprites/Trap.png");
	}

	@Override
	public void create () {
		texturesInit();

		batch = new SpriteBatch();

		sceneManager = new SceneManager();
		sceneManager.setup();
	}

	private void updateObjects() {
		for (var gameObj : GlobalSettings.gameObjects) {
			gameObj.update();
		}
	}

	private void renderObjects() {
		for (var gameObj : GlobalSettings.gameObjects) {
			gameObj.render(batch);
		}
	}

	@Override
	public void render () {
		var touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		sceneManager.currentScene.getCamera().unproject(touch);
		lastClick = new Vector2(touch.x, touch.y);

		batch.setProjectionMatrix(sceneManager.currentScene.getCamera().combined);
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		sceneManager.currentScene.render(batch);

		batch.end();

		sceneManager.checkScene();
	}
	
	@Override
	public void dispose () {
		TextureManager.disposeAll();
		batch.dispose();
	}
}
