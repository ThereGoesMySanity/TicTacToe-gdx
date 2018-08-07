package tgms.ttt.desktop;

import static tgms.ttt.Net.Socket.SocketConnection.DEFAULT_PORT;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Socket.GameServerSocket;
import tgms.ttt.Net.Socket.SocketClient;
import tgms.ttt.Net.Socket.SocketServer;
import tgms.ttt.PlatformInterfaces.Feature.Online;
import tgms.ttt.PlatformInterfaces.Platform;

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
		online = new Online() {
			@Override
			public Connection getConnection() {
				String[] values = {"Host", "Connect"};
				String value = (String) JOptionPane.showInputDialog(null,
						"Host server or connect to server?",
						"Connection", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
				String player = JOptionPane.showInputDialog("Enter name:");
				if (value.equals("Host")) {
					return new SocketServer(player, DEFAULT_PORT);
				} else if (value.equals("Connect")) {
					int result = JOptionPane.showConfirmDialog(null, "Direct connect?");
					if (result == JOptionPane.YES_OPTION) {
						String ipaddr = JOptionPane.showInputDialog("Enter IP to connect to:");
						return new SocketClient(player, ipaddr, DEFAULT_PORT);
					} else {
						return new GameServerSocket(player, DEFAULT_PORT);
					}
				}
				return null;
			}

			@Override
			public String connectToUser(Connection c) {
				String s = "";
				if (c instanceof GameServerSocket) {
					GameServerSocket gss = (GameServerSocket)c;
					while (s == null || s.isEmpty()) {
						StringBuilder prompt = new StringBuilder("Select a user to connect to:\n");
						String[] users = gss.getUsers();
						for(String u : users) {
							if (!u.equals(gss.getUser().name)) {
								prompt.append(u);
								prompt.append('\n');
							}
						}
						if (users.length == 1) {
							prompt.append("No users! Leave the box blank and hit OK to refresh.");
						}
						InterruptPane ip = new InterruptPane(prompt.toString());
						gss.addInterrupt(ip);
						s = ip.showDialog();
					}
					gss.connectToUser(s);
				}
				return s;
			}
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
