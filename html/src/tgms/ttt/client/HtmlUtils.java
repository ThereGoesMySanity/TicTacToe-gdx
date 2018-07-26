package tgms.ttt.client;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.google.gwt.user.client.Window;

import tgms.ttt.Net.Socket.SocketClient;
import tgms.ttt.Net.Socket.SocketServer;
import tgms.ttt.PlatformInterfaces.Platform;

import static tgms.ttt.Net.Connection.DEFAULT_PORT;

class HtmlUtils extends Platform {

    @Override
    public BitmapFont getFont() {
        if (font == null) {
            font = new BitmapFont(true);
        }
        return font;
    }

    HtmlUtils() {
        threading = false;
        online = () -> {
            String s = Window.prompt("Enter IP to connect to,"
                            + " or \"host\" if you are hosting.",
                    "host");
            if (s != null) {
                String user = Window.prompt("Enter username", "");
                if (s.equals("host")) {
                    return new SocketServer(user, DEFAULT_PORT);
                } else {
                    return new SocketClient(user, s, DEFAULT_PORT);
                }
            }
            return null;
        };
    }
}
