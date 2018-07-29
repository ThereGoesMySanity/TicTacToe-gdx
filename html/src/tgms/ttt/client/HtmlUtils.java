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
        	GameConnectionServiceConnection gcsc = new GameConnectionServiceConnection(user);
        	StringBuilder prompt = new StringBuilder("Select a user to connect to:\n");
        	for(String u : gcsc.getUsers()) {
        		if (!u.equals(gcsc.getUser().name)) {
        			prompt.append(u);
        			prompt.append('\n');
        		}
        	}
            String s = Window.prompt(prompt.toString(), "");
            if (s != null && !s.isEmpty()) {
            	gcsc.setUser(s);
            	return gcsc;
            }
            return null;
        };
    }
}
