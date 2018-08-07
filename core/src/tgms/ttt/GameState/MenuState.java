package tgms.ttt.GameState;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import tgms.ttt.TicTacToe;
import tgms.ttt.GameState.GameStateManager.State;

public class MenuState extends GameState {

    private int currentChoice = 0;
    private ArrayList<String> options;
    private Rectangle[] rects;
    private Color titleColor;

    MenuState(GameStateManager gsm) {
        super(gsm);
        titleColor = new Color(0x800000FF);
        options = new ArrayList<>();
        if (gsm.platform().online != null) {
            options.add("Start Local");
            options.add("Start Online");
        } else {
            options.add("Start");
        }
        options.add("Options");
        if(Gdx.app.getType() == ApplicationType.Desktop) {
        	options.add("Quit");
        }
        rects = new Rectangle[options.size()];
        float x = TicTacToe.WIDTH / 2f;
        float y = TicTacToe.HEIGHT / 3f;
        int spacing = 60;
        for (int i = 0; i < options.size(); i++) {
        	rects[i] = new Rectangle(x, y + (i - 0.2f) * spacing, 
        			TicTacToe.WIDTH - x, spacing * 0.6f);
        }
    }

    @Override
    public void draw(ShapeRenderer s, SpriteBatch sb) {
        sb.begin();
        font.setColor(titleColor);
        GlyphLayout title = new GlyphLayout(font, "Tic-Tac-Toe");
        float x = ((TicTacToe.WIDTH - title.width) / 2);
        font.draw(sb, title, x, title.height + 32);
        for (int i = 0; i < options.size(); i++) {
            if (i == currentChoice) {
                font.setColor(Color.DARK_GRAY);
            } else {
                font.setColor(Color.LIGHT_GRAY);
            }
            font.draw(sb, options.get(i), rects[i].x, rects[i].y + rects[i].height / 2);
            
        }
        sb.end();
    }

    private void select(int i) {
        switch (options.get(i)) {
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
            case "Quit":
                Gdx.app.exit();
                break;
        }
    }

    @Override
    public boolean keyReleased(int k) {
        if (k == Input.Keys.ENTER) {
            select(currentChoice);
        }
        if (k == Input.Keys.DOWN) {
            currentChoice = (currentChoice + 1) % options.size();
        }
        if (k == Input.Keys.UP) {
            currentChoice = (currentChoice + options.size() - 1) % options.size();
        }
        return true;
    }

    @Override
    public boolean mouseReleased(int x, int y) {
    	for (int i = 0; i < options.size(); i++) {
    		if(rects[i].contains(x, y)) {
    			select(i);
    			return true;
    		}
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
    	for (int i = 0; i < options.size(); i++) {
    		if(rects[i].contains(x, y)) {
    			currentChoice = i;
    			return true;
    		}
        }
    	return false;
    }

	@Override
	public void onResize() {
		float x = TicTacToe.WIDTH / 2f;
        float y = TicTacToe.HEIGHT / 3f;
        int spacing = 60;
        for (int i = 0; i < options.size(); i++) {
        	rects[i] = new Rectangle(x, y + (i - 0.2f) * spacing, 
        			TicTacToe.WIDTH - x, spacing * 0.6f);
        }
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
	}
}
