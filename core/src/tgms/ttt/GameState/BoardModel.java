package tgms.ttt.GameState;

import java.util.Arrays;
import java.util.HashSet;

import com.badlogic.gdx.math.GridPoint2;

public class BoardModel {
	//0 = blank but part of board, 1 and 2 = X and O, 3 and 4 = completed X and O row, 5 = Not board
	public int[] fullBoard;
    private int width;
    private int height;
	private int turn;
	private GridPoint2 lastMove;
	private int freeSpace;

	private int boardSize;
	private int inARow;

    public String win = null;
    public BoardModel(int boardSize, int inARow) {
        this.boardSize = boardSize;
        this.inARow = inARow;

		turn = 1;
        width = boardSize;
        height = boardSize;

		fullBoard = new int[width*height]; 
		freeSpace = 9;
    }
    public BoardModel(BoardModel copy) {
        boardSize = copy.boardSize;
        inARow = copy.inARow;

        turn = copy.turn;
		width = copy.width;
		height = copy.height;

        fullBoard = copy.fullBoard.clone();
        freeSpace = copy.freeSpace;
    }

	private void addNewBoard(int x, int y) {
		//Update 2018-07-15: I refactored it, they're switched now
		//Okay, I consider x to be the outside arraylist and I don't care if that's weird
		//Also, I'll probably mess up a lot either way.
		//Update 5/20/15: Yes I messed up a lot, oh well
        boolean expandV = (y < 0 || y > height - 1);
        boolean expandH = (x < 0 || x > width - 1);
        if (expandH || expandV) {
            int offsetX = (x<0? -x : 0);
            int startX = x + offsetX;
            int offsetY = (y<0? -y : 0);
            int startY = y + offsetY;
            int newWidth = (expandH? width + boardSize : width);
            int newHeight = (expandV? height + boardSize : height);
            int[] newBoard = new int[newWidth * newHeight];
            if (expandH) { //expanding to the left/right
                for(int i = 0; i < height; i++) {
                    if (i < y || i >= y + boardSize) Arrays.fill(newBoard, newWidth * (i + offsetY) + startX, newWidth * (i + offsetY) + startX + boardSize, 5);
                    System.arraycopy(fullBoard, i * width, newBoard, (i + offsetY) * newWidth + offsetX, width);
                }
            } else {
                System.arraycopy(fullBoard, 0, newBoard, offsetY * newWidth, fullBoard.length);
            }
            if (expandV) { //expanding up/down
                for(int i = startY; i < startY + boardSize; i++) {
                    Arrays.fill(newBoard, newWidth * i, newWidth * i + startX, 5);
                    Arrays.fill(newBoard, newWidth * i + startX + boardSize, newWidth * (i + 1), 5);
                }
            }
            fullBoard = newBoard;
            width = newWidth;
            height = newHeight;
        } else if (getBoard(x, y) == 5) { //If within board
			for (int i = 0; i < boardSize; i++) {
                Arrays.fill(fullBoard, (y + i) * width + x, (y + i) * width + x + boardSize, 0);
			}
		}
        freeSpace += boardSize * boardSize;
	}

	public void updateBoard() {
		GridPoint2[] directions = new GridPoint2[] {
				new GridPoint2(1, 0),
				new GridPoint2(0, 1),
				new GridPoint2(1, 1),
				new GridPoint2(-1, 1)
		};
		for (GridPoint2 dir : directions) {
			int pos, neg;
			GridPoint2 last = lastMove.cpy();
			for(pos = 0; pos < inARow && inBounds(last) && getBoard(last) == getBoard(lastMove); last.add(dir), pos++);
			last = lastMove.cpy().sub(dir);
			for(neg = 0; pos + neg < inARow && inBounds(last) && getBoard(last) == getBoard(lastMove); last.sub(dir), neg++);
			if (pos + neg == inARow) {
				last = lastMove.cpy();
				for(int i = 0; i < pos; i++, last.add(dir))
					setBoard(last, getBoard(last) + 2);
				last = lastMove.cpy().sub(dir);
				for(int i = 0; i < neg; i++, last.sub(dir))
					setBoard(last, getBoard(last) + 2);
				HashSet<GridPoint2> fill = new HashSet<>();
				floodFill(fill, lastMove);
				if(fill.size() >= inARow * inARow) {
					switch (getBoard(lastMove)) {
					case 3: win = "X"; break;
					case 4: win = "O"; break;
					}
                    return;
				} else {
					buildFromLastMove(false);
				}
				break;
			}
		}
		if (freeSpace == 0) {
			buildFromLastMove(true);
		}
	}

	private void floodFill(HashSet<GridPoint2> checked, GridPoint2 p) {
		checked.add(p);
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				GridPoint2 point = new GridPoint2(i, j).add(p);
				if(inBounds(point) && getBoard(point) == getBoard(p) && !checked.contains(point)) {
					floodFill(checked, point);
				}
			}
		}
	}

	private void setBoard(GridPoint2 p, int val) {
		setBoard(p.x, p.y, val);
	}

	private void setBoard(int x, int y, int val) {
        fullBoard[y * width + x] = val;
	}

	public int getBoard(GridPoint2 p) {
		return getBoard(p.x, p.y);
	}

	public int getBoard(int x, int y) {
		return fullBoard[y * width + x]; 
	}
	public int height() {
		return height;
	}
	public int width() {
		return width;
	}

	public boolean inBounds(GridPoint2 p) {
		return inBounds(p.x, p.y);
	}
	public boolean inBounds(int x, int y) {
		return !(x < 0
				|| x >= width()
				|| y < 0
				|| y >= height());
	}

	public int getTurn() {
		return turn;
	}
	private void nextTurn() {
		turn = turn % 2 + 1;
	}
    public String getWin() {
        return win;
    }


	public void makeMove(int x, int y) {
		makeMove(new GridPoint2(x, y));
	}

	public void makeMove(GridPoint2 p) {
		if (getBoard(p) == 0) {
			setBoard(p, getTurn());
			freeSpace--;
			lastMove = p;
			nextTurn();
			updateBoard();
		}
	}
    public BoardModel moveRaw(int i) {
        if (fullBoard[i] == 0) {
            fullBoard[i] = turn;
            freeSpace--;
            lastMove = new GridPoint2(i / height, i % height);
            nextTurn();
            updateBoard();
        }
        return this;
    }


	private void buildFromLastMove(boolean noMovesLeft) {
		int posx = lastMove.x % boardSize;
		int posy = lastMove.y % boardSize;
		int boardX = lastMove.x - posx + (posx - 1) * boardSize;
		int boardY = lastMove.y - posy + (posy - 1) * boardSize;
		

		if (inBounds(boardX, boardY) && getBoard(boardX, boardY) != 5) {
			//addNewBoard(-1, -1);
            win = "Tie";
		} else {
            addNewBoard(boardX, boardY);
        }
	}
}
