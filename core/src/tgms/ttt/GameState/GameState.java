package tgms.ttt.GameState;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;

import java.awt.Point;
import java.awt.event.MouseEvent;

public abstract class GameState {
	protected GameStateManager gsm;
	public abstract void init();
	public abstract void update();
	public abstract void draw(ShapeRenderer s, SpriteBatch sb);
	public abstract boolean keyPressed(int k);
	public abstract boolean keyReleased(int k);
	public abstract boolean mouseReleased(int x, int y);
	public abstract boolean mouseMoved(int x, int y);
}
