package Entity.Enemies;

import java.awt.Graphics2D;
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
	//private int heightCrazy;
	//private int widthCrazy;

	private long flightTimer;
	//private long walkTimer;
	private boolean flying;
	private boolean walking;

	private final int WALKING = 0;
	private final int FLYING = 1;
	//private final int ATTACK = 2;
	private int currentAction;

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
		//heightCrazy = 0;
		//widthCrazy = 0;

		walking = false;
		flying = true;
		flightTimer = System.nanoTime();

		width = widthFly;
		height = heightFly;
		cwidth = 50;
		cheight = 50;

		health = maxHealth = 100;
		damage = 1;

		currentAction = FLYING;
		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/bird.gif"));
			sprites = new BufferedImage[3][];
			sprites[0] = new BufferedImage[2];
			sprites[1] = new BufferedImage[3];
			for(int i = 0; i < 2; i ++)
				sprites[0][i] = spriteSheet.getSubimage(i * widthWalk, 0, widthWalk, heightWalk);
			for(int i = 0; i < 3; i ++)
				sprites[1][i] = spriteSheet.getSubimage(i * widthFly, heightWalk, widthFly, heightFly);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites[currentAction]);
		animation.setDelay(300);

		right = true;

	}

	public void getNextPosition() {

		if(currentAction == FLYING) dx = 0;
		else if(left) {
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

		if(falling && currentAction == WALKING)
			dy += fallSpeed;
		else
			dy = 0;
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
		if(dx == 0 && currentAction == WALKING) {
			right = !right;
			left = !left;
			facingRight = !facingRight;
		}
		if(flying){
			if(currentAction != FLYING){
				flightTimer = System.nanoTime();
				currentAction = FLYING;
				width = widthFly;
				height = heightFly;
				animation.setFrames(sprites[FLYING]);
				animation.setDelay(400);
			}
			long elapsed = (System.nanoTime() - flightTimer) / 1000000;
			if (elapsed > 5000){
				walking = true;
				flying = false;
			}
		}
		if(walking){
			if(currentAction != WALKING){
				currentAction = WALKING;
				width = widthWalk;
				height = heightWalk;
				animation.setFrames(sprites[WALKING]);
				animation.setDelay(400);
			}
		}
		animation.update();

	}

	public void draw(Graphics2D graphics) {
		setMapPosition();
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
