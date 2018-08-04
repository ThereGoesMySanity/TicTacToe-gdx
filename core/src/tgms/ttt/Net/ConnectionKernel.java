package tgms.ttt.Net;

public interface ConnectionKernel {
    void send(Message m);
    Message read();
    boolean first();
    void close();
}
