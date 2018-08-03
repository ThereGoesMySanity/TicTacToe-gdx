package tgms.ttt.Net.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends SocketConnection {
    static ServerSocket ss;

    public SocketServer(String username, int port) {
        super(username);
        try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void start() {
    	try {
	        Socket s = ss.accept();
			init(s);
			sock = s;
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
		try {
			ss.close();
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}