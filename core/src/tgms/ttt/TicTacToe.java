package tgms.ttt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.GameState.GameStateManager;
import tgms.ttt.PlatformInterfaces.Platform;

public class TicTacToe extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private GameStateManager gsm;
    private Platform platform;
    private OrthographicCamera camera;
    public static int WIDTH, HEIGHT;

    public TicTacToe(Platform p) {
        platform = p;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.setToOrtho(true, WIDTH, HEIGHT);
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        batch.setProjectionMatrix(camera.combined);
    	shape.setProjectionMatrix(camera.combined);
        shape.setAutoShapeType(true);
        gsm = new GameStateManager(platform);
        Gdx.input.setInputProcessor(gsm);
    }

    @Override
    public void render() {
        gsm.update();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.draw(shape, batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
    }
    
    @Override
    public void resize(int w, int h) {
    	WIDTH = w;
    	HEIGHT = h;
    	Gdx.app.debug("test", w + " " + h);
    	camera.setToOrtho(true, WIDTH, HEIGHT);
    	batch.setProjectionMatrix(camera.combined);
    	shape.setProjectionMatrix(camera.combined);
    	gsm.onResize();
    }
}
