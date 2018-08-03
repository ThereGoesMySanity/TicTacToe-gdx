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
import java.util.concurrent.ConcurrentHashMap;

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
	private Thread serverThread, socketThread;
	private boolean running = true;
	private static final long serialVersionUID = 1L;

	class SocketThread extends Thread {
		private ConcurrentHashMap<String, MessageSocket> sockets;
		public SocketThread(ConcurrentHashMap<String, MessageSocket> s) {
			sockets = s;
		}

		@Override
		public void run() {
			while (running) {
				for (Entry<String, MessageSocket> e : sockets.entrySet()) {
					System.out.println(e.getKey());
					while (e.getValue().available()) {
						Message m = e.getValue().read();
						switch (m.type) {
						case Message.CONNECT:
							connect(e.getKey(), m.player.name);
						case Message.GET_USERS:
							e.getValue().writeObject(getUsers());
						case Message.CONNECT_TO_USER:
							connectToUser(e.getKey(), m.player.name);
						default:
							send(e.getKey(), m);
						}
					}
					while (messages.containsKey(e.getKey()) 
							&& !messages.get(e.getKey()).isEmpty()) {
						e.getValue().write(read(e.getKey()));
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

	class GameServer extends Thread {
		private ServerSocket ss;
		private ConcurrentHashMap<String, MessageSocket> sockets;
		public GameServer() throws IOException {
			ss = new ServerSocket(DEFAULT_PORT);
			sockets = new ConcurrentHashMap<>();
			socketThread = new SocketThread(sockets);
		}

		@Override
		public void run() {
			socketThread.start();
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
			serverThread = new GameServer();
			serverThread.setName("GameServer Thread");
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSessionId() {
		return getThreadLocalRequest().getSession().getId();
	}

	@Override
	public void connect(String username) {
		connect(getSessionId(), username);
	}

	public synchronized void connect(String id, String username) {
		System.out.println(username + " connected");
		if(!players.containsKey(username)) {
			players.put(username, id);
			names.put(id, username);
			messages.put(id, new ArrayDeque<Message>());
		} 
	}

	@Override
	public synchronized String[] getUsers() {
		String[] s = players.keySet().toArray(new String[0]);
		System.out.println(Arrays.toString(s));
		return s;
	}

	@Override
	public void connectToUser(String username) {
		connectToUser(getSessionId(), username);
	}

	public synchronized void connectToUser(String id, String username) {
		games.put(id, players.get(username));
		games.put(players.get(username), id);
	}

	@Override
	public boolean available() {
		return available(getSessionId());
	}

	public synchronized boolean available(String id) {
		return messages.containsKey(id) && !messages.get(getSessionId()).isEmpty();
	}

	@Override
	public void send(Message m) {
		send(getSessionId(), m);
	}

	public synchronized void send(String id, Message m) {
		messages.get(games.get(id)).add(m);
	}

	@Override
	public Message read() {
		return read(getSessionId());
	}

	public synchronized Message read(String id) {
		return messages.get(id).poll();
	}

	@Override
	public boolean first() {
		return first(getSessionId());
	}

	public synchronized boolean first(String id) {
		return id.compareTo(games.get(id)) < 0;
	}

	@Override
	public synchronized void close() {
		if (names.containsKey(getSessionId())) players.remove(names.remove(getSessionId()));
		if (games.containsKey(getSessionId())) games.remove(games.remove(getSessionId()));
		if (messages.containsKey(getSessionId())) messages.remove(getSessionId());
	}
}
