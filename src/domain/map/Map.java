package domain.map;

import java.io.Serializable;
import java.util.List;

import domain.services.Utilities;

public class Map implements Serializable {
	private static final long serialVersionUID = 1L;

	public String mapName; 			// Name of map used to read and write map
	
	public PathTile startingTile;	// Starting path tile
	public PathTile endingTile;		// Ending path tile
	
	public int height;
	public int width;
	
	private List<PathTile> path;
	
	public List<PathTile> getPath() {
		if (path == null) setPath();
		return path;
	}

	public void setPath() {
		this.path = Utilities.findPath(this);
	}
	public Tile[][] tileMap;
	
	//Default map with all grass tiles
	public Map(String mapName, int height, int width) {
		this.mapName = mapName;
		this.height = height;
		this.width = width;
		
		initializeTileMap();
	}
	
	//Starting tile must be somewhere at the bottom end ending tile must be somewhere on the top
	//Types of these tiles are PATH, isWalkable = 1
	// DEPRICATED CONSTRUCTOR!!!
	public Map(String mapName, PathTile startingTile, PathTile endingTile, int height, int width) {
		this(mapName, height, width); // Default constructor
		if (true) return; // DEPRICATED BELOW!!!
		
		Tile prevStart = tileMap[(int)startingTile.location.yCoord][(int)startingTile.location.xCoord];
		Tile prevEnd = tileMap[(int)endingTile.location.yCoord][(int)endingTile.location.xCoord];
		//Ensure that both starting and ending tiles are of type PATH
		if (startingTile.getType() != TileType.PATH || endingTile.getType() != TileType.PATH) return; // Raise an invalid argument exception?
		
		this.startingTile = (startingTile.location.yCoord == height-1) ? startingTile : null; //Display an error message in UI???
		this.endingTile = (endingTile.location.yCoord == 0) ? endingTile : null; 
		
		tileMap[(int)startingTile.location.yCoord][(int)startingTile.location.xCoord] = this.startingTile;
		tileMap[(int)endingTile.location.yCoord][(int)endingTile.location.xCoord] = this.endingTile;
		
		startingTile.setLocation(prevStart.location);
		endingTile.setLocation(prevEnd.location);
	}
	
	/**
	 * Initializes map to empty grass tile map
	 */
	private void initializeTileMap() {
		double y = 0;
		double x = 0;
		this.tileMap = new Tile[height][width];
		for (int i = 0; i < height; i++) {
			y += Tile.tileLength * 0.5;
			x = 0;
			for (int j = 0; j < width; j++) {
				if(tileMap[i][j] == null) {
					x += Tile.tileLength * 0.5;
					Location location = new Location(x, y);
					
					Tile tile = new Tile(location);
					tileMap[i][j] = tile;
					
					x += Tile.tileLength * 0.5;
				}
			}
			
			y += Tile.tileLength * 0.5;
		}	
	}
	public PathTile getStartingTile() {
		return startingTile;
	}

	public void setStartingTile(PathTile startingTile) {
		if(startingTile == null) {
			this.startingTile = null;
			return;
		}
		int[] d = locationToTileMap(startingTile.getLocation());
		if (d[0] != height - 1) return;
		
		this.startingTile = startingTile;
		tileMap[d[0]][d[1]] = startingTile;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public PathTile getEndingTile() {
		return endingTile;
	}

	public void setEndingTile(PathTile endingTile) {
		if(endingTile == null) {
			this.endingTile = null;
			return;
		}
		int[] d = locationToTileMap(endingTile.getLocation());
		if (d[0] != 0) return;
		
		this.endingTile = endingTile;
		tileMap[d[0]][d[1]] = endingTile;
	}
	
	public static void printMap(Map map) {
		for (int i = 0; i < map.height; i++) {
			for (int j = 0; j < map.width; j++) {
				System.out.print(map.tileMap[i][j].type.toString());
			}
			System.out.print("\n");
		}
	}
	public static int[] locationToTileMap(Location location) {
		int x;
		int y;
		
		x = (int) ((location.getXCoord() - Tile.tileLength * 0.5) / Tile.tileLength);
		y = (int) ((location.getYCoord() - Tile.tileLength * 0.5) / Tile.tileLength);
		
		return new int[]{y, x};
	}
}

