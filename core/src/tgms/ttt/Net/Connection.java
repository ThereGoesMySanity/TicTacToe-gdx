package tgms.ttt.Net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import tgms.ttt.GameState.BoardState;

public abstract class Connection extends Thread {
	public static int DEFAULT_PORT = 5435;
	DataInputStream in;
	DataOutputStream out;
	String user, userTwo;
	BoardState b;
	int playerNum;
	boolean connected = false;

	public static Connection createConnection() throws IOException {
		String[] values = {"Host", "Connect"};
		String value = (String)JOptionPane.showInputDialog(null, 
				"Host server or connect to server?", 
				"Connection", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
		String player = JOptionPane.showInputDialog("Enter name:");
		if (value.equals("Host")) {
			return new Server(player, DEFAULT_PORT);
		} else if (value.equals("Connect")) {
			String ipaddr = JOptionPane.showInputDialog("Enter IP to connect to:");
			return new Client(player, ipaddr, DEFAULT_PORT);
		}
		return null;
	}

	public String getPlayerName() {
		return userTwo;
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int num) {
		this.playerNum = num;
	}

	public Connection(String username)
			throws IOException {
		user = username;
	}

	public boolean connected() {
		return connected;
	}

	public void init(Socket s) throws IOException {
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
	}

	public void run(Socket s) throws IOException, InterruptedException {
		out.writeUTF("user:" + user);
		while (true) {
			if (in.available() > 0) {
				getInput();
			}
			sleep(100);
		}
	}

	public void setBoardState(BoardState b) {
		this.b = b;
	}

	public void makeMove(int x, int y) {
		if (playerNum == b.getTurn()) {
			try {
				out.writeUTF("move:" + x + "," + y);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void getInput() throws IOException {
		String s = "";
		while (in.available() > 0) {
			s += in.readUTF();
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
