package tgms.ttt.server;

import static tgms.ttt.Net.Socket.SocketConnection.DEFAULT_PORT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
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
	private Thread serverThread;
	private boolean running = true;
	private static final long serialVersionUID = 1L;


	class GameServer extends Thread {
		private ServerSocket ss;
		private GameConnectionServiceImpl s;
		public GameServer(GameConnectionServiceImpl s) throws IOException {
			this.s = s;
			ss = new ServerSocket(DEFAULT_PORT);
		}

		@Override
		public void run() {
			while (running) {
				try {
					Socket s = ss.accept();
					MessageSocket m = new MessageSocket(s);
					new SocketThread(this.s, s.getRemoteSocketAddress().toString(), m).start();
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
			serverThread = new GameServer(this);
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
		Message m = new Message(names.get(id));
		m.type = Message.CONNECT_TO_USER;
		send(players.get(username), m);
	}

	public synchronized boolean available(String id) {
		return messages.containsKey(id) && !messages.get(id).isEmpty();
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
		if(messages == null || !available(id)) return null;
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
		close(getSessionId());
	}

	public synchronized void close(String id) {
		if (names.containsKey(id)) players.remove(names.remove(id));
		if (games.containsKey(id)) games.remove(games.remove(id));
		if (messages.containsKey(id)) messages.remove(id);
	}
}
