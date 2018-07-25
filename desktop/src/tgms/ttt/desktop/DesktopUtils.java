package tgms.ttt.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tgms.ttt.PlatformInterfaces.OSQuery;
import tgms.ttt.PlatformInterfaces.Online;
import tgms.ttt.Net.Socket.SocketClient;
import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Socket.SocketServer;
import tgms.ttt.PlatformInterfaces.Platform;

import static tgms.ttt.Net.Connection.DEFAULT_PORT;

class DesktopUtils extends Platform {
    DesktopUtils() {
        threading = true;
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
