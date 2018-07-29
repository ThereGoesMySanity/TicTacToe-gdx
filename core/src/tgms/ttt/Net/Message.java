package tgms.ttt.Net;

import java.io.Serializable;

import com.badlogic.gdx.math.GridPoint2;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2311602896746533892L;
	public Player player;
	public GridPoint2 move;
	
	public Message() {
		player = null;
		move = null;
	}
	
	public Message(String name) {
		player = new Player(name);
		move = null;
	}
	public Message(Player p, GridPoint2 m) {
		player = p;
		move = m;
	}
}
