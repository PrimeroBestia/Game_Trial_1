package GameState;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState{
	
	private TileMap tileMap;
	private Background background;
	
	public Level1State(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}
	
	@Override
	public void init() {

		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);

		background = new Background("/Backgrounds/grassbg1.gif",0.1);
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D graphics) {

		//Draw Background
		background.draw(graphics);
		
		//Draw Tilemap
		tileMap.draw(graphics);
		
	}

	@Override
	public void keyPressed(int key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub
		
	}

}
