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

import tgms.ttt.TicTacToe;
import tgms.ttt.GameState.GameStateManager.State;

public class BoardState extends GameState {
	protected BoardModel board;
	private GridPoint2 mouse;
	private Vector2 offset;
	private float squareSize;

	private Color colorX, colorO, colorBoard;
	private Texture xPic, oPic;

	BoardState(GameStateManager gsm, int boardSize, int inARow) {
		super(gsm);
		board = new BoardModel(boardSize, inARow);
		colorX = gsm.xColor;
		colorO = gsm.oColor;
		colorBoard = gsm.boardColor;
		xPic = gsm.xImage;
		oPic = gsm.oImage;
		offset = new Vector2();
		mouse = new GridPoint2();
		updateOffset();
	}

	private Vector2 getCoords(GridPoint2 p) {
		return getCoords(p.x, p.y);
	}

	private Vector2 getCoords(int x, int y) {
		return new Vector2(x, y).scl(squareSize).add(offset);
	}

	private void drawGrid(ShapeRenderer s) {
		Vector2 point1, point2;
		for (int i = 0; i < board.width(); i++) {
			for (int j = 0; j < board.height(); j++) {
				if (board.getBoard(i, j) != 5) {
					if (j != board.height() - 1
							&& board.getBoard(i, j + 1) != 5) { // if the one below it is also a valid space
						point1 = getCoords(i, j + 1);
						point2 = getCoords(i + 1, j + 1);
						s.line(point1, point2);
					}
					if (i != board.width() - 1
							&& board.getBoard(i + 1, j) != 5) { // if the one to the right is valid
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
			if (board.inBounds(mouse) && board.getBoard(mouse) != 5) {
				s.setColor(Color.GREEN);
				s.rect(point.x, point.y, squareSize, squareSize);
			}
			s.set(ShapeType.Line);
		}
		s.setColor(colorBoard);
		drawGrid(s);
		for (int i = 0; i < board.width(); i++) {
			for (int j = 0; j < board.height(); j++) {
				if (board.getBoard(i, j) != 5 && board.getBoard(i, j) != 0) {
					s.setColor(colorBoard);
					switch (board.getBoard(i, j)) {
					case 3: s.setColor(colorX); break;
					case 4: s.setColor(colorO); break;
					}
					drawShape(s, sb, i,  j);
					if (board.getBoard(i, j) == 3 || board.getBoard(i, j) == 4) drawMatch(s, i, j);
				}
			}
		}
		s.end();
	}

	private void drawShape(ShapeRenderer s, SpriteBatch sb, int x, int y) {
		Texture pic = board.getBoard(x, y) == 3? xPic : oPic;
		Vector2 point = getCoords(x, y);
		if (pic != null) {
			s.end();
			sb.begin();
			sb.draw(pic, point.x, point.y, squareSize, squareSize);
			sb.end();
			s.begin(ShapeType.Line);
		} else {
			point.add(squareSize / 2, squareSize / 2);
			if(board.getBoard(x, y) == 1 || board.getBoard(x, y) == 3) {
				s.x(point, squareSize / 2); 
			} else if (board.getBoard(x, y) == 2 || board.getBoard(x, y) == 4) {
				s.circle(point.x, point.y, squareSize / 2);
			}
		}
	}

	private void drawMatch(ShapeRenderer s, int x, int y) {
		for (int k = -1; k < 2; k++) {
			for (int l = -1; l < 2; l++) {
				if (board.inBounds(x + l, y + k) && board.getBoard(x + l, y + k) == board.getBoard(x, y)) {
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
		if (board.height() * TicTacToe.WIDTH > board.width() * TicTacToe.HEIGHT) {
			squareSize = TicTacToe.HEIGHT * 1f / board.height();
			offset.set((TicTacToe.WIDTH - squareSize * board.width()) / 2, 0);
		} else if (board.height() * TicTacToe.WIDTH < board.width() * TicTacToe.HEIGHT) {
			squareSize = TicTacToe.WIDTH * 1f / board.width();
			offset.set(0, (TicTacToe.HEIGHT - squareSize * board.height()) / 2);
		} else {
			squareSize = TicTacToe.WIDTH * 1f / board.height();
			offset.set(0, 0);
		}
	}

	@Override
	public boolean mouseReleased(int x, int y) {
		Vector2 p = new Vector2(x, y).sub(offset).scl(1 / squareSize);
		if (p.x < board.width() && p.y < board.height()) {
			//I have to remember that X is Y and Y is X
			makeMove((int)p.x, (int)p.y);//Literally 80% of my problems are that
		}
		return true;
	}

	protected void makeMove(int x, int y) {
		board.makeMove(x, y);
		checkState();
	}
	protected void makeMove(GridPoint2 p) {
		board.makeMove(p);
		checkState();
	}

	private void checkState() {
		updateOffset();		
		if (board.getWin() != null) {
			gsm.WIN = board.getWin();
			gsm.setState(State.GAMEOVER);
		}

	}

	@Override
	public boolean mouseMoved(int x, int y) {
		Vector2 v = new Vector2(x, y).sub(offset).scl(1 / squareSize);
		GridPoint2 p = new GridPoint2((int)v.x, (int)v.y);
		if (board.inBounds(p)) { //I have to remember that X is Y and Y is X
			mouse = p; //Update 2017-05-06: past me didn't remember this and I had to fix it :/
			//it was literally that exact line that I messed it up...
		}
		return true;
	}

	@Override
	public void onResize() {
		updateOffset();
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
	}
}
