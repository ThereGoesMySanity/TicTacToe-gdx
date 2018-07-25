package tgms.ttt.GameState;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.TicTacToe;

public class MenuState extends GameState {

	private int currentChoice = 0;
	private String[] options;
	private Color titleColor;

	MenuState(GameStateManager gsm){
		this.gsm = gsm;
		try {
			titleColor = new Color (0x800000FF);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(){
	    if (gsm.getOnline().supported()) {
            options = new String[]{
                    "Start Local",
                    "Start Online",
                    "Options",
                    "Help",
                    "Quit"
            };
        } else {
            options = new String[]{
                    "Start",
                    "Options",
                    "Help",
                    "Quit"
            };
        }
    }

	@Override
	public void draw(ShapeRenderer s, SpriteBatch sb){
		s.setColor(Color.WHITE);
		s.set(ShapeRenderer.ShapeType.Filled);
		s.rect(0, 0, TicTacToe.WIDTH, TicTacToe.HEIGHT);
		sb.setColor(titleColor);
        GlyphLayout title = new GlyphLayout(font, "Tic-Tac-Toe");
		float x = ((TicTacToe.WIDTH - title.width) / 2);
		font.draw(sb, title, x, title.height+32);
		for(int i = 0; i < options.length; i++){
			if(i==currentChoice){
				sb.setColor(Color.DARK_GRAY);
			}else{
				sb.setColor(Color.LIGHT_GRAY);
			}
			font.draw(sb, options[i], getMenuX(), getMenuY() + (i * getMenuSpacing()));
		}
	}
	private void select(int choice){
		switch(choice){
		case 0:
			gsm.setState(GameStateManager.BOARDSTATE);
			break;
		case 1:
			gsm.setState(GameStateManager.BOARDSTATE_NET);
			break;
		case 2:
			gsm.setState(GameStateManager.OPTIONSSTATE);
			break;
		case 3:
		    //TODO: be snarky
			break;
		case 4:
			System.exit(0);
		}
	}
	private int getMenuX() {
		return TicTacToe.WIDTH / 2;
	}
	private int getMenuY() {
		return TicTacToe.HEIGHT / 3;
	}
	private int getMenuSpacing() {
		return 60;
	}
	
	public boolean keyPressed(int k){
		if(k == Input.Keys.ENTER){
			select(currentChoice);
		}
		if(k==Input.Keys.DOWN){
			currentChoice = (currentChoice+1)%options.length;
		}
		if(k==Input.Keys.UP){
			currentChoice = (currentChoice+options.length-1)%options.length;
		}
        return true;
    }

	@Override
	public boolean keyReleased(int k) { return false; }

	@Override
	public boolean mouseReleased(int x, int y) {
		// TODO Auto-generated method stub
		if(y >= getMenuY() - getMenuSpacing()
                && y <= getMenuY() + options.length * getMenuSpacing()
				&& x >= getMenuX()){
			select(currentChoice);
			return true;
		}
		return false;
	}

	@Override
	public void update() {
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		if(y >= getMenuY() - getMenuSpacing() && y <= getMenuY() + options.length * getMenuSpacing()
				&& x >= getMenuX()){
			currentChoice = (y - getMenuY() + getMenuSpacing()) / getMenuSpacing();
			return true;
		}
		return false;
	}
}
