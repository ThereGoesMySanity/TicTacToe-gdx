package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameState {
    BitmapFont font;
    GameStateManager gsm;

    GameState(GameStateManager gsm) {
        font = gsm.platform().getFont();
        this.gsm = gsm;
    }

    public void update() {}

    public abstract void draw(ShapeRenderer s, SpriteBatch sb);

    public abstract boolean keyReleased(int k);

    public abstract boolean mouseReleased(int x, int y);

    public abstract boolean mouseMoved(int x, int y);
    
    public abstract void onResize();
}
