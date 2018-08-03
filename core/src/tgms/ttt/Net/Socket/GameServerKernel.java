package tgms.ttt.Net.Socket;

public interface GameServerKernel {
	void connect(String user);
	String[] getUsers();
	void connectToUser(String user);
}
