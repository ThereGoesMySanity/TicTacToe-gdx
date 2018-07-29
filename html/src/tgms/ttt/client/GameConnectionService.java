package tgms.ttt.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import tgms.ttt.Net.ConnectionKernel;

@RemoteServiceRelativePath("gameConnection")
public interface GameConnectionService extends RemoteService, ConnectionKernel {
	void connect(String username);
	Set<String> getUsers();
	void connectToUser(String username);
}
