package tgms.ttt.Net;

import java.io.Serializable;

import com.badlogic.gdx.math.GridPoint2;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2311602896746533892L;
	public Player player;
	public int x, y;
	
	public Message() {
		player = null;
		x = y = -1;
	}
	
	public Message(String name) {
		player = new Player(name);
		x = y = -1;
	}
	public Message(Player p, GridPoint2 m) {
		player = p;
		x = m.x;
		y = m.y;
	}
	public Message(Player p, int x, int y) {
		player = p;
		this.x = x;
		this.y = y;
	}
}
