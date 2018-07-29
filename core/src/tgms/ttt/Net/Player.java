package tgms.ttt.Net;

public class Player {
	public String name;
	public int turn;
	public Player(String u) {
		this(u, (int)(Math.random()*2+1));
	}
	public Player(String u, int t) {
		name = u;
		turn = t;
	}
}
