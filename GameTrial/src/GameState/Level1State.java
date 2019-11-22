package GameState;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Slow;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState{

	private TileMap tileMap;
	private Background background;

	private Player player;

	private ArrayList<Enemy> enemies;

	private HUD hud;

	public Level1State(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		background = new Background("/Backgrounds/menubg.gif",0.1);
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.1);
		player = new Player(tileMap);
	}

	@Override
	public void init() {

		player.setPosition(100, 100);
		enemies = new ArrayList<Enemy>();
		Slow s = new Slow(tileMap);
		s.setPosition(150, 200);
		enemies.add(s);
		hud = new HUD(player);

	}

	@Override
	public void update() {
		try {
			player.update();
			tileMap.setPosition(GamePanel.WIDTH / 2  - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
			background.setPosition(tileMap.getx(), tileMap.gety());

			for(int i = 0; i < enemies.size(); i++) {
				enemies.get(i).update();
				player.fireBallHit(enemies.get(i));
				if(player.intersects(enemies.get(i))){
					player.takeDamage(enemies.get(i).getDamage());
				}
				if(enemies.get(i).isDead()){
					enemies.remove(i);
					i--;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
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

		//DrawPlayer
		if(player != null) {
			player.draw(graphics);
			if(player.isDead()){
				textCenterDrawString("Dead", graphics, 0, 0, font);
			}
		}
		for(Enemy e: enemies) {
			e.draw(graphics);
		}
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching(true);
		if(k == KeyEvent.VK_F) player.setFiring(true);
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
