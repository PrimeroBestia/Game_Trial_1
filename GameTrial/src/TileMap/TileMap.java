package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import Entity.Animation;
import Main.GamePanel;

public class TileMap {
	
	//Position
	private double x;
	private double y;
	
	//Bound
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	//Camera Tween
	private double tween;
	
	//Map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	//Tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize) {
		
		this.tileSize = tileSize;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		tween = 0.07;
		
	}
	
	public void loadTiles(String src) {
		try {
			tileset = ImageIO.read(getClass().getResourceAsStream(src));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage,Tile.NORMAL);
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage,Tile.BLOCKED);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String src) {
		
		try {
			
			InputStream input = getClass().getResourceAsStream(src);
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(input));
			
			numCols = Integer.parseInt(buffReader.readLine());
			numRows = Integer.parseInt(buffReader.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delimit = "\\s+";
			
			for(int row = 0; row < numRows; row++) {
				String line = buffReader.readLine();
				String[] tokens = line.split(delimit);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getTileSize() {return tileSize;}
	public int getx() {return (int)x;}
	public int gety() {return (int)y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	public int getType(int row, int col) {
		int rowCol = map [row][col];
		int rows = rowCol / numTilesAcross;
		int cols = rowCol % numTilesAcross;
		
		return tiles[rows][cols].getType();
	}
	
	public void setTween(double d) { tween = d; }
	
	public void setPosition(double x, double y) {
		
		this.x = x  * tween;
		this.y = y  * tween;
		fixBounds();
		
		colOffset = (int)-this.x /tileSize;
		rowOffset = (int)-this.y /tileSize;
		
	}
	
	public void fixBounds() {
		
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
		
	}
	
	public void draw(Graphics2D graphics) {
		
		try {
			for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
				
				if(row >= numRows) break;
	
				for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
					if(col >= numCols) break;
	
					if(map[row][col] == 0) continue;
					int rowCol = map [row][col];
					int rows = rowCol / numTilesAcross;
					int cols = rowCol % numTilesAcross;
					graphics.drawImage(tiles[rows][cols].getImage(), (int)x + (col * tileSize),
							(int)y + (row * tileSize), null);
				}
					
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
