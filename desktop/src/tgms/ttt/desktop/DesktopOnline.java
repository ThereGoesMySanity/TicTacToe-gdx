package tgms.ttt.desktop;

import java.io.IOException;

import javax.swing.JOptionPane;

import tgms.ttt.Online;
import tgms.ttt.desktop.Net.Client;
import tgms.ttt.Net.Connection;
import tgms.ttt.desktop.Net.Server;

import static tgms.ttt.Net.Connection.DEFAULT_PORT;

public class DesktopOnline implements Online {

    public Connection getConnection() {
        String[] values = {"Host", "Connect"};
        String value = (String) JOptionPane.showInputDialog(null,
                "Host server or connect to server?",
                "Connection", JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
        String player = JOptionPane.showInputDialog("Enter name:");
        if (value.equals("Host")) {
            return new Server(player, DEFAULT_PORT);
        } else if (value.equals("Connect")) {
            String ipaddr = JOptionPane.showInputDialog("Enter IP to connect to:");
            return new Client(player, ipaddr, DEFAULT_PORT);
        }
        return null;
    }
}
