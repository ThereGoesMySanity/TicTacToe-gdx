package tgms.ttt.PlatformInterfaces;

import com.badlogic.gdx.graphics.Texture;

import tgms.ttt.Net.Connection;

public interface Feature {
	public interface Online {
	    Connection getConnection();
		String connectToUser(Connection conn);
	}
	public interface OSQuery {
	    Texture getImage();
	}
	public interface Threading {
		void start(Runnable r);
	}
}
