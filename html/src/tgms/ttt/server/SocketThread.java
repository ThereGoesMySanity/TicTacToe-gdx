package tgms.ttt.server;

import tgms.ttt.Net.Message;
import tgms.ttt.Net.Socket.MessageSocket;

public class SocketThread {
	private boolean running = true;
	private String id;
	private MessageSocket mSocket;
	private GameConnectionServiceImpl servlet;
	public SocketThread(GameConnectionServiceImpl s, String id, MessageSocket ms) {
		servlet = s;
		this.id = id;
		mSocket = ms;
	}

	public void start() {
		new Thread(() -> {
			while (running) {
				Message m = mSocket.read();
				System.out.println(id);
				switch (m.type) {
				case Message.CONNECT:
					servlet.connect(id, m.player.name);
					break;
				case Message.GET_USERS:
					mSocket.writeObject(servlet.getUsers());
					break;
				case Message.CONNECT_TO_USER:
					servlet.connectToUser(id, m.player.name);
					break;
				case Message.DISCONNECT:
					servlet.close(id);
					break;
				default:
					servlet.send(id, m);
				}
			}
		}).start();
		new Thread(() -> {
			while (running) {
				while (servlet.available(id)) {
					mSocket.write(servlet.read(id));
				}
			}
		}).start();
	}
	public void end() {
		running = false;
	}
}
