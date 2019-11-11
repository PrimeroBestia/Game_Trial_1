package GameState;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState{

	private TileMap tileMap;
	private Background background;

	private Player player;

	public Level1State(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		background = new Background("/Backgrounds/menubg.gif",0.1);
	}

	@Override
	public void init() {

		player = new Player(tileMap);
		player.setPosition(100, 100);

	}

	@Override
	public void update() {

		if(player != null) {
			player.update();
			if(!player.isDead()) 
				tileMap.setPosition(GamePanel.WIDTH / 2  - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
			else
				player.setPosition(player.getx(), GamePanel.HEIGHT + 30);
			if(player.notOnScreen())
				player.setDead(true);
		}

	}

	@Override
	public void draw(Graphics2D graphics) {

		Font font = new Font("Arial", Font.PLAIN, 28);
		//Draw Background
		background.draw(graphics);

		//Draw Tilemap
		tileMap.draw(graphics);

		//DrawPlayer
		if(player != null) {
			player.draw(graphics);
			if(player.isDead()){
				textCenterDrawString("Dead", graphics, 0, 0, font);
			}
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
	}

	@Override
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
		if(k == KeyEvent.VK_R) player.setScratching(false);
		if(k == KeyEvent.VK_F) player.setFiring(false);
	}

}
