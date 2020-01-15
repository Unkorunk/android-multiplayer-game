package ru.timelimit.client.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameClient extends ApplicationAdapter {
	public static UI gui = new GameUI();

	OrthographicCamera camera;

	static final int WORLD_HEIGHT = 360;
	static final int WORLD_WIDTH = 640;

	SpriteBatch batch;
	Sprite background;
	
	@Override
	public void create () {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(200, 200 * (height / width));
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
        gui.init();
		batch = new SpriteBatch();

		background = new Sprite(TextureManager.addTexture("Background", "Background/Background_sky.png"));
		background.setPosition(0, 0);
		background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
	}

	@Override
	public void render () {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		TextureManager.disposeAll();
		batch.dispose();
	}
}
