package tgms.ttt.Net;

import java.io.IOException;
import java.net.Socket;

public class Client extends Connection {
	Socket conn;
    public Client(String username, String host, int port)
            throws IOException {
        super(username);
        conn = new Socket(host, port);
        connected = true;
    }
    
    @Override
    public void run() {
    	try {
    		init(conn);
			run(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    public void getInput2(String s) {
        if (s.startsWith("turn:")) {
            playerNum = s.charAt(5) - '0';
        }
    }
}
