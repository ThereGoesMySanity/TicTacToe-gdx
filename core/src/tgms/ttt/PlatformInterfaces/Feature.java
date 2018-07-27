package tgms.ttt.PlatformInterfaces;

import com.badlogic.gdx.graphics.Texture;

import tgms.ttt.Net.Connection;

public interface Feature {
	public interface Online extends Feature {
	    Connection getConnection();
	}
	public interface OSQuery extends Feature {
	    Texture getImage();
	}
	public interface Threading extends Feature {
		void start(Runnable r);
	}
}
