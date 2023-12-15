package tgms.ttt;

import tgms.ttt.GameState.BoardModel;
import tgms.ttt.BoardTree;
import java.util.ArrayList;
import java.lang.System;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimLauncher {
    public static long iterations = 0;
    public static long printTime;
    public static void main(String[] args) {
        printTime = System.currentTimeMillis();
        BoardTree result = iterate(new BoardModel(3, 3), 0);
        if (result != null) System.out.println("Solved");
        else System.out.println("Not solved");
        System.out.println(iterations);
    }
    //refactor to be bfs for threading?
    public static BoardTree iterate(BoardModel state, int depth) {
        if (depth > 7) return null;
        if (depth < 5) {
            long elapsed = System.currentTimeMillis() - printTime;
            if (elapsed > 1000) {
                System.out.println((iterations * 1000 / elapsed) + " iterations/sec");
                printTime = System.currentTimeMillis();
                iterations = 0;
            }
        }
        for (int i = 0; i < state.fullBoard.length; i++) {
            if (state.fullBoard[i] == 0) {
                BoardModel copy = new BoardModel(state).moveRaw(i);
                iterations++;
                if (copy.win != null && copy.win.equals("X")) {
                    return new BoardTree(copy, null);
                } else {
                    ArrayList<BoardTree> children = new ArrayList<>();
                    for(int j = 0; j < copy.fullBoard.length; j++) {
                        if (copy.fullBoard[j] == 0) {
                            BoardTree ret = iterate(new BoardModel(copy).moveRaw(j), depth + 1);
                            iterations++;
                            if (ret == null) {
                                children = null;
                                break;
                            }
                            children.add(ret);
                        }
                    }
                    if (children != null)
                        return new BoardTree(copy, children);
                }
            }
        }
        return null;
    }
}
