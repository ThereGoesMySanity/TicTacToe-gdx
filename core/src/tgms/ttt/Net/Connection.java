package tgms.ttt.Net;

import com.badlogic.gdx.math.GridPoint2;

import tgms.ttt.GameState.BoardState;

public abstract class Connection implements ConnectionKernel {
	protected Player user, userTwo;
	BoardState b;
	protected boolean connected = false;

	public Connection(String username) {
		user = new Player(username);
	}

	public abstract void start();

	public boolean accept(Player p) {
		return true;
	}

	public void handleInput() {
		while(available()) {
			Message m = read();
			if(m != null) {
				if (userTwo == null) {
					if(accept(m.player)) {
						userTwo = m.player;
					}
				} else {
					if(m.move != null) {
						makeMove(m.move);
					}
				}
			}
		}
	}

	public void makeMove(int x, int y) {
		makeMove(new GridPoint2(x, y));
	}

	public void makeMove(GridPoint2 p) {
		send(new Message(null, p));
	}

	public int getLocalTurn() {
		return user.turn;
	}

	public String getPlayerName() {
		return userTwo.name;
	}

	public boolean connected() {
		return connected;
	}

	public void setBoardState(BoardState b) {
		this.b = b;
	}
}
