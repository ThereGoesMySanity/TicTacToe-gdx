package tgms.ttt.Net;

import tgms.ttt.GameState.BoardState;

public abstract class Connection implements ConnectionKernel {
	private Player user;
	protected Player userTwo;
	BoardState b;
	protected boolean connected = false;

	public Connection(String username) {
		user = new Player(username);
	}

	public void start() {
		if (first()) {
			send(new Message(user.name));
		}
	}

	public boolean accept(Player p) {
		return true;
	}

	public void handleInput() {
		Message m = read();
		if(m != null) {
			if (userTwo == null) {
				if(accept(m.player)) {
					userTwo = m.player;
					if (!first()) {
						send(new Message(user.name));
					}
				}
			} else {
				if(m.x >= 0) {
					b.makeMove(m.x, m.y);
				}
			}
		}
	}

	public int getLocalTurn() {
		return getUser().turn;
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

	public Player getUser() {
		return user;
	}
}