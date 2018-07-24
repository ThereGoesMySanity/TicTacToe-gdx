package tgms.ttt.Net;

import java.io.IOException;

import tgms.ttt.GameState.BoardState;

public abstract class Connection implements ConnectionBase {
	public static int DEFAULT_PORT = 5435;
	String user, userTwo;
	BoardState b;
	protected int playerNum;
	protected boolean connected = false;

	public String getPlayerName() {
		return userTwo;
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int num) {
		this.playerNum = num;
	}

	public Connection(String username) {
		user = username;
	}

	public boolean connected() {
		return connected;
	}

	public void setBoardState(BoardState b) {
		this.b = b;
	}

	public void makeMove(int x, int y) {
		if (playerNum == b.getTurn()) {
            send("move:" + x + "," + y);
		}
	}

	@Override
	public void getInput(){
		String s = "";
		while (available() > 0) {
			s += read();
		}
		getInput2(s);
		if (s.startsWith("user:")) {
			userTwo = s.substring(5);
		}
		if (s.startsWith("move:")) {
			String[] m = s.substring(5).split(",");
			b.makeMove(Integer.parseInt(m[0]),
						Integer.parseInt(m[1]));
		}
	}
	public abstract void getInput2(String s);
}
