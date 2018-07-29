package tgms.ttt.Net;

public interface ConnectionKernel {
    boolean available();
    void send(Message m);
    Message read();
    boolean first();
}
