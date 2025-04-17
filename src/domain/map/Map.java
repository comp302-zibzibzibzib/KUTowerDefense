package domain.map;

import java.io.Serializable;

public class Map implements Serializable {
	private static final long serialVersionUID = 1L;

	public String mapName; 			// Name of map used to read and write map
	
	public PathTile startingTile;	// Starting path tile
	public PathTile endingTile;		// Ending path tile
	
	public int height;
	public int width;
	
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
	public Map(String mapName, PathTile startingTile, PathTile endingTile, int height, int width) {
		this(mapName, height, width); // Default constructor
		
		//Ensure that both starting and ending tiles are of type PATH
		if (startingTile.getType() != TileType.PATH || endingTile.getType() != TileType.PATH) return; // Raise an invalid argument exception?
		
		this.startingTile = (startingTile.location.yCoord == height-1) ? startingTile : null; //Display an error message in UI???
		this.endingTile = (endingTile.location.yCoord == 0) ? endingTile : null; 
		
		tileMap[(int)startingTile.location.yCoord][(int)startingTile.location.xCoord] = this.startingTile;
		tileMap[(int)endingTile.location.yCoord][(int)endingTile.location.xCoord] = this.endingTile;
	}
	
	/**
	 * Initializes map to empty grass tile map
	 */
	private void initializeTileMap() {
		this.tileMap = new Tile[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(tileMap[i][j] == null) {
					Tile tile = new Tile();
					tileMap[i][j] = tile;
				}
			}
		}	
	}
	public PathTile getStartingTile() {
		return startingTile;
	}

	public void setStartingTile(PathTile startingTile) {
		this.startingTile = startingTile;
	}

	public PathTile getEndingTile() {
		return endingTile;
	}

	public void setEndingTile(PathTile endingTile) {
		this.endingTile = endingTile;
	}
	
	public static void printMap(Map map) {
		for (int i = 0; i < map.height; i++) {
			for (int j = 0; j < map.width; j++) {
				System.out.print(map.tileMap[i][j].type.toString());
			}
			System.out.print("\n");
		}
	}
}

