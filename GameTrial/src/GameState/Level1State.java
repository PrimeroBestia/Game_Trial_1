package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Slow;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState{

	private TileMap tileMap;
	private Background background;
	private long blinkTime;

	private Player player;

	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;

	private HUD hud;

	public Level1State(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}

	@Override
	public void init() {

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
		Slow s = new Slow(tileMap);
		s.setPosition(150, 200);
		enemies.add(s);
		hud = new HUD(player);
		blinkTime = System.nanoTime();

	}

	@Override
	public void update() {
		try {
			player.update();
			tileMap.setPosition(GamePanel.WIDTH / 2  - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
			background.setPosition(tileMap.getx(), tileMap.gety());

			for(int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				e.update();
				player.fireBallHit(e);
				player.scratchHit(e);
				if(player.intersects(e)){
					player.takeDamage(e.getDamage());
				}
				if(e.isDead()){
					Explosion explode = new Explosion(tileMap);
					explode.setPosition(e.getx(), e.gety());
					explosions.add(explode);
					enemies.remove(i);
					i--;
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
