package tgms.ttt.Net;

import java.io.Serializable;

public class Message implements Serializable {
	public static final byte CONNECT = 1;
	public static final byte GET_USERS = 2;
	public static final byte CONNECT_TO_USER = 3;
	public static final byte DISCONNECT = 4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2311602896746533892L;
	public Player player;
	public int x, y;
	public byte type;
	
	public Message() {
		player = null;
		x = y = -1;
		type = 0;
	}
	
	public Message(String name) {
		player = new Player(name);
		x = y = -1;
		type = 0;
	}
	
	public Message(int x, int y) {
		player = null;
		this.x = x;
		this.y = y;
		type = 0;
	}
	public Message(byte f) {
		x = y = -1;
		type = f;
	}
	@Override
	public String toString() {
		return "type: "+type;
	}
}
