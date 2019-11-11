package Main;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import GameState.GameStateManager;
public class GamePanel extends JPanel implements Runnable, KeyListener{
	/**
	 * Primero
	 */
	private static final long serialVersionUID = 1L;
	//Screen Dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 1;


	//Threads
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000/FPS;

	//Image
	private BufferedImage image;
	private	Graphics2D graphics;

	//Game State Manager
	private GameStateManager gameStateManager;


	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT*SCALE));
		setFocusable(true);
		requestFocus();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	public void init() {
		image = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
		running = true;
		gameStateManager = new GameStateManager();
	}

	@Override
	public void run() {

		init();

		long start;
		long elapsed;
		long wait;

		//Game Loop

		while(running) {

			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;
			wait = targetTime - (elapsed / 1000000);
			if(wait < 0) wait = 1;

			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void update() {
		gameStateManager.update();
	}

	public void draw() {
		gameStateManager.draw(graphics);
	}

	public void drawToScreen() {
		Graphics screen = getGraphics();
		screen.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		screen.dispose();
	}

	@Override
	public void keyPressed(KeyEvent key) {
		// TODO Auto-generated method stub
		gameStateManager.keyPressed(key.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent key) {
		// TODO Auto-generated method stub
		gameStateManager.keyReleased(key.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub

	}

}
