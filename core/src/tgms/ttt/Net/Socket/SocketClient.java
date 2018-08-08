package tgms.ttt.Net.Socket;

import java.io.IOException;
import java.net.Socket;

public class SocketClient extends SocketConnection {
    public SocketClient(String username, String host, int port) {
        super(username);
        try {
			sock = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void start() {
    	try {
    		init(sock);
    		connected = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public boolean first() {
		return false;
	}
}