package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.ConnectionThread;
import tgms.ttt.PlatformInterfaces.Platform;
import tgms.ttt.TicTacToe;

public class NetBoardState extends BoardState {
	private Connection conn;

	NetBoardState(GameStateManager gsm, Connection c) {
		super(gsm, 3, 3);
		conn = c;
		c.setBoardState(this);
		conn.start();
		if (gsm.platform().isSupported(Platform.Features.THREAD)) {
			new ConnectionThread(conn).start();
		}
	}

	@Override
	public void update() {
		if(!gsm.platform().isSupported(Platform.Features.THREAD)) {
			conn.handleInput();
		}
		super.update();
	}
	
	@Override
	public boolean mouseReleased(int x, int y) {
		return conn.connected()
				&& getTurn() == conn.getPlayerNum()
                && super.mouseReleased(x, y);
	}
	
	@Override
	public void makeMove(int x, int y) {
		if(conn.connected() && getTurn() == conn.getPlayerNum()) {
			conn.makeMove(x, y);
		}
		super.makeMove(x, y);
	}
	@Override
	public void draw(ShapeRenderer s, SpriteBatch sb) {
		super.draw(s, sb);
		if(!conn.connected()) {
			font.draw(sb, "Connecting...", TicTacToe.WIDTH/2, TicTacToe.HEIGHT/2);
		} else {
			if(getTurn() == conn.getPlayerNum()) {
				font.draw(sb, "Your turn", 0, 50);
			} else {
				font.draw(sb, conn.getPlayerName()+"'s turn", 0, 50);
			}
		}
	}
	
}
