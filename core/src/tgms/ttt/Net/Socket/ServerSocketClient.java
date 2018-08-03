package tgms.ttt.Net.Socket;

import com.badlogic.gdx.net.Socket;

public class ServerSocketClient extends SocketClient {

	public ServerSocketClient(String username, int port) {
		super(username, "raspberrypi", port);
	}

	@Override
	protected void init(Socket s) {
		super.init(s);
		
	}
}
