package tgms.ttt.GameState;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import tgms.ttt.TicTacToe;
import tgms.ttt.GameState.GameStateManager.State;

public class BoardState extends GameState {
    //0 = blank but part of board, 1 and 2 = X and O, 3 and 4 = completed X and O row, 5 = Not board
    private ArrayList<ArrayList<Integer>> fullBoard;
    private int turn = 1;
    private GridPoint2 lastMove;
    private int winner = 0;
    private float offsetx;
    private float offsety;
    private float squareSize;

    private int boardSize;
    //TODO
//    private int inARow;

    private int mouseX, mouseY;

    private Color colorX, colorO;
    private Color colorBoard;
    private Texture xPic, oPic;

    BoardState(GameStateManager gsm, int boardSize, int inARow) {
        super(gsm);
        this.boardSize = boardSize;
//        this.inARow = inARow;
        colorX = gsm.xColor;
        colorO = gsm.oColor;
        colorBoard = gsm.boardColor;
        xPic = gsm.xImage;
        oPic = gsm.oImage;
        fullBoard = new ArrayList<>();
        for (int i = 0; i < boardSize; i++) {
            ArrayList<Integer> z = new ArrayList<>();
            for (int j = 0; j < boardSize; j++) {
                z.add(0);
            }
            fullBoard.add(z);
        }
        updateOffset();
    }

    private void addNewBoard(int x, int y,
                             ArrayList<ArrayList<Integer>> board) {
        //Update 2018-07-15: I refactored it, they're switched now
        //Okay, I consider x to be the outside arraylist and I don't care if that's weird
        //Also, I'll probably mess up a lot either way.
        //Update 5/20/15: Yes I messed up a lot, oh well

        if (y < 0 || y > board.size() - 1) { //expanding up/down
            for (int i = 0; i < boardSize; i++) {
                ArrayList<Integer> z = new ArrayList<>();
                for (int j = 0; j < board.get(0).size(); j++) {
                    if ((j < x + boardSize && j >= x && x >= 0)) { //adjusts for y if it isn't 0 or negative, almost forgot about this
                        z.add(0);
                    } else {
                        z.add(5);
                    }
                }
                if (y < 0) {
                    board.add(0, z);
                    y++;
                } else {
                    board.add(z);
                }
            }
        }
        if (x < 0 || x > board.get(0).size() - 1) { //expanding to the left/right
            int loc = (x < 0) ? 0 : board.get(0).size();
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < board.size(); j++) {
                    if (j < y + boardSize && j >= y) {
                        board.get(j).add(loc, 0); //append as opposed to add
                    } else {
                        board.get(j).add(loc, 5); //append lack of board everywhere else
                    }
                }
            }
        }
        if ((y >= 0 && y < board.size())
                && (x >= 0 && x < board.get(0).size())) { //If within board
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    board.get(y + i).set(x + j, 0);
                }
            }
        }
        updateOffset();
    }

    private Vector2 getCoords(int x, int y) {
        return new Vector2(x * squareSize + offsetx, y * squareSize + offsety);
    }

    private boolean inBounds(int x, int y) {
        return !(x < 0
                || x >= fullBoard.get(0).size()
                || y < 0
                || y >= fullBoard.size());
    }

    private void drawGrid(ShapeRenderer s) {
        Vector2 point1, point2;
        for (int i = 0; i < fullBoard.get(0).size(); i++) {
            for (int j = 0; j < fullBoard.size(); j++) {
                if (getBoard(i, j) != 5) {
                    if (j != fullBoard.size() - 1
                            && getBoard(i, j + 1) != 5) { // if the one below it is also a valid space
                        point1 = getCoords(i, j + 1);
                        point2 = getCoords(i + 1, j + 1);
                        s.line(point1, point2);
                    }
                    if (i != fullBoard.get(0).size() - 1
                            && getBoard(i + 1, j) != 5) { // if the one to the right is valid
                        point1 = getCoords(i + 1, j);
                        point2 = getCoords(i + 1, j + 1);
                        s.line(point1, point2);
                    }
                }
            }
        }
    }

    @Override
    public void draw(ShapeRenderer s, SpriteBatch sb) {

        Vector2 point0, point1, point2;
        s.begin(ShapeType.Line);
        if (Gdx.app.getType() != Application.ApplicationType.Android) {
            s.set(ShapeType.Filled);
            point0 = getCoords(mouseX, mouseY);
            if (inBounds(mouseX, mouseY) && getBoard(mouseX, mouseY) != 5) {
                s.setColor(Color.GREEN);
                s.rect(point0.x, point0.y, squareSize, squareSize);
            }
            s.set(ShapeType.Line);
        }
        s.setColor(colorBoard);
        drawGrid(s);
        for (int i = 0; i < fullBoard.get(0).size(); i++) {
            for (int j = 0; j < fullBoard.size(); j++) {
                if (getBoard(i, j) != 5) {
                    point1 = getCoords(i, j);
                    point2 = getCoords(i + 1, j + 1);
                    if (getBoard(i, j) == 1) {
                        if (xPic != null) {
                            s.end();
                            sb.begin();
                            sb.draw(xPic, point1.x, point1.y, squareSize, squareSize);
                            sb.end();
                            s.begin(ShapeType.Line);
                        } else {
                            s.line(point1, point2);
                            point1 = getCoords(i + 1, j);
                            point2 = getCoords(i, j + 1);
                            s.line(point1, point2);
                        }
                    } else if (getBoard(i, j) == 2) {
                        if (oPic != null) {
                            s.end();
                            sb.begin();
                            sb.draw(oPic, point1.x, point1.y, squareSize, squareSize);
                            sb.end();
                            s.begin(ShapeType.Line);
                        } else {
                            s.circle(point1.x + squareSize / 2, point1.y + squareSize / 2,
                                    squareSize / 2);
                        }
                    } else if (getBoard(i, j) == 3) {
                        s.setColor(colorX);
                        if (xPic != null) {
                            s.end();
                            sb.begin();
                            sb.draw(xPic, point1.x, point1.y, squareSize, squareSize);
                            sb.end();
                            s.begin(ShapeType.Line);
                        } else {
                            s.line(point1, point2);
                            point1 = getCoords(i + 1, j);
                            point2 = getCoords(i, j + 1);
                            s.line(point1, point2);
                        }
                        for (int k = -1; k < 2; k++) {
                            for (int l = -1; l < 2; l++) {
                                if (j + k > 0 && i + l > 0
                                        && j + k < fullBoard.size()
                                        && i + l < fullBoard.get(0).size()) {
                                    if (getBoard(i + l, j + k) == 3) {
                                        point1 = getCoords(i, j);
                                        point2 = getCoords(i + l, j + k);
                                        s.set(ShapeType.Filled);
                                        s.rectLine(point1.x + squareSize / 2,
                                                point1.y + squareSize / 2,
                                                point2.x + squareSize / 2,
                                                point2.y + squareSize / 2, 3);
                                        s.set(ShapeType.Line);
                                    }
                                }
                            }
                        }
                    } else if (getBoard(i, j) == 4) {
                        s.setColor(colorO);
                        if (oPic != null) {
                            s.end();
                            sb.begin();
                            sb.draw(oPic, point1.x, point1.y, squareSize, squareSize);
                            sb.end();
                            s.begin(ShapeType.Line);
                        } else {
                            s.circle(point1.x + squareSize / 2, point1.y + squareSize / 2,
                                    squareSize / 2);
                        }
                        for (int k = -1; k < 2; k++) {
                            for (int l = -1; l < 2; l++) {
                                if (j + k > 0 && i + l > 0
                                        && j + k < fullBoard.size()
                                        && i + l < fullBoard.get(0).size()) {
                                    if (getBoard(i + l, j + k) == 4) {
                                        point1 = getCoords(i, j);
                                        point2 = getCoords(i + l, j + k);
                                        s.set(ShapeType.Filled);
                                        s.rectLine(point1.x + squareSize / 2,
                                                point1.y + squareSize / 2,
                                                point2.x + squareSize / 2,
                                                point2.y + squareSize / 2, 3);
                                        s.set(ShapeType.Line);
                                    }
                                }
                            }
                        }
                    }
                    s.setColor(colorBoard);
                }
            }
        }
        if (winner != 0) {
            gsm.setState(State.GAMEOVER);
        }
        s.end();
    }

    @Override
    public boolean keyReleased(int k) {
        if (k == Input.Keys.R) {
            gsm.setState(State.BOARDSTATE);
        }
        if (k == Input.Keys.ENTER) {
            makeMove(mouseX, mouseY);
        }
        if (k == Input.Keys.DOWN) {
            mouseY += 1;
        }
        if (k == Input.Keys.UP) {
            mouseY -= 1;
        }
        if (k == Input.Keys.RIGHT) {
            mouseX += 1;
        }
        if (k == Input.Keys.LEFT) {
            mouseX -= 1;
        }
        return true;
    }

    private void updateOffset() {
        if (fullBoard.size() * TicTacToe.WIDTH > fullBoard.get(0).size() * TicTacToe.HEIGHT) {
            squareSize = TicTacToe.HEIGHT * 1.0f / fullBoard.size();
            offsetx = (TicTacToe.WIDTH - squareSize * fullBoard.get(0).size()) / 2;
            offsety = 0;
        } else if (fullBoard.size() * TicTacToe.WIDTH < fullBoard.get(0).size() * TicTacToe.HEIGHT) {
            squareSize = TicTacToe.WIDTH * 1.0f / fullBoard.get(0).size();
            offsety = (TicTacToe.HEIGHT - squareSize * fullBoard.size()) / 2;
            offsetx = 0;
        } else {
            squareSize = TicTacToe.WIDTH * 1.0f / fullBoard.size();
            offsetx = 0;
            offsety = 0;
        }
    }

    private void updateBoard() {
        boolean full = true;
        for (int i = 0; i < fullBoard.size(); i++) {
            for (int j = 0; j < fullBoard.get(0).size(); j++) { //god this looks like such a mess, I know
                if (getBoard(j, i) == 0) {
                    full = false;
                }
                if (i < fullBoard.size() - 2
                        && j < fullBoard.get(0).size() - 2) {
                    if (getBoard(j, i) == getBoard(j + 1, i + 1)
                            && getBoard(j + 1, i + 1) == fullBoard
                            .get(i + 2).get(j + 2)
                            && (getBoard(j, i) == 1
                            || getBoard(j, i) == 2)) {
                        fullBoard.get(i).set(j, getBoard(j, i) + 2);
                        fullBoard.get(i + 1).set(j + 1, getBoard(j, i));
                        fullBoard.get(i + 2).set(j + 2, getBoard(j, i));
                        buildFromLastMove(false);
                    }
                }
                if (i < fullBoard.size() - 2) {
                    if (getBoard(j, i) == getBoard(j, i + 1)
                            && getBoard(j, i + 1) == getBoard(j, i + 2)
                            && (getBoard(j, i) == 1
                            || getBoard(j, i) == 2)) {
                        fullBoard.get(i).set(j, getBoard(j, i) + 2);
                        fullBoard.get(i + 1).set(j, getBoard(j, i));
                        fullBoard.get(i + 2).set(j, getBoard(j, i));
                        buildFromLastMove(false);
                    }
                }
                if (j < fullBoard.get(0).size() - 2) {
                    if (getBoard(j, i) == getBoard(j + 1, i)
                            && getBoard(j + 1, i) == getBoard(j + 2, i)
                            && (getBoard(j, i) == 1
                            || getBoard(j, i) == 2)) {
                        fullBoard.get(i).set(j, getBoard(j, i) + 2);
                        fullBoard.get(i).set(j + 1, getBoard(j, i));
                        fullBoard.get(i).set(j + 2, getBoard(j, i));
                        buildFromLastMove(false);
                    }
                }
                if (j > 1 && i < fullBoard.size() - 2) {
                    if (getBoard(j, i) == getBoard(j - 1, i + 1)
                            && getBoard(j - 1, i + 1) == getBoard(j - 2, i + 2)
                            && (getBoard(j, i) == 1
                            || getBoard(j, i) == 2)) {
                        fullBoard.get(i).set(j, getBoard(j, i) + 2);
                        fullBoard.get(i + 1).set(j - 1, getBoard(j, i));
                        fullBoard.get(i + 2).set(j - 2, getBoard(j, i));
                        buildFromLastMove(false);
                    }
                }
            }
        }
        if (full) {
            buildFromLastMove(true);
        }
        for (int i = 0; i < fullBoard.size(); i++) {
            for (int j = 0; j < fullBoard.get(0).size(); j++) {
                ArrayList<GridPoint2> match = new ArrayList<>();
                int previousSize = 0;
                match.add(new GridPoint2(i, j));
                if (getBoard(j, i) == 3) {
                    while (previousSize < match.size()) {
                        previousSize = match.size();
                        for (int x = 0; x < match.size(); x++) {
                            for (int k = -1; k < 2; k++) {
                                for (int l = -1; l < 2; l++) {
                                    if (match.get(x).x + k > 0
                                            && match.get(x).x + k < fullBoard.size()
                                            && match.get(x).y + l > 0
                                            && match.get(x).y + l < fullBoard.get(0).size()) {
                                        if (fullBoard.get(match.get(x).x + k)
                                                .get(match.get(x).y + l) == 3
                                                && !(match.contains(new GridPoint2(
                                                match.get(x).x + k,
                                                match.get(x).y + l)))) {
                                            match.add(new GridPoint2(
                                                    match.get(x).x + k,
                                                    match.get(x).y + l));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (match.size() >= 9) {
                        winner = 1;
                    }
                }
                if (getBoard(j, i) == 4) {
                    while (previousSize < match.size()) {
                        previousSize = match.size();
                        for (int x = 0; x < match.size(); x++) {
                            for (int k = -1; k < 2; k++) {
                                for (int l = -1; l < 2; l++) {
                                    if (match.get(x).x + k > 0
                                            && match.get(x).x + k < fullBoard
                                            .size()
                                            && match.get(x).y + l > 0
                                            && match.get(x).y + l < fullBoard
                                            .get(0).size()) {
                                        if (fullBoard.get(match.get(x).x + k)
                                                .get(match.get(x).y + l) == 4
                                                && !(match.contains(new GridPoint2(
                                                match.get(x).x + k,
                                                match.get(x).y + l)))) {
                                            match.add(new GridPoint2(
                                                    match.get(x).x + k,
                                                    match.get(x).y + l));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (match.size() >= 9) {
                        winner = 2;
                    }
                }
            }
        }
        if (winner != 0) {
            if (winner == 1) {
                gsm.WIN = "X";
            } else {
                gsm.WIN = "O";
            }
        }
    }

    private int getBoard(int x, int y) {
        return fullBoard.get(y).get(x);
    }

    private void buildFromLastMove(boolean noMovesLeft) {
        int posx = lastMove.x % boardSize;
        int posy = lastMove.y % boardSize;
        int boardX = lastMove.x - posx;
        int boardY = lastMove.y - posy;
        posx -= boardSize / 2;
        posy -= boardSize / 2;
        if (boardSize % 2 == 0) {
            if (posx >= 0) posx++;
            if (posy >= 0) posy++;
        }
        int x = Integer.signum(Integer.signum(posx - posy) + Integer.signum(posx + posy));
        int y = Integer.signum(Integer.signum(posy - posx) + Integer.signum(posx + posy));
        addNewBoard(boardX + x * boardSize, boardY + y * boardSize, fullBoard);

        if (noMovesLeft && x == 0 && y == 0) {
            addNewBoard(-boardSize, -boardSize, fullBoard);
        }
    }

    @Override
    public boolean mouseReleased(int x, int y) {
        int currentX = (int) ((x - offsetx) / squareSize);
        int currentY = (int) ((y - offsety) / squareSize);
        if (currentY < fullBoard.size() && currentX < fullBoard.get(0).size()) {
            //I have to remember that X is Y and Y is X
            makeMove(currentX, currentY);//Literally 80% of my problems are that
        }
        return true;
    }

    public synchronized void makeMove(int x, int y) {
        if (getBoard(x, y) == 0) {
            fullBoard.get(y).set(x, getTurn());
            lastMove = new GridPoint2(x, y);
            nextTurn();
            updateBoard();
        }
    }

    public int getTurn() {
        return turn;
    }

    private void nextTurn() {
        turn = turn % 2 + 1;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        int currentX = (int) ((x - offsetx) / squareSize);
        int currentY = (int) ((y - offsety) / squareSize);
        if (inBounds(currentX, currentY)) { //I have to remember that X is Y and Y is X
            mouseX = currentX; //Update 2017-05-06: past me didn't remember this and I had to fix it :/
            mouseY = currentY; //it was literally that exact line that I messed it up...
        }
        return true;
    }

	@Override
	public void onResize() {
		updateOffset();
	}
}
