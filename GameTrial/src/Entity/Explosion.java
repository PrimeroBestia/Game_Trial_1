package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Explosion extends MapObject{

	private boolean remove;
	private BufferedImage[] sprites;

	public Explosion(TileMap tileMap, int width, int height) {
		super(tileMap);
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/explosion.gif"));
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i ++)
				sprites[i] = spriteSheet.getSubimage(i * 30, 0, 30, 30);

			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
		catch(Exception e){

		}
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void update(){
		animation.update();
		remove = animation.hasPlayedOnce();
	}

	public void draw(Graphics2D graphics){
		setMapPosition();
		super.draw(graphics);
	}
}
