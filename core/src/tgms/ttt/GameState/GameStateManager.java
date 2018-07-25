package tgms.ttt.GameState;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.IOException;

import tgms.ttt.Net.Connection;
import tgms.ttt.PlatformInterfaces.OSQuery;
import tgms.ttt.PlatformInterfaces.Online;
import tgms.ttt.PlatformInterfaces.Platform;

public class GameStateManager implements InputProcessor {
	private GameState[] gameStates;
	private int currentState;
	public String WIN;
	public static final int NUMGAMESTATES = 5;
	
	public static final int MENUSTATE = 0;
	public static final int BOARDSTATE = 1;
	public static final int BOARDSTATE_NET = 2;
	public static final int OPTIONSSTATE = 3;
	public static final int GAMEOVER = 4;

	public Color xColor = Color.RED;
	public Color oColor = Color.BLUE;
	public Color boardColor = Color.BLACK;
	public Texture xImage = null;
	public Texture oImage = null;

    private Platform platform;
	
	public GameStateManager(Platform p){
	    platform = p;
		gameStates = new GameState[NUMGAMESTATES];
		currentState = MENUSTATE;
		loadState(currentState);
	}
	private void loadState(int state){
		if(state == MENUSTATE){
			gameStates[state] = new MenuState(this);
		}
		if(state == BOARDSTATE){
			gameStates[state] = new BoardState(this, 3, 3);
		}
		if(state == BOARDSTATE_NET){
            Connection c = getOnline().getConnection();
            if (c != null) {
                gameStates[BOARDSTATE_NET] = new NetBoardState(this, c);
            } else {
                setState(MENUSTATE);
            }
        }
		if(state == OPTIONSSTATE){
			gameStates[state] = new OptionsState(this);
		}
		if(state == GAMEOVER){
			gameStates[state] = new GameOver(this);
		}
	}

	private void unloadState(int state){
		gameStates[state] = null;
	}

	public void setState(int state){
		unloadState(currentState);
		currentState = state;
		loadState(state);
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int k) {
        return gameStates[currentState] != null && gameStates[currentState].keyReleased(k);
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
        return gameStates[currentState] != null
                && button == Input.Buttons.LEFT
                && gameStates[currentState].mouseReleased(screenX, screenY);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
		return gameStates[currentState] != null
				&& gameStates[currentState].mouseMoved(screenX, screenY);
	}

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void update(){
		if(gameStates[currentState]!=null) gameStates[currentState].update();
	}

	public void draw(ShapeRenderer s, SpriteBatch sb){
		if(gameStates[currentState] != null) gameStates[currentState].draw(s, sb);
	}

	public OSQuery getOS() {
	    return platform.getOS();
    }

    public Online getOnline() {
        return platform.getOnline();
    }
}
