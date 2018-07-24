package tgms.ttt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.GameState.GameStateManager;

public class TicTacToe extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shape;
	GameStateManager gsm;
	public static int WIDTH, HEIGHT;

	public TicTacToe(int w, int h) {
	    super();
		WIDTH = w;
		HEIGHT = h;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
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
