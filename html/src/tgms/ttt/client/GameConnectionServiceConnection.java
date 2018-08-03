package tgms.ttt.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Message;

public class GameConnectionServiceConnection extends Connection {
	private GameConnectionServiceAsync conn;
	private boolean first;
	private Message read;

	public GameConnectionServiceConnection(String username) {
		super(username);
		conn = GWT.create(GameConnectionService.class);
		conn.connect(getUser().name, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable arg0) {
				arg0.printStackTrace();
			}
			@Override
			public void onSuccess(Void arg0) {
				conn.getUsers(new GetUsersAsync());
			}
		});
	}

	class GetUsersAsync implements AsyncCallback<String[]> {
		@Override
		public void onSuccess(String[] result) {
			StringBuilder prompt = new StringBuilder("Select a user to connect to:\n");
			String s = "";
			while (s == null || s.isEmpty() || s.equals("refresh")) {
				for(String u : result) {
					if (!u.equals(getUser().name)) {
						prompt.append(u);
						prompt.append('\n');
					}
				}
				s = Window.prompt(prompt.toString(), "");
			}
			if (s != null && !s.isEmpty()) {
				userTwo.name = s;
				conn.connectToUser(s, new StartAsync());
			}
		}
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}
	}

	class StartAsync implements AsyncCallback<Void> {
		@Override
		public void onSuccess(Void result) {
			conn.first(new FirstAsync());
		}
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}
	}

	class FirstAsync implements AsyncCallback<Boolean> {
		@Override
		public void onSuccess(Boolean result) {
			first = result;
			connected = true;
		}
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}
	}

	@Override
	public void start() {
	}

	@Override
	public boolean first() {
		return first;
	}

	@Override
	public boolean available() {
		conn.available(new AvailableAsync());
		return read != null;
	}
	class AvailableAsync implements AsyncCallback<Boolean> {
		@Override
		public void onFailure(Throwable arg0) {
			arg0.printStackTrace();
		}
		@Override
		public void onSuccess(Boolean result) {
			if (result) {
				conn.read(new ReadAsync());
			}
		}
	}

	@Override
	public void send(Message m) {
		conn.send(m, null);
	}

	@Override
	public Message read() {
		Message m = read;
		read = null;
		return m; 
	}
	class ReadAsync implements AsyncCallback<Message> {
		@Override
		public void onFailure(Throwable arg0) {
			arg0.printStackTrace();
		}
		@Override
		public void onSuccess(Message arg0) {
			read = arg0;
		}
	}

	@Override
	public void close() {
		conn.close(null);
	}
}