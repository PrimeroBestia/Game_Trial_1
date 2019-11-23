package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Bird;
import Entity.Enemies.Slow;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState{

	private TileMap tileMap;
	private Background background;
	private long blinkTime;
	private boolean bossStarted;

	private Player player;

	private ArrayList<Enemy> enemies;
	private Enemy boss;
	private ArrayList<Explosion> explosions;

	private HUD hud;

	public Level1State(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}

	@Override
	public void init() {
		Point[] points = new Point[] {
				new Point(860, 200),
				new Point(1525, 200),
				new Point(1680, 200),
				new Point(1800, 200),
				new Point(150, 200)
		};
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		background = new Background("/Backgrounds/menubg.gif",0.1);
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.1);
		player = new Player(tileMap);
		player.setPosition(100, 100);
		player.setDead(false);
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		boss = new Bird(tileMap);
		boss.setPosition(3100, 100);
		for(Point p : points) {
			Slow s = new Slow(tileMap);
			s.setPosition(p.getX(), p.getY());
			enemies.add(s);
		}
		hud = new HUD(player);
		blinkTime = System.nanoTime();
		bossStarted = false;

	}

	@Override
	public void update() {
		try {
			player.update();
			background.setPosition(tileMap.getx(), tileMap.gety());
			
			if(player.getx() > 2950 && !bossStarted && !boss.isDead()) {
				bossStarted = true;
				System.out.println("boss");
				int tilex1 = (int)(2900) / 30;
				int tilex2 = (int)(2930) / 30;
				int tiley1 = (int)(170) / 30;
				int tiley2 = (int)(200) / 30;
				System.out.println(tilex1 + " " + tiley1);
				tileMap.changeType(tiley1, tilex1, 22);
				tileMap.changeType(tiley1, tilex2, 4);
				tileMap.changeType(tiley2, tilex1, 22);
				tileMap.changeType(tiley2, tilex2, 4);
				tileMap.setPosition(GamePanel.WIDTH / 2  - 4000, GamePanel.HEIGHT / 2 - player.gety());
			}
			else if(!bossStarted)
				tileMap.setPosition(GamePanel.WIDTH / 2  - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
			
			for(int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				e.update();
				player.fireBallHit(e);
				player.scratchHit(e);
				if(player.intersects(e)){
					player.takeDamage(e.getDamage());
				}
				if(e.isDead()){
					Explosion explode = new Explosion(tileMap,e.getWidth(),e.getHeight());
					explode.setPosition(e.getx(), e.gety());
					explosions.add(explode);
					enemies.remove(i);
					i--;
				}
			}
			if(bossStarted) {
				boss.update();
				player.fireBallHit(boss);
				player.scratchHit(boss);
				if(player.intersects(boss)){
					player.takeDamage(boss.getDamage());
				}
				if(boss.isDead()){
					Explosion explode = new Explosion(tileMap,boss.getWidth(),boss.getHeight());
					explode.setPosition(boss.getx(), boss.gety());
					explosions.add(explode);
					bossStarted = false;
				}
			}
			for(int i = 0; i < explosions.size(); i++){
				Explosion e = explosions.get(i);
				e.update();
				if(e.shouldRemove()){
					explosions.remove(i);
					i--;
				}
			}

		}
		catch(Exception e) {
		}

	}

	@Override
	public void draw(Graphics2D graphics) {

		Font font = new Font("Arial", Font.PLAIN, 28);
		//Draw Background
		background.draw(graphics);

		//Draw Tilemap
		tileMap.draw(graphics);

		//HUD draw
		hud.draw(graphics);

		//Draw enemy
		for(Enemy e: enemies) {
			e.draw(graphics);
		}
		//Draw Explosion
		for(Explosion e: explosions) {
			e.draw(graphics);
		}
		if(bossStarted && !boss.isDead()) {
			boss.draw(graphics);
		}
		//DrawPlayer
		if(player == null) return;
		player.draw(graphics);
		if(player.isDeathPlayed()){
			graphics.setColor(Color.RED);
			textCenterDrawString("You are dead", graphics, 0, -20, font);
			long elapsed =(System.nanoTime() - blinkTime) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
			textCenterDrawString("Press Any Key to reset", graphics, 0, 10, font);
		}
	}

	@Override
	public void keyPressed(int k) {
		if(player.isDeathPlayed())
			gameStateManager.setState(GameStateManager.MENUSTATE);
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();
		if(k == KeyEvent.VK_R) player.setDead(false);
	}

	@Override
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}

}
