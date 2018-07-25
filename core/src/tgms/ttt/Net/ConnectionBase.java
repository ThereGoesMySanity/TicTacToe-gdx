package tgms.ttt.Net;

public interface ConnectionBase {
    void start();
    int available();
    void send(String s);
    String read();
    void handleInput();
}
