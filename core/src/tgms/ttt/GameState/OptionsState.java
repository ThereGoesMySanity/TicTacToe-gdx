package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.awt.event.KeyEvent;
import tgms.ttt.Main.GamePanel;

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
	private Font titleFont;
	private Font font;
	private int currentChoice;
	public OptionsState(GameStateManager gsm){
		this.gsm = gsm;
		optionsColor.put("Red", Color.RED);
		optionsColor.put("Blue", Color.BLUE);
		optionsColor.put("Green", Color.GREEN);
		optionsColor.put("Pink", Color.PINK);
		optionsColor.put("Black", Color.BLACK);
		try{
			titleColor = new Color (128, 0, 0);
			titleFont = new Font("Fixedsys", Font.TRUETYPE_FONT, (56));
			font = new Font("Fixedsys", Font.TRUETYPE_FONT, (32));
		}catch(Exception e){e.printStackTrace();}

	}
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, TicTacToe.WIDTH, TicTacToe.HEIGHT);
		g.setColor(titleColor);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
		int x = ((TicTacToe.WIDTH - fm.stringWidth("Options")) / 2);
		g.drawString("Options", x, fm.getAscent()+(60));
		g.setFont(font);
		for(int i = 0; i < options.length; i++){
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.LIGHT_GRAY);
			}
			
			switch(i){
			case 0:
				g.drawString((String) optionsColor.keySet().toArray()[xColor], (400), ((200 + i*60)));
				break;
			case 1:
				g.drawString(xPic, (400), ((200 + i*60)));
				break;
			case 2:
				g.drawString((String) optionsColor.keySet().toArray()[oColor], (400), ((200 + i*60)));
				break;
			case 3:
				g.drawString(oPic, (400), ((200 + i*60)));
				break;
			case 4:
				g.drawString((String) optionsColor.keySet().toArray()[boardColor], (400), ((200 + i*60)));
				break;

			}
			g.drawString(options[i], (150), ((200 + i*60)));
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
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select(currentChoice);
		}
		if(k==KeyEvent.VK_DOWN){
			currentChoice = (currentChoice+1)%options.length;
		}
		if(k==KeyEvent.VK_UP){
			currentChoice = (currentChoice+options.length-1)%options.length;
		}
	}
	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(Point click) {
		// TODO Auto-generated method stub
		if(click.y >= (160) && click.y <= ((160 + options.length*60))&&click.x>=(150)){
			select((click.y-160)/60);
		}
	}
	@Override
	public void mouseClicked(Point click) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getY() >= (160) && e.getY() <= (160 + options.length*60)&&e.getX()>=(150)){
			currentChoice = ((e.getY()-(160))/(60));
		}
	}
}
