package tgms.ttt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import tgms.ttt.PlatformInterfaces.Platform;
import tgms.ttt.TicTacToe;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 720;
		config.width = 720;
		new LwjglApplication(new TicTacToe(new DesktopUtils()), config);
	}
}
