package ru.timelimit.client.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import ru.timelimit.client.game.SceneManagement.SceneManager;
import ru.timelimit.client.game.UI.UI;
import ru.timelimit.client.game.UI.GameUI;

import java.io.IOException;

public class GameClient extends ApplicationAdapter {
	public static GameClient instance;

	private CustomInputProcessor inputProcessor;
	private InputMultiplexer im;

	public SceneManager sceneManager;
	private SpriteBatch batch;

	public static Vector2 lastClick;


	private void texturesInit() {
		TextureManager.addTexture("test", "badlogic.jpg");
		TextureManager.addTexture("BackgroundSky", "Background/Background_sky.png");
		TextureManager.addTexture("Character", "Character/idle.gif");
		TextureManager.addTexture("Laser", "Sprites/Trap.png");
		TextureManager.addTexture("PlatformL", "Sprites/PlatformLeft.png");
		TextureManager.addTexture("PlatformM", "Sprites/PlatformMiddle.png");
		TextureManager.addTexture("PlatformR", "Sprites/PlatformRight.png");
	}

	public enum ActionClientEnum {
		CONNECT,
		DISCONNECT
	}

	public enum ActionServerEnum {
		OKAY
	}

	public static class ActionClient {
		public String accessToken;
		ActionClientEnum actionType;
	}

	public static class ActionServer {
		public ActionServerEnum actionType;
	}

	public static Client client = null;

	@Override
	public void create() {
		instance = this;
		texturesInit();

		// TODO: remove that
		client = new Client();
		client.start();

		client.getKryo().register(ru.timelimit.client.game.GameClient.ActionClientEnum.class);
		client.getKryo().register(ru.timelimit.client.game.GameClient.ActionClient.class);
		client.getKryo().register(ru.timelimit.client.game.GameClient.ActionServerEnum.class);
		client.getKryo().register(ru.timelimit.client.game.GameClient.ActionServer.class);

		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof ActionServer) {
					ActionServer actionServer = (ActionServer) object;
					System.out.println(actionServer.actionType.name());
				}
			}
		});

		new Thread(() -> {
			try {
				client.connect(5000, "194.67.87.216", 25567);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}).start();

		//~~~

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
	public void render() {
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
	public void dispose() {
		TextureManager.disposeAll();
		batch.dispose();
	}
}
