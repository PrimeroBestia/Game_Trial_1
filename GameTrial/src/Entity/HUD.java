package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD {

	private Player player;
	private BufferedImage image;
	private Font font;
	public HUD(Player p) {
		player = p;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud.gif"));
			font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D graphics) {

		graphics.drawImage(image, 0, 10, null);
		graphics.setFont(font);
		graphics.setColor(Color.RED);
		graphics.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
		graphics.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 45);

	}
}
