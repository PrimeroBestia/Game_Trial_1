package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Slow extends Enemy{

	private BufferedImage[] sprites;

	public Slow(TileMap tileMap) {
		super(tileMap);

		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10;

		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;

		health = maxHealth = 2;
		damage = 1;
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/slugger.gif"));
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i ++)
				sprites[i] = spriteSheet.getSubimage(i * width, 0, width, height);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);

		right = true;

	}

	public void getNextPosition() {


		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}

		if(falling) {
			dy += fallSpeed;
		}
	}

	public void update() {

		if(notOnScreen()) return;
		//Update new Position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);

		if(flinching) {
			long elapsed = (System.nanoTime() -flinchTimer)/1000000;
			if(elapsed > 400)
				flinching = false;
		}
		if(dx == 0) {
			right = !right;
			left = !left;
			facingRight = !facingRight;
		}

		animation.update();

	}

	public void draw(Graphics2D graphics) {
		setMapPosition();
		System.out.println("Slow: x:" + (x+xmap) + " y:" + (y+ymap));
		if(notOnScreen()) return;
		if(flinching) {
			long elapsed =(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		super.draw(graphics);
	}
}
