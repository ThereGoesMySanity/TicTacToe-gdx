package tgms.ttt.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Message;

public class GameConnectionServiceConnection extends Connection {
	private GameConnectionServiceAsync conn;
	boolean available, first;
	Message message;
	Set<String> users;
	
	public GameConnectionServiceConnection(String username) {
		super(username);
		conn.connect(user.name, null);
	}
	
	public Set<String> getUsers() {
		conn.getUsers(new AsyncCallback<Set<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(Set<String> result) {
				users = result;
			}
		});
		return users;
	}

	public void setUser(String name) {
		userTwo.name = name;
	}

	@Override
	public boolean available() {
		conn.available(new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				available = result;
			}
			@Override
			public void onFailure(Throwable caught) {
				available = false;
				caught.printStackTrace();
			}
		});
		return available;
	}

	@Override
	public void send(Message m) {
		conn.send(m, null);
	}

	@Override
	public Message read() {
		conn.read(new AsyncCallback<Message>() {
			@Override
			public void onSuccess(Message result) {
				message = result;
			}
			@Override
			public void onFailure(Throwable caught) {
				message = null;
				caught.printStackTrace();
			}
		});
		return message; 
	}

	@Override
	public void start() {
		conn.connectToUser(userTwo.name, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				connected = true;
			}
			@Override
			public void onFailure(Throwable caught) {
				connected = false;
				caught.printStackTrace();
			}
		});
	}

	@Override
	public boolean first() {
		conn.first(new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				first = result;
			}
			@Override
			public void onFailure(Throwable caught) {
				first = false;
				caught.printStackTrace();
			}
		});
		return first;
	}
}
