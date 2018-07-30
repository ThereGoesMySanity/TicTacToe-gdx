package tgms.ttt.server;

import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import tgms.ttt.Net.Message;
import tgms.ttt.client.GameConnectionService;

public class GameConnectionServiceImpl extends RemoteServiceServlet implements GameConnectionService {
	//key: username, value: session id
	public HashMap<String, String> players;
	//key and value: session id
	public HashMap<String, String> games;
	//key: session id
	public HashMap<String, Message> messages;
	private static final long serialVersionUID = 1L;
	
	public GameConnectionServiceImpl() {
		players = new HashMap<>();
		games = new HashMap<>();
		messages = new HashMap<>();
	}
	
	private String getSessionId() {
		return getThreadLocalRequest().getSession().getId();
	}


	@Override
	public void connect(String username) {
		System.out.println(username + " connected");
		if(!players.containsKey(username)) {
			players.put(username, getSessionId());
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
		return messages.containsKey(getSessionId());
	}

	@Override
	public void send(Message m) {
		messages.put(games.get(getSessionId()), m);
	}

	@Override
	public Message read() {
		return messages.remove(getSessionId());
	}

	@Override
	public boolean first() {
		return getSessionId().compareTo(games.get(getSessionId())) < 0;
	}
}
