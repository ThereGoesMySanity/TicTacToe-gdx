package tgms.ttt.Net.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.net.Socket;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Message;
import tgms.ttt.Net.Player;

public abstract class SocketConnection extends Connection {
    ObjectInputStream in;
    protected ObjectOutputStream out;
    public static int DEFAULT_PORT = 5435;

    public SocketConnection(String username) {
        super(username);
    }

    public void init(Socket s) {
        try {
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
			connected = true;
			send(new Message(new Player(getUser().name), null));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public boolean available() {
        try {
            return in.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void send(Message m) {
    	try {
			out.writeObject(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public Message read() {
    	try {
			return (Message) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
    }
}
