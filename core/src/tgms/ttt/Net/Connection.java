package tgms.ttt.Net;

import java.util.HashSet;

import com.badlogic.gdx.Gdx;

import tgms.ttt.GameState.NetBoardState;
import tgms.ttt.PlatformInterfaces.Interruptible;

public abstract class Connection implements ConnectionKernel {
	private Player user;
	protected Player userTwo;
	NetBoardState b;
	protected boolean connected = false;
	private HashSet<Interruptible> interrupts;

	public Connection(String username) {
		user = new Player(username);
		interrupts = new HashSet<>();
	}
	
	public abstract void start();

	public boolean accept(Player p) {
		return true;
	}

	public void handleInput() {
		if (connected && userTwo == null && first()) {
			send(new Message(user.name));
		}
		Message m = read();
		if(m != null) {
			if (m.type != 0) {
				Gdx.app.debug("help", "Interrupt "+m.type);
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
					b.receiveMove(m.x, m.y);
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

	public void setBoardState(NetBoardState b) {
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
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}