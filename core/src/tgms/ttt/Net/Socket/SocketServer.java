package tgms.ttt.Net.Socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;

public class SocketServer extends SocketConnection {
    static ServerSocket s;

    public SocketServer(String username, int port) {
        super(username);
        s = Gdx.net.newServerSocket(Net.Protocol.TCP, port, null);
    }

    public void start() {
    	try {
	        Socket sock = s.accept(null);
			init(sock);
	        connected = true;
    	} catch (Exception e1) {
			e1.printStackTrace();
		}
    	super.start();
    }

	@Override
	public boolean first() {
		return true;
	}
	
	@Override
	public void close() {
		super.close();
		s.dispose();
	}
}