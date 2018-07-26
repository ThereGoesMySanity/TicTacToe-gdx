package tgms.ttt.Net;

public class ConnectionThread implements Runnable {
    private Connection conn;
    private int sleep;

    public ConnectionThread(Connection c) {
        this(c, 100);
    }
    public ConnectionThread(Connection c, int s) {
        conn = c;
        sleep = s;
    }
    
    @Override
    public void run() {
        conn.start();
        while(true) {
            try {
                Thread.sleep(sleep);
                if (conn.available() > 0) {
                    conn.handleInput();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
