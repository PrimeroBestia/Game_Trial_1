package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Player extends MapObject {

	//Player Stats
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private long flinchTime;

	//Fire ball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;

	//Scratch
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	//Gliding
	private boolean gliding;

	//Animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
			2, 8, 1, 2, 4, 2, 5
	};


	//Action animation
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;


	public Player(TileMap tileMap) {
		super(tileMap);

		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;

		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;
		flinchTimer = 100;
		flinchTime = 0;

		health = maxHealth = 5;
		fire = maxFire = 2500;

		fireCost = 200;
		fireBallDamage = 1;
		fireBalls = new ArrayList<FireBall>();

		scratchDamage = 8;
		scratchRange = 40;

		dead = false;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < numFrames.length; i ++) {
				BufferedImage[] images = new BufferedImage[numFrames[i]];

				for(int j = 0; j < numFrames[i]; j++) {
					if(i != SCRATCHING) {
						images[j] = spriteSheet.getSubimage(j * width, i * height, width, height);
					}
					else {
						images[j] = spriteSheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}
				}

				sprites.add(images);

			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);

	}


	public int getFire() {return fire;}
	public int getMaxFire() {return maxFire;}
	public int getHealth() {return health;}
	public int getMaxHealth() {return maxHealth;}
	public boolean isDead() {return dead;}

	public void setFiring(boolean b) {
		firing = b;
	}

	public void setScratching(boolean b) {
		scratching = b;
	}

	public void setGliding(boolean b) {
		gliding = b;
	}

	public void setDead(boolean b) {
		dead = b;
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) dx = 0;
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) dx = 0;
			}
		}

		if((currentAction == SCRATCHING || currentAction == FIREBALL)
				&& !(jumping || falling)) dx = 0;
		//Jumping
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		if(falling) {

			if(dy > 0 && gliding) dy+= fallSpeed * 0.005;
			else dy += fallSpeed;

			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;

			if(dy > maxFallSpeed) dy = maxFallSpeed;

		}

	}

	public void takeDamage(int damage) {
		if(!flinching){
			if(facingRight)dx = -5;
			else dx = 5;
			health -= damage;
			flinching = true;
			flinchTime = System.nanoTime();
		}
		if(health <= 0) dead = true;
	}


	public void fireBallHit(Enemy enemies) {
		for(int i = 0; i < fireBalls.size(); i++){
			if(enemies.intersects(fireBalls.get(i))){
				enemies.hit(fireBallDamage);
				fireBalls.get(i).setHit();
			}
		}
	}

	public void update() {

		//Update new Position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);

		//Check Attack animation
		if(currentAction == SCRATCHING)
			if(animation.hasPlayedOnce()) scratching = false;
		if(currentAction == FIREBALL)
			if(animation.hasPlayedOnce()) firing = false;

		//FireBall attack
		fire += 1;
		if(fire > maxFire) fire = maxFire;

		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		//set animation
		if(scratching) {
			if(currentAction != SCRATCHING) {
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {

				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;

				if(fire > fireCost) {
					fire -= fireCost;
					FireBall fb = new FireBall(tileMap, facingRight);
					fb.setPosition(x, y);
					fireBalls.add(fb);
				}

			}
		}
		else if (dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if (dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		if(flinching){
			if((System.nanoTime() - flinchTime) / 1000000 > 1000){
				flinching = false;
			}
		}
		animation.update();

		//set Direction
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}

	}

	public void draw(Graphics2D graphics) {
		setMapPosition();
		for(FireBall fb: fireBalls) {
			fb.draw(graphics);
		}
		//draw player
		if(flinching) {
			long elapsed =(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}

		super.draw(graphics);

	}


}
