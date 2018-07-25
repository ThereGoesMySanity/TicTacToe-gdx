package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.event.KeyEvent;

import tgms.ttt.TicTacToe;


public class GameOver extends GameState {
    //TODO: A lot
	public GameOver(GameStateManager gsm){
		this.gsm = gsm;
	}
	public void init() {
		
	}

	public void update() {}

	@Override
	public void draw(ShapeRenderer s, SpriteBatch sb) {
		GlyphLayout gameOver = new GlyphLayout(font, "Game Over");
		GlyphLayout winner = new GlyphLayout(font, gsm.WIN + " Wins");
        float x = ((TicTacToe.WIDTH - gameOver.width) / 2);
        float x2 = ((TicTacToe.WIDTH - winner.width)/ 2);
        sb.setColor(Color.GREEN);
		font.draw(sb, winner, x, 32);
		font.draw(sb, winner, x2, 128);
	}

    @Override
    public boolean keyPressed(int k) {
        return false;
    }

    @Override
	public boolean keyReleased(int k) {
        if (k == KeyEvent.VK_ENTER){
            select();
            return true;
        }
        return false;
    }

	public void select(){
		gsm.setState(GameStateManager.BOARDSTATE);
	}

	@Override
	public boolean mouseReleased(int x, int y) {
	    return false;
    }

	@Override
	public boolean mouseMoved(int x, int y) {
        return false;
    }
}
