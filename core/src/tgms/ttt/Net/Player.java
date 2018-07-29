package tgms.ttt.Net;

import java.io.Serializable;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5185398329756881993L;
	public String name;
	public int turn;
	public Player() {
		name = "";
		turn = 0;
	}
	public Player(String u) {
		this(u, (int)(Math.random()*2+1));
	}
	public Player(String u, int t) {
		name = u;
		turn = t;
	}
}
