package tgms.ttt.Net.Socket;

import java.io.IOException;
import java.net.Socket;

import tgms.ttt.Net.Message;

public class GameServerSocket extends SocketClient implements GameServerKernel {
	private static final String SERVER = "127.0.0.1";

	public GameServerSocket(String username, int port) {
		super(username, SERVER, port);
		try {
			ms = new MessageSocket(sock);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connect(username);
	}
	
	@Override
	public String[] getUsers() {
		send(new Message(Message.GET_USERS));
		return (String[]) ms.readObject();
	}
	
	@Override
	public void connect(String user) {
		Message m = new Message(user);
		m.type = Message.CONNECT;
		send(m);
	}
	
	@Override
	public void connectToUser(String user) {
		Message m = new Message(user);
		m.type = Message.CONNECT_TO_USER;
		send(m);
	}

	@Override
	protected void init(Socket s) {
	}
}
