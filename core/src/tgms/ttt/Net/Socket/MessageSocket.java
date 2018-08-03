package tgms.ttt.Net.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import tgms.ttt.Net.Message;

public class MessageSocket {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	public MessageSocket(InputStream i, OutputStream o) {
		try {
			in = new ObjectInputStream(i);
			out = new ObjectOutputStream(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public MessageSocket(Socket s) throws IOException {
		this(s.getInputStream(), s.getOutputStream());
	}
	public MessageSocket(InputStream i, OutputStream o, String username) {
		this(i, o);
		try {
			out.writeUTF(username);
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
	public void writeObject(Object obj) {
		try {
			out.writeObject(obj);
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
	public Object readObject() {
		try {
			return in.readObject();
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
