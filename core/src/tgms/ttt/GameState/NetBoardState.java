package tgms.ttt.GameState;
import java.awt.Font;
import java.awt.Graphics2D;

import tgms.ttt.Main.GamePanel;
import tgms.ttt.desktop.Net.Connection;

public class NetBoardState extends BoardState {
	private Connection conn;

	public NetBoardState(GameStateManager gsm, Connection c) {
		super(gsm, 3, 3);
		conn = c;
		c.setBoardState(this);
		conn.start();
	}
	
	@Override
	public void mouseReleased(GridPoint2 p) {
		if(conn.connected() && getTurn() == conn.getPlayerNum()) {
			super.mouseReleased(p);
		}
	}
	
	@Override
	public void makeMove(int x, int y) {
		if(conn.connected() && getTurn() == conn.getPlayerNum()) {
			conn.makeMove(x, y);
		}
		super.makeMove(x, y);
	}
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if(!conn.connected()) {
			g.setFont(new Font("", Font.TRUETYPE_FONT, 56));
			g.drawString("Connecting...", TicTacToe.WIDTH/2, TicTacToe.HEIGHT/2);
		} else {
			g.setFont(new Font("", Font.TRUETYPE_FONT, 24));
			if(getTurn() == conn.getPlayerNum()) {
				g.drawString("Your turn", 0, 50);
			} else {
				g.drawString(conn.getPlayerName()+"'s turn", 0, 50);
			}
		}
	}
	
}
