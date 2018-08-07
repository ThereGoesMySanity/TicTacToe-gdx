package tgms.ttt.Net;

public class ConnectionThread implements Runnable {
    private Connection conn;
    private int sleep;
    private boolean running = true;

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
        while (running) {
            try {
                Thread.sleep(sleep);
                conn.handleInput();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stop() {
    	running = false;
    }
}
