package tgms.ttt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.GameState.GameStateManager;
import tgms.ttt.PlatformInterfaces.Platform;

public class TicTacToe extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private GameStateManager gsm;
	private Platform platform;
	public static int WIDTH, HEIGHT;

	public TicTacToe(Platform p) {
	    platform = p;
    }

	@Override
	public void create () {
	    Graphics.DisplayMode m = Gdx.graphics.getDisplayMode();
	    WIDTH = m.width;
	    HEIGHT = m.height;
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		gsm = new GameStateManager(platform);
	}

	@Override
	public void render () {
	    gsm.update();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		shape.begin();
		gsm.draw(shape, batch);
		shape.end();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shape.dispose();
	}
}
