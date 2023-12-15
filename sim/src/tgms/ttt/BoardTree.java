package tgms.ttt;
import tgms.ttt.GameState.BoardModel;
import java.util.ArrayList;
public class BoardTree {
    public BoardModel state;
    public ArrayList<BoardTree> children;
    public BoardTree(BoardModel state, ArrayList<BoardTree> children) {
        this.state = state;
        this.children = children;
    }
}
