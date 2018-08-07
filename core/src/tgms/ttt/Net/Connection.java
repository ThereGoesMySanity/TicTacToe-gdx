package tgms.ttt.Net;

import java.util.HashSet;

import tgms.ttt.GameState.BoardState;
import tgms.ttt.PlatformInterfaces.Interruptible;

public abstract class Connection implements ConnectionKernel {
	private Player user;
	protected Player userTwo;
	BoardState b;
	protected boolean connected = false;
	private HashSet<Interruptible> interrupts;

	public Connection(String username) {
		user = new Player(username);
		interrupts = new HashSet<>();
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
			if (m.type != 0) {
				for (Interruptible i : interrupts) i.interrupt(m.type);
			}
			if (userTwo == null && m.player != null) {
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

	public void addInterrupt(Interruptible i) {
		interrupts.add(i);
	}

	@Override
	public void close() {
		send(new Message(Message.DISCONNECT));
	}
}