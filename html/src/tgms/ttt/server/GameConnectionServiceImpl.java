package tgms.ttt.server;

import static tgms.ttt.Net.Socket.SocketConnection.DEFAULT_PORT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import tgms.ttt.Net.Message;
import tgms.ttt.Net.Socket.MessageSocket;
import tgms.ttt.client.GameConnectionService;

public class GameConnectionServiceImpl extends RemoteServiceServlet implements GameConnectionService {
	//key: username, value: session id
	private HashMap<String, String> players;
	//reverse of ^
	private HashMap<String, String> names;
	//key and value: session id
	private HashMap<String, String> games;
	//key: session id
	private HashMap<String, Queue<Message>> messages;
	private GameServer server;
	private boolean running = true;
	private static final long serialVersionUID = 1L;

	class GameServer extends Thread {
		private ServerSocket ss;
		private HashMap<String, MessageSocket> sockets;
		public GameServer() throws IOException {
			ss = new ServerSocket(DEFAULT_PORT);
			sockets = new HashMap<>();
		}

		@Override
		public void run() {
			new Thread(new Runnable() {
				public void run() {
					while (running) {
						synchronized (messages) {
							for (Entry<String, MessageSocket> e : sockets.entrySet()) {
								while (e.getValue().available()) {
									Message m = e.getValue().read();
									switch (m.type) {
									case Message.CONNECT:
										connect(m.player.name);
									case Message.GET_USERS:
										e.getValue().writeObject(getUsers());
									case Message.CONNECT_TO_USER:
										connectToUser(m.player.name);
									default:
									}
								}
								while (!messages.get(e.getKey()).isEmpty()) {
									e.getValue().write(messages.get(e.getKey()).poll());
								}
							}
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}, "Socket Check Loop").start();
			while (running) {
				try {
					Socket s = ss.accept();
					try {
						MessageSocket m = new MessageSocket(s);
						sockets.put(s.getRemoteSocketAddress().toString(), m);
					} catch (IOException e) {
						e.printStackTrace();
						sockets.remove(s.getRemoteSocketAddress().toString());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public GameConnectionServiceImpl() {
		players = new HashMap<>();
		names = new HashMap<>();
		games = new HashMap<>();
		messages = new HashMap<>();
		try {
			server = new GameServer();
			server.setName("GameServer Thread");
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSessionId() {
		return getThreadLocalRequest().getSession().getId();
	}

	@Override
	public void connect(String username) {
		System.out.println(username + " connected");
		synchronized (messages) {
			if(!players.containsKey(username)) {
				players.put(username, getSessionId());
				names.put(getSessionId(), username);
				messages.put(getSessionId(), new ArrayDeque<Message>());
			} 
		}
	}

	@Override
	public String[] getUsers() {
		String[] s = players.keySet().toArray(new String[0]);
		System.out.println(Arrays.toString(s));
		return s;
	}

	@Override
	public void connectToUser(String username) {
		games.put(getSessionId(), players.get(username));
		games.put(players.get(username), getSessionId());
	}

	@Override
	public boolean available() {
		return messages.containsKey(getSessionId()) && !messages.get(getSessionId()).isEmpty();
	}

	@Override
	public void send(Message m) {
		synchronized (messages) {
			messages.get(games.get(getSessionId())).add(m);
		}
	}

	@Override
	public Message read() {
		synchronized (messages) {
			return messages.get(getSessionId()).poll();
		}
	}

	@Override
	public boolean first() {
		return getSessionId().compareTo(games.get(getSessionId())) < 0;
	}

	@Override
	public void close() {
		if (names.containsKey(getSessionId())) players.remove(names.remove(getSessionId()));
		if (games.containsKey(getSessionId())) games.remove(games.remove(getSessionId()));
		if (messages.containsKey(getSessionId())) messages.remove(getSessionId());
	}
}
