package tgms.ttt.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayDeque;
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
	private static final long serialVersionUID = 1L;


	class GameServer extends ServerSocket implements Runnable {
		private HashMap<String, MessageSocket> sockets;
		public GameServer(int port) throws IOException {
			super(port);
		}

		@Override
		public void run() {
			synchronized (messages) {
				for (Entry<String, MessageSocket> e : sockets.entrySet()) {
					while (!messages.get(e.getKey()).isEmpty()) {
						e.getValue().write(messages.get(e.getKey()).poll());
					}
					while (e.getValue().available()) {
						messages.get(e.getKey()).add(e.getValue().read());
					}
				}
			}
		}
	}

	public GameConnectionServiceImpl() {
		players = new HashMap<>();
		names = new HashMap<>();
		games = new HashMap<>();
		messages = new HashMap<>();
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
		return players.keySet().toArray(new String[0]);
	}

	@Override
	public void connectToUser(String username) {
		games.put(getSessionId(), players.get(username));
		games.put(players.get(username), getSessionId());
	}

	@Override
	public boolean available() {
		return !messages.get(getSessionId()).isEmpty();
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
