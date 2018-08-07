package tgms.ttt.GameState;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.TicTacToe;
import tgms.ttt.GameState.GameStateManager.State;


public class GameOver extends GameState {
    //TODO: A lot
	BoardState board;
    GameOver(GameStateManager gsm, BoardState state) {
        super(gsm);
        board = state;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(ShapeRenderer s, SpriteBatch sb) {
    	board.draw(s, sb);
        sb.begin();
        font.setColor(Color.GREEN);
        GlyphLayout gameOver = new GlyphLayout(font, "Game Over");
        GlyphLayout winner = new GlyphLayout(font, gsm.WIN + " Wins");
        float x = ((TicTacToe.WIDTH - gameOver.width) / 2);
        float x2 = ((TicTacToe.WIDTH - winner.width) / 2);
        font.draw(sb, gameOver, x, 32);
        font.draw(sb, winner, x2, 128);
        sb.end();
    }

    @Override
    public boolean keyReleased(int k) {
        if (k == Input.Keys.ENTER) {
            select();
            return true;
        }
        return false;
    }

    private void select() {
        gsm.setState(State.BOARDSTATE);
    }

    @Override
    public boolean mouseReleased(int x, int y) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }

	@Override
	public void onResize() {
		board.onResize();
	}

	@Override
	public void dispose() {
	}
}
