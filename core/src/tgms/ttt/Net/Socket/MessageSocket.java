package tgms.ttt.Net.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.net.Socket;

import tgms.ttt.Net.Message;

public class MessageSocket {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public MessageSocket(Socket s) {
		try {
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void write(Message m) {
		try {
			out.writeObject(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Message read() {
		try {
			return (Message)in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public boolean available() {
		try {
			return in.available() > 0;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
