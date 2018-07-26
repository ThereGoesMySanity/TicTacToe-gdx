package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.event.KeyEvent;

import tgms.ttt.TicTacToe;


public class GameOver extends GameState {
    //TODO: A lot
    GameOver(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(ShapeRenderer s, SpriteBatch sb) {
        sb.begin();
        font.setColor(Color.GREEN);
        GlyphLayout gameOver = new GlyphLayout(font, "Game Over");
        GlyphLayout winner = new GlyphLayout(font, gsm.WIN + " Wins");
        float x = ((TicTacToe.WIDTH - gameOver.width) / 2);
        float x2 = ((TicTacToe.WIDTH - winner.width) / 2);
        font.draw(sb, winner, x, 32);
        font.draw(sb, winner, x2, 128);
        sb.end();
    }

    @Override
    public boolean keyReleased(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
            return true;
        }
        return false;
    }

    private void select() {
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
