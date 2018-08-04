package tgms.ttt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import tgms.ttt.Net.Message;

public interface GameConnectionServiceAsync {
	void send(Message m, AsyncCallback<Void> callback);
	void read(AsyncCallback<Message> callback);
	void first(AsyncCallback<Boolean> callback);
	void close(AsyncCallback<Void> callback);

	void getUsers(AsyncCallback<String[]> callback);
	void connect(String username, AsyncCallback<Void> callback);
	void connectToUser(String username, AsyncCallback<Void> callback);
}
