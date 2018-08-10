package tgms.ttt.Net.Socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import tgms.ttt.Net.Message;

public class MessageSocket {
	private Socket sock;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String[] users;
	public MessageSocket(InputStream i, OutputStream o) {
		try {
			System.out.println("made the stream");
			out = new ObjectOutputStream(o);
			in = new ObjectInputStream(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public MessageSocket(Socket s) throws IOException {
		this(s.getInputStream(), s.getOutputStream());
		sock = s;
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
		writeObject(m);
	}
	public void writeObject(Object obj) {
		try {
			out.writeObject(obj);
			out.flush();
			System.out.println("Wrote message ");
			if(sock != null)System.out.println(sock.isConnected());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Message read() {
		Object o = readObject();
		if (o instanceof String[]) {
			users = (String[])o;
			return new Message(Message.GET_USERS);
		}
		return (Message)o;
	}
	public synchronized Object readObject() {
		try {
			return in.readObject();
		} catch (EOFException e) {
			System.out.println("Client "+sock.getRemoteSocketAddress()+" disconnected");
			return null;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String[] getUsers() {
		String[] u = users;
		users = null;
		return u;
	}
}
