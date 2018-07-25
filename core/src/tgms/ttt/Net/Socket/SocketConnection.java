package tgms.ttt.Net.Socket;

import com.badlogic.gdx.net.Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import tgms.ttt.Net.Connection;

public abstract class SocketConnection extends Connection {
    DataInputStream in;
    protected DataOutputStream out;

    public SocketConnection(String username) {
        super(username);
    }

    public void init(Socket s) {
        in = new DataInputStream(s.getInputStream());
        out = new DataOutputStream(s.getOutputStream());
    }

    @Override
    public int available() {
        try {
            return in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void send(String s) {
        try {
            out.writeUTF(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String read() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
