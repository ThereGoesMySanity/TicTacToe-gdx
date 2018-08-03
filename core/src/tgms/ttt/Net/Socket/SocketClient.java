package tgms.ttt.Net.Socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;

public class SocketClient extends SocketConnection {
	Socket conn;
    public SocketClient(String username, String host, int port) {
        super(username);
        conn = Gdx.net.newClientSocket(Net.Protocol.TCP, host, port, null);
        connected = true;
    }
    
    @Override
    public void start() {
    	try {
    		init(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	super.start();
    }

	@Override
	public boolean first() {
		return false;
	}
}