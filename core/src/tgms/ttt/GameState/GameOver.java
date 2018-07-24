package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import tgms.ttt.Main.GamePanel;

public class GameOver extends GameState {
	
	public GameOver(GameStateManager gsm){
		this.gsm = gsm;
	}
	public void init() {
		
	}

	public void update() {}

	public void draw(Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
        int x = ((TicTacToe.WIDTH - fm.stringWidth("Game Over")) / 2);
        int x2 = ((TicTacToe.WIDTH - fm.stringWidth(gsm.WIN + " Wins")) / 2);
        g.setColor(Color.GREEN);
		g.drawString("Game Over", x, 32);
		g.drawString(gsm.WIN + " Wins", x2, 128);
	}

	public void keyPressed(int k) {
		System.out.println(k + " " + KeyEvent.VK_ENTER);
		if (k == KeyEvent.VK_ENTER){
			select();
		}
	}

	public void keyReleased(int k) {}
	public void select(){
		gsm.setState(GameStateManager.BOARDSTATE);
	}
	public void mouseClicked(GridPoint2 click) {
		
	}
	@Override
	public void mouseReleased(GridPoint2 click) {}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
