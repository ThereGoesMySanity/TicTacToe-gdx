package tgms.ttt.GameState;

import java.awt.BasicStroke;
import com.badlogic.gdx.graphics.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import tgms.ttt.Main.GamePanel;

public class BoardState extends GameState {
	//0 = blank but part of board, 1 and 2 = X and O, 3 and 4 = completed X and O row, 5 = Not board
	private ArrayList<ArrayList<Integer>> fullBoard = new ArrayList<ArrayList<Integer>>();
	protected int turn = 1;
	private Point lastMove;
	private int winner = 0;
	private int offsetx, offsety;
	private int squareSize;

	private int boardSize;
	private int inARow;

	private int mouseX, mouseY;

	private Color colorX, colorO;
	private Color colorBoard;
	private BufferedImage xPic, oPic;

	public BoardState(GameStateManager gsm, int boardSize, int inARow) {
		this.boardSize = boardSize;
		this.inARow = inARow;
		this.gsm = gsm;
		init();
	}

	private void addNewBoard(int x, int y,
			ArrayList<ArrayList<Integer>> board) {
		//Update 2018-07-15: I refactored it, they're switched now
		//Okay, I consider x to be the outside arraylist and I don't care if that's weird
		//Also, I'll probably mess up a lot either way.
		//Update 5/20/15: Yes I messed up a lot, oh well
		
		if (y < 0 || y > board.size() - 1) { //expanding up/down
			for (int i = 0; i < boardSize; i++) {
				ArrayList<Integer> z = new ArrayList<Integer>();
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
			int loc = (x < 0)? 0 : board.get(0).size();
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
	}

	@Override
	public void init() {
		colorX = gsm.xColor;
		colorO = gsm.oColor;
		colorBoard = gsm.boardColor;
		xPic = gsm.xImage;
		oPic = gsm.oImage;
		for (int i = 0; i < boardSize; i++) {
			ArrayList<Integer> z = new ArrayList<Integer>();
			for (int j = 0; j < boardSize; j++) {
				z.add(0);
			}
			fullBoard.add(z);
		}
	}

	private Point getCoords(int x, int y) {
		return new Point(x * squareSize + offsetx, y * squareSize + offsety);
	}

	private boolean inBounds(int x, int y) {
		return !(x < 0 
				|| x >= fullBoard.size() 
				|| y < 0 
				|| y >= fullBoard.get(0).size());
	}

	private void drawGrid(Graphics2D g) {
		Point point1, point2;
		for (int i = 0; i < fullBoard.size(); i++) {
			for (int j = 0; j < fullBoard.get(0).size(); j++) {
				if (getSquareType(i, j) != 5) {
					if (i != fullBoard.size() - 1
							&& fullBoard.get(i + 1).get(j) != 5) { // if the one below it is also a valid space
						point1 = getCoords(j, i + 1);
						point2 = getCoords(j + 1, i + 1);
						g.drawLine(point1.x, point1.y, point2.x, point2.y);
					}
					if (j != fullBoard.get(0).size() - 1
							&& fullBoard.get(i).get(j + 1) != 5) { // if the one to the right is valid
						point1 = getCoords(j + 1, i);
						point2 = getCoords(j + 1, i + 1);
						g.drawLine(point1.x, point1.y, point2.x, point2.y);
					}
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		Point point0 = new Point();
		Point point1 = new Point();
		Point point2 = new Point();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, TicTacToe.WIDTH, TicTacToe.HEIGHT);
		point0 = getCoords(mouseX, mouseY);
		if (inBounds(mouseY, mouseX) && getSquareType(mouseY, mouseX) != 5) {
			g.setColor(Color.GREEN);
			g.fillRect(point0.x, point0.y, squareSize, squareSize);
		}
		g.setColor(colorBoard);
		drawGrid(g);
		for (int i = 0; i < fullBoard.size(); i++) {
			for (int j = 0; j < fullBoard.get(0).size(); j++) {
				if (getSquareType(i, j) != 5) {
					if (getSquareType(i, j) == 1) {

						point1 = getCoords(j, i);
						point2 = getCoords(j + 1, i + 1);
						if (xPic != null) {
							g.drawImage(xPic, point1.x, point1.y, squareSize,
									squareSize, null);
						} else {
							g.drawLine(point1.x, point1.y, point2.x, point2.y);
							point1 = getCoords(j + 1, i);
							point2 = getCoords(j, i + 1);
							g.drawLine(point1.x, point1.y, point2.x, point2.y);
						}
					} else if (getSquareType(i, j) == 2) {
						point1 = getCoords(j, i);
						point2 = getCoords(j + 1, i + 1);
						if (oPic != null) {
							g.drawImage(oPic, point1.x, point1.y, squareSize,
									squareSize, null);
						} else {
							g.drawOval(point1.x + 1, point1.y + 1,
									squareSize - 2, squareSize - 2);
						}
					} else if (getSquareType(i, j) == 3) {
						point1 = getCoords(j, i);
						point2 = getCoords(j + 1, i + 1);
						g.setColor(colorX);
						if (xPic != null) {
							g.drawImage(xPic, point1.x, point1.y, squareSize,
									squareSize, null);
						} else {
							g.drawLine(point1.x, point1.y, point2.x, point2.y);
							point1 = getCoords(j + 1, i);
							point2 = getCoords(j, i + 1);
							g.drawLine(point1.x, point1.y, point2.x, point2.y);
						}
						for (int k = -1; k < 2; k++) {
							for (int l = -1; l < 2; l++) {
								if (i + k > 0 && j + l > 0
										&& i + k < fullBoard.size()
										&& j + l < fullBoard.get(0).size()) {
									if (fullBoard.get(i + k).get(j + l) == 3) {
										point1 = getCoords(j, i);
										point2 = getCoords(j + l, i + k);
										g.setStroke(new BasicStroke(3));
										g.drawLine(point1.x + squareSize / 2,
												point1.y + squareSize / 2,
												point2.x + squareSize / 2,
												point2.y + squareSize / 2);
										g.setStroke(new BasicStroke());
									}
								}
							}
						}
					} else if (getSquareType(i, j) == 4) {
						point1 = getCoords(j, i);
						point2 = getCoords(j + 1, i + 1);
						g.setColor(colorO);
						if (oPic != null) {
							g.drawImage(oPic, point1.x, point2.x, squareSize,
									squareSize, null);
						} else {
							g.drawOval(point1.x + 1, point1.y + 1, squareSize,
									squareSize);
						}
						for (int k = -1; k < 2; k++) {
							for (int l = -1; l < 2; l++) {
								if (i + k > 0 && j + l > 0
										&& i + k < fullBoard.size()
										&& j + l < fullBoard.get(0).size()) {
									if (fullBoard.get(i + k).get(j + l) == 4) {
										point1 = getCoords(j, i);
										point2 = getCoords(j + l, i + k);
										g.setStroke(new BasicStroke(3));
										g.drawLine(point1.x + squareSize / 2,
												point1.y + squareSize / 2,
												point2.x + squareSize / 2,
												point2.y + squareSize / 2);
										g.setStroke(new BasicStroke());
									}
								}
							}
						}
					}
					g.setColor(colorBoard);
				}
			}
		}
		if (winner != 0) {
			gsm.setState(GameStateManager.GAMEOVER);
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_R) {
			gsm.setState(GameStateManager.BOARDSTATE);
		}
		if (k == KeyEvent.VK_ENTER) {
			makeMove(mouseX, mouseY);
		}
		if (k == KeyEvent.VK_DOWN) {
			mouseY += 1;
		}
		if (k == KeyEvent.VK_UP) {
			mouseY -= 1;
		}
		if (k == KeyEvent.VK_RIGHT) {
			mouseX += 1;
		}
		if (k == KeyEvent.VK_LEFT) {
			mouseX -= 1;
		}
	}

	@Override
	public void keyReleased(int k) {
	}

	@Override
	public void update() {
		if (fullBoard.size() * TicTacToe.WIDTH > fullBoard.get(0).size()
										* TicTacToe.HEIGHT) {
			squareSize = TicTacToe.HEIGHT / fullBoard.size();
			offsetx = (TicTacToe.WIDTH - squareSize * fullBoard.get(0).size())
					/ 2;
			offsety = 0;
		} else if (fullBoard.size() * TicTacToe.WIDTH < fullBoard.get(0).size()
				* TicTacToe.HEIGHT) {
			squareSize = TicTacToe.WIDTH / fullBoard.get(0).size();
			offsety = (TicTacToe.HEIGHT - squareSize * fullBoard.size()) / 2;
			offsetx = 0;
		} else {
			squareSize = TicTacToe.WIDTH / fullBoard.size();
			offsetx = 0;
			offsety = 0;
		}
		boolean full = true;
		for (int i = 0; i < fullBoard.size(); i++) {
			for (int j = 0; j < fullBoard.get(0).size(); j++) { //god this looks like such a mess, I know
				if (getSquareType(i, j) == 0) {
					full = false;
				}
				if (i < fullBoard.size() - 2
						&& j < fullBoard.get(0).size() - 2) {
					if (getSquareType(i, j) == fullBoard.get(i + 1).get(j + 1)
							&& fullBoard.get(i + 1).get(j + 1) == fullBoard
							.get(i + 2).get(j + 2)
							&& (getSquareType(i, j) == 1
							|| getSquareType(i, j) == 2)) {
						fullBoard.get(i).set(j, getSquareType(i, j) + 2);
						fullBoard.get(i + 1).set(j + 1, getSquareType(i, j));
						fullBoard.get(i + 2).set(j + 2, getSquareType(i, j));
						buildFromLastMove(false);
					}
				}
				if (i < fullBoard.size() - 2) {
					if (getSquareType(i, j) == fullBoard.get(i + 1).get(j)
							&& fullBoard.get(i + 1).get(j) == fullBoard
							.get(i + 2).get(j)
							&& (getSquareType(i, j) == 1
							|| getSquareType(i, j) == 2)) {
						fullBoard.get(i).set(j, getSquareType(i, j) + 2);
						fullBoard.get(i + 1).set(j, getSquareType(i, j));
						fullBoard.get(i + 2).set(j, getSquareType(i, j));
						buildFromLastMove(false);
					}
				}
				if (j < fullBoard.get(0).size() - 2) {
					if (getSquareType(i, j) == fullBoard.get(i).get(j + 1)
							&& fullBoard.get(i).get(j + 1) == fullBoard.get(i)
							.get(j + 2)
							&& (getSquareType(i, j) == 1
							|| getSquareType(i, j) == 2)) {
						fullBoard.get(i).set(j, getSquareType(i, j) + 2);
						fullBoard.get(i).set(j + 1, getSquareType(i, j));
						fullBoard.get(i).set(j + 2, getSquareType(i, j));
						buildFromLastMove(false);
					}
				}
				if (j > 1 && i < fullBoard.size() - 2) {
					if (getSquareType(i, j) == fullBoard.get(i + 1).get(j - 1)
							&& fullBoard.get(i + 1).get(j - 1) == fullBoard
							.get(i + 2).get(j - 2)
							&& (getSquareType(i, j) == 1
							|| getSquareType(i, j) == 2)) {
						fullBoard.get(i).set(j, getSquareType(i, j) + 2);
						fullBoard.get(i + 1).set(j - 1, getSquareType(i, j));
						fullBoard.get(i + 2).set(j - 2, getSquareType(i, j));
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
				ArrayList<Point> match = new ArrayList<Point>();
				int previousSize = 0;
				match.add(new Point(i, j));
				if (getSquareType(i, j) == 3) {
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
												.get(match.get(x).y + l) == 3
												&& !(match.contains(new Point(
														match.get(x).x + k,
														match.get(x).y + l)))) {
											match.add(new Point(
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
				if (getSquareType(i, j) == 4) {
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
												&& !(match.contains(new Point(
														match.get(x).x + k,
														match.get(x).y + l)))) {
											match.add(new Point(
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

	private int getSquareType(int x, int y) {
		try {
			return fullBoard.get(x).get(y);
		} catch (Exception e) {
			System.out.println(x + " " + y);
			System.out
			.println(fullBoard.size() + " " + fullBoard.get(0).size());
			System.exit(1);
		}
		return 0;
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
		System.out.println(boardX + x * boardSize);
		System.out.println(boardY + y * boardSize);
		addNewBoard(boardX + x * boardSize, boardY + y * boardSize, fullBoard);

		if(noMovesLeft && x == 0 && y == 0) {
			addNewBoard(-boardSize, -boardSize, fullBoard);
		}
	}

	@Override
	public void mouseReleased(Point point) {
		int currentX = (point.x - offsetx) / squareSize;
		int currentY = (point.y - offsety) / squareSize;
		if (currentY < fullBoard.size() && currentX < fullBoard.get(0).size()) {
			//I have to remember that X is Y and Y is X
			makeMove(currentX, currentY);//Literally 80% of my problems are that
		}
	}

	public synchronized void makeMove(int x, int y) {
		if (fullBoard.get(y).get(x) == 0) {
			fullBoard.get(y).set(x, getTurn());
			lastMove = new Point(x, y);
			nextTurn();
		}
	}

	public int getTurn() {
		return turn;
	}

	private void nextTurn() {
		turn = turn % 2 + 1;
	}

	@Override
	public void mouseClicked(Point click) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int currentX = (e.getX() - offsetx) / squareSize;
		int currentY = (e.getY() - offsety) / squareSize;
		if (inBounds(currentY, currentX)) { //I have to remember that X is Y and Y is X
			mouseX = currentX; //Update 2017-05-06: past me didn't remember this and I had to fix it :/
			mouseY = currentY; //it was literally that exact line that I messed it up...
		}
	}
}
