package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	
	public Background(String src, double moveScale) {
		
		try {
			image = ImageIO.read(
						getClass().getResourceAsStream(src)
					);	
			this.moveScale = moveScale;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	

	public void update() {
		x += dx;
		y += dy;
	}

	public void draw(Graphics2D graphics) {
		
		graphics.drawImage(image, (int)x, (int)y, null);
		if(x < 0) {
			x +=  GamePanel.WIDTH;
		}
		else if(x > 0) {
			x -= GamePanel.WIDTH;
		}
		if(y < 0) {
			y +=  GamePanel.HEIGHT;
		}
		else if(y > 0) {
			y -= GamePanel.HEIGHT;
		}
		graphics.drawImage(image, (int)x, (int)y, null);
		
	}

}
