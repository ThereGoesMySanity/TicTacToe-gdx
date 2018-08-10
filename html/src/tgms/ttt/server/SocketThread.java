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
		Thread read = new Thread(() -> {
			while (running) {
				Message m = mSocket.read();
				System.out.println(id);
				if (m != null) {
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
						end();
						break;
					default:
						servlet.send(id, m);
					}
				} else {
					end();
				}
			}
		});
		read.start();
		Thread write = new Thread(() -> {
			while (running) {
				while (servlet.available(id)) {
					mSocket.write(servlet.read(id));
				}
			}
		});
		write.start();
	}
	public void end() {
		servlet.close(id);
		running = false;
	}
}
