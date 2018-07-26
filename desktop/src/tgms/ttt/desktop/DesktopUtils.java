package tgms.ttt.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tgms.ttt.Net.Socket.SocketClient;
import tgms.ttt.Net.Socket.SocketServer;
import tgms.ttt.PlatformInterfaces.FreeTypeFontGenerator;
import tgms.ttt.PlatformInterfaces.FreeTypeFontParameter;
import tgms.ttt.PlatformInterfaces.Platform;

import static tgms.ttt.Net.Connection.DEFAULT_PORT;

class DesktopUtils extends Platform {
	@Override
	public BitmapFont getFont() {
        if (font == null) {
            Gdx.app.debug("test", Gdx.files.internal("fonts/default.ttf").exists() + " font");
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/default.ttf"));
            FreeTypeFontParameter param = new FreeTypeFontParameter();
            param.size = FONT_SIZE;
            param.flip = true;
            font = gen.generateFont(param);
            gen.dispose();
        }
        return font;
	}
    DesktopUtils() {
        thread = (r) -> { new Thread(r).start(); };
        online = () -> {
            String[] values = {"Host", "Connect"};
            String value = (String) JOptionPane.showInputDialog(null,
                    "Host server or connect to server?",
                    "Connection", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
            String player = JOptionPane.showInputDialog("Enter name:");
            if (value.equals("Host")) {
                return new SocketServer(player, DEFAULT_PORT);
            } else if (value.equals("Connect")) {
                String ipaddr = JOptionPane.showInputDialog("Enter IP to connect to:");
                return new SocketClient(player, ipaddr, DEFAULT_PORT);
            }
            return null;
        };

        os = () -> {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                return new Texture(Gdx.files.absolute(jfc.getSelectedFile().getAbsolutePath()));
            } else {
                return null;
            }
        };
    }
}
