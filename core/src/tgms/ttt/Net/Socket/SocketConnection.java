package tgms.ttt.Net.Socket;

import java.io.IOException;
import java.net.Socket;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Message;

public abstract class SocketConnection extends Connection {
	protected MessageSocket ms;
	protected Socket sock;
	public static int DEFAULT_PORT = 5435;

	public SocketConnection(String username) {
		super(username);
	}

	protected void init(Socket s) {
		try {
			ms = new MessageSocket(s.getInputStream(), s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connected = false;
	}
}
