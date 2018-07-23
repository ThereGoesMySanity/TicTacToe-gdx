package tgms.ttt.GameState;
import com.badlogic.gdx.graphics.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import tgms.ttt.Net.Connection;
public class GameStateManager {
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
	public BufferedImage xImage = null;
	public BufferedImage oImage = null;
	
	public GameStateManager(){
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
			try {
				Connection c = Connection.createConnection();
				if (c != null) {
					gameStates[BOARDSTATE_NET] = new NetBoardState(this, c);
				} else {
					setState(MENUSTATE);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public void update(){
		if(gameStates[currentState]!=null) gameStates[currentState].update();
	}

	public void draw(Graphics2D g){
		if(gameStates[currentState] != null) gameStates[currentState].draw(g);
	}
	public void keyPressed(int k){
		if(gameStates[currentState]!=null) gameStates[currentState].keyPressed(k);
	}
	public void keyReleased(int k){
		if(gameStates[currentState]!=null) gameStates[currentState].keyReleased(k);
	}
	public void mouseReleased(Point click) {
		if(gameStates[currentState]!=null) gameStates[currentState].mouseReleased(click);

	}
	public GameState getState(){
		return gameStates[currentState];
	}
	public void mouseMoved(MouseEvent e) {
		if(gameStates[currentState]!=null) gameStates[currentState].mouseMoved(e);
	}
	
}
