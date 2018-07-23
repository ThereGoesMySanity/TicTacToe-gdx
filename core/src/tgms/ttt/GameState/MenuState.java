package tgms.ttt.GameState;
import com.badlogic.gdx.graphics.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import tgms.ttt.Main.GamePanel;

public class MenuState extends GameState{

	private int currentChoice = 0;
	private String[] options = {
		"Start Local",
		"Start Online",
		"Options",
		"Help",
		"Quit"
	};
	private Color titleColor;
	private Font titleFont;
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		try {
			titleColor = new Color (128, 0, 0);
			titleFont = new Font("Fixedsys", Font.TRUETYPE_FONT, 56);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void init(){}
	public void draw(Graphics2D g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, TicTacToe.WIDTH, TicTacToe.HEIGHT);
		g.setColor(titleColor);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
		int x = ((TicTacToe.WIDTH - fm.stringWidth("Tic-Tac-Toe")) / 2);
		int y = fm.getHeight();
		g.drawString("Tic-Tac-Toe", x, y+32);
		g.setFont(titleFont);
		for(int i = 0; i < options.length; i++){
			if(i==currentChoice){
				g.setColor(Color.DARK_GRAY);
			}else{
				g.setColor(Color.LIGHT_GRAY);
			}
			g.drawString(options[i], getMenuX(), getMenuY() + (i * getMenuSpacing()));
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
			JOptionPane.showConfirmDialog(null, "It's tic tac toe that expands infinitely.\nNo explanation needed.", "You don't need help.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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
	
	public void keyPressed(int k){
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
	public void keyReleased(int k){}
	@Override
	public void mouseReleased(Point click) {
		// TODO Auto-generated method stub
		if(click.y >= getMenuY() - getMenuSpacing() && click.y <= getMenuY() + options.length * getMenuSpacing()
				&& click.x >= getMenuX()){
			select(currentChoice);
		}
	}
	@Override
	public void mouseClicked(Point click) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getY() >= getMenuY() - getMenuSpacing() && e.getY() <= getMenuY() + options.length * getMenuSpacing()
				&& e.getX() >= getMenuX()){
			currentChoice = (e.getY() - getMenuY() + getMenuSpacing()) / getMenuSpacing();
		}
	}
}
