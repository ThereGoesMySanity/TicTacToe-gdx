package tgms.ttt.desktop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import tgms.ttt.GameState.GameStateManager;

public class GamePanel extends JPanel
        implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static int WIDTH = 720;
    public static int HEIGHT = 720;
    public static final double SCALE = 1;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public static Graphics2D g;

    private static GameStateManager gsm;

    private JFileChooser jfc = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files",
            "jpg", "png", "gif", "jpeg");

    public GamePanel() {
        super();
        jfc.setFileFilter(filter);
        setPreferredSize(
                new Dimension((int) (WIDTH * SCALE), (int) (HEIGHT * SCALE)));
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addMouseListener(this);
            addKeyListener(this);
            addMouseMotionListener(this);
            thread.start();
        }
    }
    
    private void initImage() {
    	image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
    }

    private void init() {
        initImage();
        running = true;
        gsm = new GameStateManager();
    }

    @Override
    public void run() {
        init();
        while (running) {
        	if (getWidth() != WIDTH * SCALE || getHeight() != HEIGHT * SCALE) {
        		System.out.println("resize");
        		WIDTH = (int) (getWidth() / SCALE);
        		HEIGHT = (int) (getHeight()  / SCALE);
        		initImage();
        	}
            update();
            draw();
            drawToScreen();
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }

    private void update() {
        gsm.update();
    }

    public static void drawImage(BufferedImage i) {
        g.drawImage(i, 0, 0, null);
    }

    public void draw() {
        gsm.draw(g);
    }

    private void drawToScreen() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, (int) (WIDTH * SCALE), (int) (HEIGHT * SCALE),
                null);
        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent key) {
    }

    @Override
    public void keyPressed(KeyEvent key) {
        gsm.keyPressed(key.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent key) {
        gsm.keyReleased(key.getKeyCode());
    }

    @Override
    public void mouseReleased(MouseEvent mouse) {
        gsm.mouseReleased(mouse.getPoint());
    }

    @Override
    public void mouseEntered(MouseEvent mouse) {
    }

    @Override
    public void mouseExited(MouseEvent mouse) {
    }

    @Override
    public void mousePressed(MouseEvent mouse) {
    }

    @Override
    public void mouseClicked(MouseEvent mouse) {
    }

    public static void reDraw() {
        gsm.draw(g);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gsm.mouseMoved(e);
    }

    public File getImage() {
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        } else {
            return null;
        }
    }
}
