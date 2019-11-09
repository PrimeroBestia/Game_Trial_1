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
		
		if(graphics == null) {
			System.out.println("Graphics NUll");
			System.exit(0);
		}
		
		graphics.drawImage(image, (int)x, (int)y, null);
		if(x < 0) {
			graphics.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
		}
		if(x > 0) {
			graphics.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
		}
	}

}
