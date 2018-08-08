package tgms.ttt.GameState;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.PlatformInterfaces.Platform;

public class GameStateManager implements InputProcessor {
	private GameState currentState;
	public String WIN;
	
	public enum State { 
		MENUSTATE,
		BOARDSTATE,
		BOARDSTATE_NET,
		OPTIONSSTATE,
		GAMEOVER
	};
	
	public Color xColor = Color.RED;
	public Color oColor = Color.BLUE;
	public Color boardColor = Color.BLACK;
	public Texture xImage = null;
	public Texture oImage = null;

    private Platform platform;
	
	public GameStateManager(Platform p){
	    platform = p;
		setState(State.MENUSTATE);
	}
	
	private GameState loadState(State state){
		switch(state) {
			case MENUSTATE:
				return new MenuState(this);
			case BOARDSTATE:
				return new BoardState(this, 3, 3);
			case BOARDSTATE_NET:
	            return new NetBoardState(this);
			case OPTIONSSTATE:
				return new OptionsState(this);
			case GAMEOVER:
				return new GameOver(this, (BoardState)currentState);
			default:
				return null;
		}
	}

	public void setState(State boardstate){
		GameState s = loadState(boardstate);
		if (s != null) {
			unloadState(currentState);
			currentState = s;
		}
	}
	
	public void unloadState(GameState state) {
		if (state != null) {
			currentState.dispose();
		}
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int k) {
        return currentState != null && currentState.keyReleased(k);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return currentState != null
                && button == Input.Buttons.LEFT
                && currentState.mouseReleased(screenX, screenY);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
		return currentState != null
				&& currentState.mouseMoved(screenX, screenY);
	}

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void update(){
		if(currentState != null) currentState.update();
	}

	public void draw(ShapeRenderer s, SpriteBatch sb){
		if(currentState != null) currentState.draw(s, sb);
	}
	
	public void onResize() {
		if(currentState != null) currentState.onResize();
	}

    public Platform platform() {
        return platform;
    }

	public void dispose() {
		currentState.dispose();
	}
}
