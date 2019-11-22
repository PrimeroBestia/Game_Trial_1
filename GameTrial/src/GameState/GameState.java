package GameState;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import Main.GamePanel;

public abstract class GameState {
	public GameStateManager gameStateManager;

	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D graphics);
	public abstract void keyPressed(int key);
	public abstract void keyReleased(int key);


	//Draw string at the center of screens
	public void textCenterDrawString(String text, Graphics2D graphics, int xOffset, int yOffset, Font font) {

	    FontMetrics metrics = graphics.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = (GamePanel.WIDTH - metrics.stringWidth(text)/2) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = (GamePanel.HEIGHT - metrics.getHeight()/2) / 2 + metrics.getAscent();
		graphics.drawString(text, x + xOffset, y + yOffset);

	}
}
