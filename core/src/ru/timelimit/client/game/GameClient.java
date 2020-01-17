package ru.timelimit.client.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ru.timelimit.client.game.SceneManagement.SceneManager;
import ru.timelimit.client.game.UI.Button;
import ru.timelimit.client.game.UI.Label;
import ru.timelimit.client.game.UI.MenuUI;
import ru.timelimit.network.*;

import java.io.IOException;

public final class GameClient extends ApplicationAdapter {

	public static GameClient instance;

	public String token = null;

	private CustomInputProcessor inputProcessor;
	private InputMultiplexer im;

	public SceneManager sceneManager;
	private SpriteBatch batch;

	public static Vector2 lastClick;


	private void texturesInit() {
		ResourceManager.addTexture("test", "badlogic.jpg");

		ResourceManager.addTexture("BackgroundSky", "Background/Background_sky.png");
		ResourceManager.addTexture("BackgroundCity", "Background/Parallax_bg_city.png");
		ResourceManager.addTexture("BackgroundGround", "Background/New_Ground.png");
		ResourceManager.addTexture("Character", "Character/idle.gif");
		ResourceManager.addTexture("Laser", "Sprites/Trap.png");
		ResourceManager.addTexture("FlyTrap", "Sprites/Fly_trap.gif");
		ResourceManager.addTexture("PlatformL", "Sprites/PlatformLeft.png");
		ResourceManager.addTexture("PlatformM", "Sprites/PlatformMiddle.png");
		ResourceManager.addTexture("PlatformR", "Sprites/PlatformRight.png");
		ResourceManager.addTexture("BtnUp", "Sprites/Button_up.png");
		ResourceManager.addTexture("BtnDown", "Sprites/Button_down.png");
		ResourceManager.addTexture("BtnMenu", "Sprites/Button_menu.png");
		ResourceManager.addTexture("BtnEmpty", "Sprites/Button_empty.png");
		ResourceManager.addTexture("Finish", "Sprites/Finish.gif");
		ResourceManager.addTexture("BtnExit", "Sprites/Button_exit.png");
		ResourceManager.addTexture("TextField", "Sprites/TextField.png");

		var fontGen = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/font.ttf"));
		var fontParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParam.size = 20;
		ResourceManager.addFont("defaultFont", fontGen.generateFont(fontParam));

		fontGen.dispose();
	}

	public static Client client = null;

	public static void sendConnect(String login, String password) {
		var actionClient = new ActionClient();
		actionClient.actionType = ActionClientEnum.CONNECT;

		var req = new ConnectRequest();
		req.password = login;
		req.username = password;

		actionClient.request = req;

		GameClient.client.sendTCP(actionClient);
	}

	public static void join() {
		var actionClient = new ActionClient();
		actionClient.actionType = ActionClientEnum.JOIN;
		actionClient.accessToken = GameClient.instance.token;

		GameClient.client.sendTCP(actionClient);
	}

	@Override
	public void create() {
		instance = this;
		texturesInit();

		client = new Client();
		client.start();

		Network.register(client);

		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof ActionServer) {
					ActionServer actionServer = (ActionServer) object;
					System.out.println(actionServer.actionType.name());
					if (actionServer.actionType == ActionServerEnum.CONNECT) {
						if (actionServer.response instanceof ConnectResponse) {
							var response = (ConnectResponse)actionServer.response;
							if (response.accessToken.equals("FAILED") || response.accessToken.equals("INCORRECT PASSWORD")) {
								sceneManager.currentScene.getUI().errorLabel.setText(response.accessToken);
							} else {
								token = response.accessToken;
								GameClient.join();
								var ui = (MenuUI)sceneManager.currentScene.getUI();
								ui.startTimer = 10;
								((Label)ui.getElement("createLobbyBtn").getChildren(Label.class)).setText("Waiting for game (Click to leave)");
								var bounds = ui.getElement("createLobbyBtn").getBounds();
								ui.getElement("createLobbyBtn").setBounds(new Rectangle(bounds.x - 100, bounds.y, bounds.width + 200, bounds.height));
							}
						}
					} else if (actionServer.actionType == ActionServerEnum.START_PREPARATION) {

					} else if (actionServer.actionType == ActionServerEnum.START_GAME) {
						sceneManager.currentScene.setState(2);
					}

				}
			}
		});

		//~~~

		batch = new SpriteBatch();

		im = new InputMultiplexer();

		inputProcessor = new CustomInputProcessor();
		GestureDetector gd = new GestureDetector(inputProcessor);

		sceneManager = new SceneManager();
		sceneManager.setup();

		new Thread(() -> {
			try {
				client.connect(5000, "194.67.87.216", 25568);
			} catch (IOException e) {
				e.printStackTrace();
				sceneManager.currentScene.getUI().errorLabel.setText("Couldnt connect to server");
			}
		}).start();

		inputProcessor.updateCamera(sceneManager.currentScene.getCamera(), sceneManager.currentScene.getBackground());

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

		inputProcessor.updateCamera(sceneManager.currentScene.getCamera(), sceneManager.currentScene.getBackground());
		sceneManager.currentScene.getCamera().update();

		sceneManager.checkScene();
	}

	@Override
	public void dispose() {
		ResourceManager.disposeAll();
		batch.dispose();
	}
}
