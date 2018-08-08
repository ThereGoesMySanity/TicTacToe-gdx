package tgms.ttt.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Message;

public class GameConnectionServiceConnection extends Connection {
	private GameConnectionServiceAsync conn;
	private boolean first;
	private Message read;
	private InterruptDialog id;

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
				getUsers();
			}
		});
	}
	
	public void getUsers() {
		conn.getUsers(new GetUsersAsync());
	}

	class GetUsersAsync implements AsyncCallback<String[]> {
		@Override
		public void onSuccess(String[] result) {
			if (id == null) {
				id = new InterruptDialog(result, getUser().name, GameConnectionServiceConnection.this);
				addInterrupt(id);
				id.show();
			} else {
				id.refresh(result);
			}
		}
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}
	}
	
	class ChooseUserAsync implements AsyncCallback<String> {
		@Override
		public void onSuccess(String s) {
			if (s != null) {
				userTwo.name = s;
				conn.connectToUser(s, new StartAsync());
			}
			//TODO: on cancel
		}
		@Override
		public void onFailure(Throwable arg0) {
			arg0.printStackTrace();
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
	public void send(Message m) {
		conn.send(m, null);
	}

	@Override
	public Message read() {
		Message m = read;
		read = null;
		conn.read(new ReadAsync());
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