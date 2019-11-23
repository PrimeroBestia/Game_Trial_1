package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import TileMap.TileMap;

public class Enemy extends MapObject{

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;

	protected boolean flinching;
	protected long flinchTimer;

	public Enemy(TileMap tileMap) {
		super(tileMap);
	}

	public boolean isDead() {
		return dead;
	}
	public int getDamage() {
		return damage;
	}

	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health <= 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}

	public void update() {

	}
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		Rectangle r = getHealthBar();
		int health = (int)(r.getWidth() * ((double)this.health / maxHealth));
		System.out.println(health + "" +this.health);
		graphics.setColor(Color.RED);
		graphics.fillRect((int)r.getX(),(int)r.getY(),health,(int)r.getHeight());
		graphics.setColor(Color.BLACK);
		graphics.drawRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
	}
}
