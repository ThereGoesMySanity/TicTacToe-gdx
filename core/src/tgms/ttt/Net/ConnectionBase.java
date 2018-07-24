package tgms.ttt.Net;

import java.io.IOException;

public interface ConnectionBase {
    int available();
    void send(String s);
    String read();
    void getInput();
}
