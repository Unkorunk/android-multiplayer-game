package ru.timelimit.client.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ru.timelimit.client.game.SceneManagement.SceneManager;
import ru.timelimit.client.game.UI.UI;
import ru.timelimit.client.game.UI.GameUI;

public class GameClient extends ApplicationAdapter {
	public static GameClient instance;

	private CustomInputProcessor inputProcessor;
	private InputMultiplexer im;

	public static UI gui = new GameUI();

	public SceneManager sceneManager;
	private SpriteBatch batch;

	public static Vector2 lastClick;


	private void texturesInit() {
		TextureManager.addTexture("test", "badlogic.jpg");
		TextureManager.addTexture("BackgroundSky", "Background/Background_sky.png");
		TextureManager.addTexture("Character", "Character/idle.gif");
		TextureManager.addTexture("Laser", "Sprites/Trap.png");
	}

	@Override
	public void create () {
		instance = this;
		texturesInit();

		batch = new SpriteBatch();

		im = new InputMultiplexer();

		inputProcessor = new CustomInputProcessor();
		GestureDetector gd = new GestureDetector(inputProcessor);

		sceneManager = new SceneManager();
		sceneManager.setup();

		inputProcessor.updateCamera(sceneManager.currentScene.getCamera());

		im.addProcessor(inputProcessor);
		im.addProcessor(gd);

		Gdx.input.setInputProcessor(im);
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

		inputProcessor.updateCamera(sceneManager.currentScene.getCamera());
		sceneManager.currentScene.getCamera().update();

		sceneManager.checkScene();
	}
	
	@Override
	public void dispose () {
		TextureManager.disposeAll();
		batch.dispose();
	}
}
