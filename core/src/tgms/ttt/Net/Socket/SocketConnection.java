package tgms.ttt.Net.Socket;

import com.badlogic.gdx.net.Socket;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Message;

public abstract class SocketConnection extends Connection {
	protected MessageSocket ms;
	private Socket sock;
	public static int DEFAULT_PORT = 5435;

	public SocketConnection(String username) {
		super(username);
	}

	protected void init(Socket s) {
		ms = new MessageSocket(s);
		connected = true;
		sock = s;
	}

	@Override
	public boolean available() {
		return ms.available();
	}

	@Override
	public void send(Message m) {
		ms.write(m);
	}

	@Override
	public Message read() {
		return ms.read();
	}

	@Override
	public void close() {
		sock.dispose();
		connected = false;
	}
}
