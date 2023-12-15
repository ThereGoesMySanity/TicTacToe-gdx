package tgms.ttt.GameState;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import tgms.ttt.TicTacToe;
import tgms.ttt.Net.Connection;
import tgms.ttt.Net.ConnectionThread;
import tgms.ttt.Net.Message;

public class NetBoardState extends BoardState {
	private Connection conn;
	private boolean drawn;

	NetBoardState(GameStateManager gsm)  {
		super(gsm, 3, 3);
		conn = gsm.platform().online.getConnection();
		conn.setBoardState(this);
		drawn = false;
	}

	@Override
	public void update() {
		if(drawn && !conn.connected()) {
			if (gsm.platform().thread != null) {
				gsm.platform().thread.start(new ConnectionThread(conn));
			} else {
			    conn.start();
			}
			gsm.platform().online.connectToUser(conn);
		}
		if(gsm.platform().thread == null) {
			conn.handleInput();
		}
		super.update();
	}
	
	@Override
	public boolean mouseReleased(int x, int y) {
		return conn.connected()
				&& board.getTurn() == conn.getLocalTurn()
                && super.mouseReleased(x, y);
	}
	
	public void receiveMove(int x, int y) {
		super.makeMove(x, y);
	}
	
	@Override
	public void makeMove(int x, int y) {
		if(conn.connected() && board.getTurn() == conn.getLocalTurn()) {
			conn.send(new Message(x, y));
			super.makeMove(x, y);
		}
	}

	@Override
	public void draw(ShapeRenderer s, SpriteBatch sb) {
		super.draw(s, sb);
		drawn = true;
		sb.begin();
		if(!conn.connected()) {
			font.draw(sb, "Connecting...", TicTacToe.WIDTH/2, TicTacToe.HEIGHT/2);
		} else {
			if(board.getTurn() == conn.getLocalTurn()) {
				font.draw(sb, "Your turn", 0, 50);
			} else {
				font.draw(sb, conn.getPlayerName()+"'s turn", 0, 50);
			}
		}
		sb.end();
	}
	
	@Override
	public void dispose() {
		conn.close();
	}
}
