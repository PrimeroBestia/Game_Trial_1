package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class MenuState extends GameState{
	
	private Background background;
	
	public static final int START = 0;
	public static final int HELP = 1;
	public static final int QUIT = 2;
	public static final String TITLE = "GAME";
	
	
	//Menu Screen Options
	private int currentChoice;
	private String[] options = {
			"Start",
			"Help",
			"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	
	public MenuState(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		
		try {
			
			background = new Background("/Backgrounds/grassbg1.gif",1);
			background.setVector(1, 0);
			
			titleColor = new Color(128,0,0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			font = new Font("Arial", Font.PLAIN	, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		background.update();
	}

	@Override
	public void draw(Graphics2D graphics) {
		// TODO Auto-generated method stub
		
		//Draw Background
		background.draw(graphics);
		
		//Draw Title
		graphics.setColor(titleColor);
		graphics.setFont(titleFont);
		textCenterDrawString(TITLE, graphics, 0, -40, titleFont);
		
		//Draw Menu Options
		graphics.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				graphics.setColor(Color.BLACK);
			}
			else {
				graphics.setColor(Color.RED);
			}
			textCenterDrawString(options[i], graphics, 0, 30 + (i*15), font);
		}
		
	}

	private void selectMenu() {
		switch(currentChoice) {
			case START:
				//Start
				gameStateManager.setState(GameStateManager.LEVEL1STATE);
				break;
			case HELP:
				//Help
				break;
			case QUIT:
				System.exit(0);
				break;
		}
	}
	
	@Override
	public void keyPressed(int key) {
		// TODO Auto-generated method stub
		if(key == KeyEvent.VK_ENTER) 
			selectMenu();
		if(key == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice < 0) currentChoice = options.length - 1;
		}
		if(key == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice >= options.length) currentChoice = 0;
		}
	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub
		
	}
	
	
	//Draw string at the center of screens
	private void textCenterDrawString(String text, Graphics2D graphics, int xOffset, int yOffset, Font font) {

	    FontMetrics metrics = graphics.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = (GamePanel.WIDTH - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = (GamePanel.HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent();
		graphics.drawString(text, x + xOffset, y + yOffset);
		
	}

}
