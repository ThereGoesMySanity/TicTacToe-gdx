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
import java.util.HashSet;

import tgms.ttt.TicTacToe;
import tgms.ttt.GameState.GameStateManager.State;

public class BoardState extends GameState {
	//0 = blank but part of board, 1 and 2 = X and O, 3 and 4 = completed X and O row, 5 = Not board
	private ArrayList<ArrayList<Integer>> fullBoard;
	private int turn;
	private GridPoint2 lastMove, mouse;
	private int freeSpace;
	private Vector2 offset;
	private float squareSize;

	private int boardSize;
	private int inARow;

	private Color colorX, colorO, colorBoard;
	private Texture xPic, oPic;

	BoardState(GameStateManager gsm, int boardSize, int inARow) {
		super(gsm);
		this.boardSize = boardSize;
		this.inARow = inARow;
		colorX = gsm.xColor;
		colorO = gsm.oColor;
		colorBoard = gsm.boardColor;
		xPic = gsm.xImage;
		oPic = gsm.oImage;
		turn = 1;
		fullBoard = new ArrayList<>();
		for (int i = 0; i < boardSize; i++) {
			ArrayList<Integer> z = new ArrayList<>();
			for (int j = 0; j < boardSize; j++) {
				z.add(0);
			}
			fullBoard.add(z);
		}
		freeSpace = 9;
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
			freeSpace += boardSize * boardSize;
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
			freeSpace += boardSize * boardSize;
		}
		if ((y >= 0 && y < board.size())
				&& (x >= 0 && x < board.get(0).size())
				&& getBoard(x, y) == 5) { //If within board
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					board.get(y + i).set(x + j, 0);
				}
			}
			freeSpace += boardSize * boardSize;
		}
		updateOffset();
	}
	private Vector2 getCoords(GridPoint2 p) {
		return getCoords(p.x, p.y);
	}

	private Vector2 getCoords(int x, int y) {
		return new Vector2(x, y).scl(squareSize).add(offset);
	}

	private boolean inBounds(GridPoint2 p) {
		return inBounds(p.x, p.y);
	}

	private boolean inBounds(int x, int y) {
		return !(x < 0
				|| x >= boardWidth()
				|| y < 0
				|| y >= boardHeight());
	}

	private void drawGrid(ShapeRenderer s) {
		Vector2 point1, point2;
		for (int i = 0; i < boardWidth(); i++) {
			for (int j = 0; j < boardHeight(); j++) {
				if (getBoard(i, j) != 5) {
					if (j != boardHeight() - 1
							&& getBoard(i, j + 1) != 5) { // if the one below it is also a valid space
						point1 = getCoords(i, j + 1);
						point2 = getCoords(i + 1, j + 1);
						s.line(point1, point2);
					}
					if (i != boardWidth() - 1
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
		s.begin(ShapeType.Line);
		if (Gdx.app.getType() != Application.ApplicationType.Android) {
			s.set(ShapeType.Filled);
			Vector2 point = getCoords(mouse);
			if (inBounds(mouse) && getBoard(mouse) != 5) {
				s.setColor(Color.GREEN);
				s.rect(point.x, point.y, squareSize, squareSize);
			}
			s.set(ShapeType.Line);
		}
		s.setColor(colorBoard);
		drawGrid(s);
		for (int i = 0; i < boardWidth(); i++) {
			for (int j = 0; j < boardHeight(); j++) {
				if (getBoard(i, j) != 5 && getBoard(i, j) != 0) {
					s.setColor(colorBoard);
					switch (getBoard(i, j)) {
					case 3: s.setColor(colorX); break;
					case 4: s.setColor(colorO); break;
					}
					drawShape(s, sb, i,  j);
					if (getBoard(i, j) == 3 || getBoard(i, j) == 4) drawMatch(s, i, j);
				}
			}
		}
		s.end();
	}

	private void drawShape(ShapeRenderer s, SpriteBatch sb, int x, int y) {
		Texture pic = getBoard(x, y) == 3? xPic : oPic;
		Vector2 point = getCoords(x, y);
		if (pic != null) {
			s.end();
			sb.begin();
			sb.draw(pic, point.x, point.y, squareSize, squareSize);
			sb.end();
			s.begin(ShapeType.Line);
		} else {
			point.add(squareSize / 2, squareSize / 2);
			if(getBoard(x, y) == 1 || getBoard(x, y) == 3) {
				s.x(point, squareSize / 2); 
			} else if (getBoard(x, y) == 2 || getBoard(x, y) == 4) {
				s.circle(point.x, point.y, squareSize / 2);
			}
		}
	}

	private void drawMatch(ShapeRenderer s, int x, int y) {
		for (int k = -1; k < 2; k++) {
			for (int l = -1; l < 2; l++) {
				if (inBounds(x + l, y + k) && getBoard(x + l, y + k) == getBoard(x, y)) {
					Vector2 center = new Vector2(squareSize / 2, squareSize / 2);
					Vector2 point1 = getCoords(x, y).add(center);
					Vector2 point2 = getCoords(x + l, y + k).add(center);
					s.set(ShapeType.Filled);
					s.rectLine(point1, point2, 3);
					s.set(ShapeType.Line);
				}
			}
		}
	}

	@Override
	public boolean keyReleased(int k) {
		if (k == Input.Keys.R) {
			gsm.setState(State.BOARDSTATE);
		}
		if (k == Input.Keys.ENTER) {
			makeMove(mouse);
		}
		if (k == Input.Keys.DOWN) {
			mouse.add(0, 1);
		}
		if (k == Input.Keys.UP) {
			mouse.sub(0, 1);
		}
		if (k == Input.Keys.RIGHT) {
			mouse.add(1, 0);
		}
		if (k == Input.Keys.LEFT) {
			mouse.sub(1, 0);
		}
		return true;
	}

	private void updateOffset() {
		if (boardHeight() * TicTacToe.WIDTH > boardWidth() * TicTacToe.HEIGHT) {
			squareSize = TicTacToe.HEIGHT * 1f / boardHeight();
			offset.set((TicTacToe.WIDTH - squareSize * boardWidth()) / 2, 0);
		} else if (boardHeight() * TicTacToe.WIDTH < boardWidth() * TicTacToe.HEIGHT) {
			squareSize = TicTacToe.WIDTH * 1f / boardWidth();
			offset.set(0, (TicTacToe.HEIGHT - squareSize * boardHeight()) / 2);
		} else {
			squareSize = TicTacToe.WIDTH * 1f / boardHeight();
			offset.set(0, 0);
		}
	}

	private void updateBoard() {
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
				System.out.println(fill.size());
				if(fill.size() >= inARow * inARow) {
					switch (getBoard(lastMove)) {
					case 3: gsm.WIN = "X"; break;
					case 4: gsm.WIN = "O"; break;
					}
					gsm.setState(State.GAMEOVER);
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
		fullBoard.get(y).set(x, val);
	}

	private int getBoard(GridPoint2 p) {
		return getBoard(p.x, p.y);
	}

	private int getBoard(int x, int y) {
		return fullBoard.get(y).get(x);
	}
	private int boardHeight() {
		return fullBoard.size();
	}
	private int boardWidth() {
		return fullBoard.get(0).size();
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
		Vector2 p = new Vector2(x, y).sub(offset).scl(1 / squareSize);
		if (p.x < boardWidth() && p.y < boardHeight()) {
			//I have to remember that X is Y and Y is X
			makeMove((int)p.x, (int)p.y);//Literally 80% of my problems are that
		}
		return true;
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

	public int getTurn() {
		return turn;
	}

	private void nextTurn() {
		turn = turn % 2 + 1;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		Vector2 v = new Vector2(x, y).sub(offset).scl(1 / squareSize);
		GridPoint2 p = new GridPoint2((int)v.x, (int)v.y);
		if (inBounds(p)) { //I have to remember that X is Y and Y is X
			mouse = p; //Update 2017-05-06: past me didn't remember this and I had to fix it :/
			//it was literally that exact line that I messed it up...
		}
		return true;
	}

	@Override
	public void onResize() {
		updateOffset();
	}
}
