package tgms.ttt.desktop.Net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.ThreadedConnection;
import tgms.ttt.SocketConnection;

public class Server extends SocketConnection, implements ThreadedConnection {
    static ServerSocket s;

    public Server(String username, int port) throws IOException {
        super(username);
        s = new ServerSocket(port);
        
    }

    @Override
    public void getInput2(String s) {
    }
    
    public void run() {
    	try {
	        Socket sock = s.accept();
			init(sock);
			playerNum = (int) (Math.random() * 2) + 1;
	        out.writeUTF("turn:" + (playerNum == 1 ? 2 : 1));
	        connected = true;
	        start(sock);
    	} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
}
