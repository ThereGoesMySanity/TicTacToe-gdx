package tgms.ttt.Net;

import java.io.IOException;

public interface ThreadedConnection extends ConnectionBase {
    default void loop() {
        while(true) {
            try {
                Thread.sleep(100);
                if (available() > 0) {
                    getInput();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
