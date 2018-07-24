package tgms.ttt.desktop.Net;

import java.io.IOException;
import java.net.Socket;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.ThreadedConnection;

public class Client extends ThreadedConnection {
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
			start(conn);
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
