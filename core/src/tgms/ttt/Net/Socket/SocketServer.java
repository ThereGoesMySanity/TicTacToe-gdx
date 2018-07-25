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

    @Override
    public void getInput2(String s) {
    }

    public void start() {
    	try {
	        Socket sock = s.accept(null);
			init(sock);
			playerNum = (int) (Math.random() * 2) + 1;
	        out.writeUTF("turn:" + (playerNum == 1 ? 2 : 1));
	        connected = true;
    	} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
}
