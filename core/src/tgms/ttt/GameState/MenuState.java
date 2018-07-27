package tgms.ttt.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.TicTacToe;
import tgms.ttt.GameState.GameStateManager.State;

public class MenuState extends GameState {

    private int currentChoice = 0;
    private String[] options;
    private Color titleColor;

    MenuState(GameStateManager gsm) {
        super(gsm);
        titleColor = new Color(0x800000FF);
        if (gsm.platform().online != null) {
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
    public void draw(ShapeRenderer s, SpriteBatch sb) {
        sb.begin();
        font.setColor(titleColor);
        GlyphLayout title = new GlyphLayout(font, "Tic-Tac-Toe");
        float x = ((TicTacToe.WIDTH - title.width) / 2);
        font.draw(sb, title, x, title.height + 32);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                font.setColor(Color.DARK_GRAY);
            } else {
                font.setColor(Color.LIGHT_GRAY);
            }
            font.draw(sb, options[i], getMenuX(), getMenuY() + (i * getMenuSpacing()));
        }
        sb.end();
    }

    private void select(String c) {
        switch (c) {
            case "Start":
            case "Start Local":
                gsm.setState(State.BOARDSTATE);
                break;
            case "Start Online":
                gsm.setState(State.BOARDSTATE_NET);
                break;
            case "Options":
                gsm.setState(State.OPTIONSSTATE);
                break;
            //TODO: be snarky
            case "Quit":
                Gdx.app.exit();
                break;
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

    @Override
    public boolean keyReleased(int k) {
        if (k == Input.Keys.ENTER) {
            select(options[currentChoice]);
        }
        if (k == Input.Keys.DOWN) {
            currentChoice = (currentChoice + 1) % options.length;
        }
        if (k == Input.Keys.UP) {
            currentChoice = (currentChoice + options.length - 1) % options.length;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(int x, int y) {
        // TODO Auto-generated method stub
        if (y >= getMenuY() - getMenuSpacing()
                && y <= getMenuY() + options.length * getMenuSpacing()
                && x >= getMenuX()) {
            select(options[currentChoice]);
            return true;
        }
        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        if (y >= getMenuY() - getMenuSpacing()
                && y <= getMenuY() + options.length * getMenuSpacing()
                && x >= getMenuX()) {
            currentChoice = (y - getMenuY() + getMenuSpacing()) / getMenuSpacing();
            return true;
        }
        return false;
    }
}
