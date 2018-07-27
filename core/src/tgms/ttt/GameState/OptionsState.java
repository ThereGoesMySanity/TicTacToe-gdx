package tgms.ttt.GameState;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import tgms.ttt.TicTacToe;
import tgms.ttt.GameState.GameStateManager.State;

public class OptionsState extends GameState {
    private ArrayList<String> options;
    private Rectangle[] rects;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.BLACK };
    private String[] colorNames = { "Red", "Blue", "Green", "Pink", "Black" };
    private int xColor = 0;
    private int oColor = 1;

    private int boardColor = 4;

    private Color titleColor;
    private int currentChoice;
    OptionsState(GameStateManager gsm){
        super(gsm);
        options = new ArrayList<>();
        options.add("X color");
        options.add("O color");
        if(gsm.platform().os != null) {
	        options.add("X image");
	        options.add("O image");
        }
        options.add("Board color");
        options.add("Back");
        rects = new Rectangle[options.size()];
        for (int i = 0; i < options.size(); i++) {
        	rects[i] = new Rectangle(i, i, i, i);
        }
        titleColor = new Color (0x800000FF);
    }

    @Override
    public void draw(ShapeRenderer s, SpriteBatch sb) {
    	sb.begin();
        font.setColor(titleColor);
        GlyphLayout opt = new GlyphLayout(font, "Options");
        float x = (TicTacToe.WIDTH - opt.width) / 2;
        font.draw(sb, opt, x, opt.height+60);
        for(int i = 0; i < options.size(); i++){
            if(i==currentChoice){
                font.setColor(Color.DARK_GRAY);
            }else{
                font.setColor(Color.LIGHT_GRAY);
            }

            switch(i){
            case 0:
                font.draw(sb, colorNames[xColor], (400), ((200 + i*60)));
                break;
            case 1:
                //font.draw(sb, xPic, (400), ((200 + i*60)));
                break;
            case 2:
                font.draw(sb, (String) colorNames[oColor], (400), ((200 + i*60)));
                break;
            case 3:
                //font.draw(sb, oPic, (400), ((200 + i*60)));
                break;
            case 4:
                font.draw(sb, (String) colorNames[boardColor], (400), ((200 + i*60)));
                break;

            }
            font.draw(sb, options.get(i), (150), ((200 + i*60)));
        }
        sb.end();
    }
    private void select(int choice){
    	Texture tex;
        switch(choice){
        case 0://xColor
            xColor++;
            if (xColor == oColor) xColor++;
            xColor %=5;
            gsm.xColor = colors[xColor];
            break;
        case 1://xImage
            tex = gsm.platform().os.getImage();
            if(tex == null) break;
            gsm.xImage = tex;
            break;
        case 2://oColor
            oColor++;
            if (xColor == oColor) oColor++;
            oColor %=5;
            gsm.oColor = colors[oColor];
            break;
        case 3://oImage
        	tex = gsm.platform().os.getImage();
            if(tex == null) break;
            gsm.oImage = tex;
            break;
        case 4://boardColor
            boardColor++;
            boardColor %=5;
            gsm.boardColor = colors[boardColor];
            break;
        case 5:
            gsm.setState(State.MENUSTATE);
            break;
        }
    }

    @Override
    public boolean keyReleased(int k) {
        if(k == Input.Keys.ENTER){
            select(currentChoice);
        }
        if(k == Input.Keys.DOWN){
            currentChoice = (currentChoice+1)%options.size();
        }
        if(k == Input.Keys.UP){
            currentChoice = (currentChoice+options.size()-1)%options.size();
        }
        return true;
    }

    @Override
    public boolean mouseReleased(int x, int y) {
        if(y >= (160) && y <= ((160 + options.size()*60)) && x>=(150)){
            select((y-160)/60);
            return true;
        }
        return false;
    }
    @Override
    public boolean mouseMoved(int x, int y) {
        if(y >= (160) && y <= (160 + options.size()*60)&&x>=(150)){
            currentChoice = ((x-(160))/(60));
            return true;
        }
        return false;
    }

	@Override
	public void onResize() {
		// TODO Auto-generated method stub
	}
}
