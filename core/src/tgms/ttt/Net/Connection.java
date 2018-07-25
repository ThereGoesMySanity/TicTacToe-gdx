package tgms.ttt.Net;

import tgms.ttt.GameState.BoardState;

public abstract class Connection implements ConnectionBase {
    public static int DEFAULT_PORT = 5435;
    String user, userTwo;
    BoardState b;
    protected int playerNum;
    protected boolean connected = false;

    public String getPlayerName() {
        return userTwo;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int num) {
        this.playerNum = num;
    }

    public Connection(String username) {
        user = username;
    }

    public boolean connected() {
        return connected;
    }

    public void sendTurn() {
        playerNum = (int)(Math.random()*2)+1;
        send("turn:"+(playerNum ==1?2:1) +"\n");
    }

    public void setBoardState(BoardState b) {
        this.b = b;
    }

    public void makeMove(int x, int y) {
        if (playerNum == b.getTurn()) {
            send("move:" + x + "," + y + "\n");
        }
    }

    @Override
    public void handleInput() {
        handleInput("");
    }

    public void handleInput(String s){
        while (available() > 0) {
            s += read();
            if (s.contains("\n")) {
                break;
            }
        }
        String[] sa = s.split("\n");
        s = sa[0];
        getInput2(s);
        if (s.startsWith("user:")) {
            userTwo = s.substring(5);
        }
        if (s.startsWith("move:")) {
            String[] m = s.substring(5).split(",");
            b.makeMove(Integer.parseInt(m[0]), Integer.parseInt(m[1]));
        }
        if (sa.length > 1) {
            handleInput(sa[1]);
        }
    }
    public abstract void getInput2(String s);
}
