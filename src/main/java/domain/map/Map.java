package domain.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domain.services.Utilities;
import domain.tower.Tower;

public class Map implements Serializable {
	// Overview: This class represents game world in which towers can be placed on lots, enemies move through a path
	// and projectiles go towards enemies
	//Map objects can be saved and changed through the Map Editor
	//A typical map consists 16x9 tiles which at least 4 of them are lot tiles, a starting tile at the bottom of the map, an ending tile at the top of the Map
	// and a single connected series of pathTiles that start from the starting tile and end at ending tile

	//Abstract Invariant: Starting and Ending tiles Must not be the same, starting tile is bottom of the map
	//ending tile is at the top of the map, single connected lines of path tiles connect starting and ending tiles
	//4 lot tiles must exist

	private static final long serialVersionUID = 1L;
	private List<Tower> towerList = new ArrayList<>();

	public String mapName; 			// Name of map used to read and write map
	
	public PathTile startingTile;	// Starting path tile
	public PathTile endingTile;		// Ending path tile
	
	public int height;
	public int width;

	private int lotCount = 0;
	private List<PathTile> path;
	public Tile[][] tileMap; //THE REP

	//Representation Invariant:
	// mapName != null
	// height > 0 && width > 0
	// staringTile != endingTile
	// path.get(0) == startingTile
	// path(path.size() - 1) == endingTile
	// each path tile is only connected to two other tiles (except starting and ending tiles)
	// top row of tileMap[][] must contain EndingTile &&
	// bottom row of tileMap[][] must contain StartingTile
	// Empty lotCount >= 4)
	//
 	//
	//Abstraction Function:
	//AF(this) = a Map where:
	// it has a mapName
	// height and width must be higher than 1
	// the start tile is located at tileMap[height - 1][x] for some x in between [0, 1, 2... ,width-1]
	// the end tile is located at grid[0][y] for some y in between [0, 1, 2... ,width-1]
    // there are at least 4 empty lot tiles
    // there is a unique sequence of adjacent (N/S/E/W) pathTiles that form an unbranching path
    // connecting the start and end tile


	//constructors
	public Map(String mapName, int height, int width) {
		//EFFECTS: Creates default map with all grass tiles
		this.mapName = mapName;
		this.height = height;
		this.width = width;
		
		initializeTileMap();
	}
	public Map(int height, int width) {
		this(null, height, width);
	}

	//Starting tile must be somewhere at the bottom end ending tile must be somewhere on the top
	//Types of these tiles are PATH, isWalkable = 1
	// DEPRICATED CONSTRUCTOR!!!
	public Map(String mapName, PathTile startingTile, PathTile endingTile, int height, int width) {
		this(mapName, height, width); // Default constructor
		if (true) return; // This is to trick the compiler so we don't have to delete the code below but also never use it
		// DEPRICATED BELOW!!!

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

	//methods
	public boolean repOK(){
		//copy of MapEditor.isValidMap()
        if (height < 1 || width < 1 || mapName == null){
			return false;
		}

		if (startingTile != null && endingTile != null) {
			if(startingTile.equals(endingTile)){
				return false;
			}

			int[] ds = locationToTileMap(endingTile.getLocation());
			if (ds[0] != 0) return false;

			int[] de = locationToTileMap(startingTile.getLocation());
			if (de[0] != height - 1) return false;
		}

		List<PathTile> path = Utilities.findPath(this);
		if (path != null) {
			if (path.get(0) != startingTile || path.get(path.size() - 1) != endingTile) return false;
		}

		if (lotCount < 4){
			return false;
		}

		return true;
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

	public void resetTowers() {
		// EFFECTS: Unsets all the targets of the towers currently present on the map
		for (Tower tower : towerList) {
			tower.setTarget(null);
		}
	}

	public List<PathTile> getPath() {
		if (path == null) setPath();
		return path;
	}

	public void setPath() {
		this.path = Utilities.findPath(this);
	}

	public PathTile getStartingTile() {
		return startingTile;
	}

	public void setStartingTile(PathTile startingTile) {
		// EFFECTS: If the given tile is correctly on the edge of the map, it is set as the starting tile
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
		// EFFECTS: If the tile is correctly on the edge of the map, it is set as the ending tile
		if(endingTile == null) {
			this.endingTile = null;
			return;
		}
		int[] d = locationToTileMap(endingTile.getLocation());
		if (d[0] != 0) return;
		
		this.endingTile = endingTile;
		tileMap[d[0]][d[1]] = endingTile;
	}

	public void addTower(Tower tower) {
		towerList.add(tower);
	}

	public void removeTower(Tower tower) {
		towerList.remove(tower);
	}

	public List<Tower> getTowerList() {
		return towerList;
	}

	public static void printMap(Map map) {
		for (int i = 0; i < map.height; i++) {
			for (int j = 0; j < map.width; j++) {
				System.out.print(map.tileMap[i][j].type.toString());
				System.out.print(map.tileMap[i][j].type.getClass());
			}
			System.out.print("\n");
		}
	}
	public static int[] locationToTileMap(Location location) {
		// REQUIRES: location is not out of bounds from the map
		// EFFECTS: Returns the tile map index corresponding to a domain Location coordinate
		int x;
		int y;
		
		x = (int) ((location.getXCoord() - Tile.tileLength * 0.5) / Tile.tileLength);
		y = (int) ((location.getYCoord() - Tile.tileLength * 0.5) / Tile.tileLength);
		
		return new int[]{y, x};
	}

	public int getLotCount() {
		return lotCount;
	}

	public void setLotCount(int value) {
		lotCount = value;
	}
}

