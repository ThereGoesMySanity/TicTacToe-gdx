package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.awt.event.KeyEvent;
import tgms.ttt.TicTacToe;

public class OptionsState extends GameState {
	private String[] options = {
			"X color",
			"X image",
			"O color",
			"O image",
			"Board color",
			"Back"
	};
	private LinkedHashMap<String, Color> optionsColor = new LinkedHashMap<String, Color>();
	private int xColor = 0;
	private int oColor = 1;
	private String xPic = "None";
	private String oPic = "None";
	private BufferedImage xImage;
	private BufferedImage oImage;
	private int boardColor = 4;
	private Color titleColor;
	private BitmapFont font;
	private int currentChoice;
	public OptionsState(GameStateManager gsm){
		this.gsm = gsm;
		optionsColor.put("Red", Color.RED);
		optionsColor.put("Blue", Color.BLUE);
		optionsColor.put("Green", Color.GREEN);
		optionsColor.put("Pink", Color.PINK);
		optionsColor.put("Black", Color.BLACK);
		try {
			titleColor = new Color (0x800000FF);
			//TODO: declare fonts
		} catch(Exception e) { e.printStackTrace(); }

	}
	@Override
	public void init() {
	}

	@Override
	public void update() {
	}

	@Override
	public void draw(ShapeRenderer g, SpriteBatch sb) {
		g.setColor(Color.WHITE);
		g.set(ShapeRenderer.ShapeType.Filled);
		g.rect(0, 0, TicTacToe.WIDTH, TicTacToe.HEIGHT);
		sb.setColor(titleColor);
        GlyphLayout opt = new GlyphLayout(font, "Options");
		float x = (TicTacToe.WIDTH - opt.width) / 2;
		font.draw(sb, opt, x, opt.height+60);
		for(int i = 0; i < options.length; i++){
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.LIGHT_GRAY);
			}
			
			switch(i){
			case 0:
				font.draw(sb, (String) optionsColor.keySet().toArray()[xColor], (400), ((200 + i*60)));
				break;
			case 1:
				font.draw(sb, xPic, (400), ((200 + i*60)));
				break;
			case 2:
				font.draw(sb, (String) optionsColor.keySet().toArray()[oColor], (400), ((200 + i*60)));
				break;
			case 3:
				font.draw(sb, oPic, (400), ((200 + i*60)));
				break;
			case 4:
				font.draw(sb, (String) optionsColor.keySet().toArray()[boardColor], (400), ((200 + i*60)));
				break;

			}
			font.draw(sb, options[i], (150), ((200 + i*60)));
		}

	}
	private void select(int choice){
		switch(choice){
		case 0://xColor
			xColor++;
			xColor %=5;
			gsm.xColor = optionsColor.get(optionsColor.keySet().toArray()[xColor]);
			break;
		/*
		case 1://xImage
			try {
				File file = Game.panel.getImage();
				if(file == null)break;
				xImage = ImageIO.read(file);
				xPic = file.getName();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gsm.xImage = xImage;
			break;
		*/
		case 2://oColor
			oColor++;
			oColor %=5;
			gsm.oColor = optionsColor.get(optionsColor.keySet().toArray()[oColor]);
			break;
		/*
		case 3://oImage
			try {
				File file = Game.panel.getImage();
				if(file == null)break;
				oImage = ImageIO.read(file);
				oPic = file.getName();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gsm.oImage = oImage;
			break;
		*/
		case 4://boardColor
			boardColor++;
			boardColor %=5;
			gsm.boardColor = optionsColor.get(optionsColor.keySet().toArray()[boardColor]);
			break;
		case 5:
			gsm.setState(GameStateManager.MENUSTATE);
			break;
		}
	}

	@Override
	public boolean keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select(currentChoice);
			return true;
		}
		if(k==KeyEvent.VK_DOWN){
			currentChoice = (currentChoice+1)%options.length;
            return true;
		}
		if(k==KeyEvent.VK_UP){
			currentChoice = (currentChoice+options.length-1)%options.length;
            return true;
		}
        return false;
	}

	@Override
	public boolean keyReleased(int k) {
	    return false;
	}

	@Override
	public boolean mouseReleased(int x, int y) {
		if(y >= (160) && y <= ((160 + options.length*60)) && x>=(150)){
			select((y-160)/60);
			return true;
		}
		return false;
	}
	@Override
	public boolean mouseMoved(int x, int y) {
		if(y >= (160) && y <= (160 + options.length*60)&&x>=(150)){
			currentChoice = ((x-(160))/(60));
			return true;
		}
		return false;
	}
}
