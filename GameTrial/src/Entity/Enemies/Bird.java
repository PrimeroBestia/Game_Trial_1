package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Bird extends Enemy{

	private BufferedImage[][] sprites;

	private int heightFly;
	private int widthFly;
	private int heightWalk;
	private int widthWalk;
	private int heightCrazy;
	private int widthCrazy;

	public Bird(TileMap tileMap) {
		super(tileMap);

		moveSpeed = 0.3;
		maxSpeed = 1.0;
		fallSpeed = 0.2;
		maxFallSpeed = 10;

		heightFly = 82;
		widthFly = 113;
		heightWalk = 49;
		widthWalk = 64;
		heightCrazy = 0;
		widthCrazy = 0;

		width = widthWalk;
		height = heightWalk;
		cwidth = 50;
		cheight = 50;

		health = maxHealth = 100;
		damage = 1;
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/bird.gif"));
			sprites = new BufferedImage[3][];
			sprites[0] = new BufferedImage[2];
			for(int i = 0; i < 2; i ++)
				sprites[0][i] = spriteSheet.getSubimage(i * widthWalk, 0, widthWalk, heightWalk);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites[0]);
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
		if(notOnScreen()) return;
		if(flinching) {
			long elapsed =(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		setMapPosition();
		super.draw(graphics);
	}
}
