package tgms.ttt.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import tgms.ttt.Net.Message;

public interface GameConnectionServiceAsync {
	void available(AsyncCallback<Boolean> callback);
	void send(Message m, AsyncCallback<Void> callback);
	void read(AsyncCallback<Message> callback);
	void first(AsyncCallback<Boolean> callback);

	void getUsers(AsyncCallback<Set<String>> callback);
	void connect(String username, AsyncCallback<Void> callback);
	void connectToUser(String username, AsyncCallback<Void> callback);
}
