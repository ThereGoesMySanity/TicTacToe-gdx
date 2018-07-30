package tgms.ttt.client;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.google.gwt.user.client.Window;

import tgms.ttt.PlatformInterfaces.Platform;

class HtmlUtils extends Platform {

	@Override
	public BitmapFont getFont() {
		if (font == null) {
			font = new BitmapFont(true);
		}
		return font;
	}

	HtmlUtils() {
		online = () -> {
			String user = Window.prompt("Enter username", "");
			return new GameConnectionServiceConnection(user);
		};
	}
}
