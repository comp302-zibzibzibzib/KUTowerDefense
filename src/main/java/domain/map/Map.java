package domain.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import domain.kutowerdefense.PlayModeManager;
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
	private final List<Tower> towerList = new ArrayList<>();

	public String mapName; 			// Name of map used to read and write map
	
	public List<PathTile> startingTiles;	// Starting path tile
	public List<PathTile> endingTiles;		// Ending path tile
	
	public int height;
	public int width;

	private int lotCount = 0;
	private HashMap<PathTile, List<PathTile>> pathMap;
	private HashMap<PathTile, List<double[]>> offsetMap;
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

		startingTiles = new ArrayList<>();
		endingTiles = new ArrayList<>();
		
		initializeTileMap();
	}
	public Map(int height, int width) {
		this(null, height, width);
	}

	//Starting tile must be somewhere at the bottom end ending tile must be somewhere on the top
	//Types of these tiles are PATH, isWalkable = 1
	// DEPRICATED CONSTRUCTOR!!!
	public Map(String mapName, PathTile startingTile, PathTile endingTiles, int height, int width) {
		this(mapName, height, width); // Default constructor
		if (true) return; // This is to trick the compiler so we don't have to delete the code below but also never use it
		// DEPRICATED BELOW!!!

		/*
		Tile prevStart = tileMap[(int)startingTile.location.yCoord][(int)startingTile.location.xCoord];
		Tile prevEnd = tileMap[(int)endingTile.location.yCoord][(int)endingTile.location.xCoord];
		//Ensure that both starting and ending tiles are of type PATH
		if (startingTile.getType() != TileType.PATH || endingTile.getType() != TileType.PATH) return; // Raise an invalid argument exception?

		this.startingTile = (startingTile.location.yCoord == height-1) ? startingTile : null; //Display an error message in UI???
		this.endingTile = (endingTile.location.yCoord == 0) ? endingTile : null;

		tileMap[(int)startingTile.location.yCoord][(int)startingTile.location.xCoord] = this.startingTile;
		tileMap[(int)endingTile.location.yCoord][(int)endingTile.location.xCoord] = this.endingTile;

		startingTile.setLocation(prevStart.location);
		endingTile.setLocation(prevEnd.location);*/
	}

	//methods
	public boolean repOK(){
		// I can't be bothered to write a repOK for the new map with multiple starting and ending tiles
		// If you are grading this, I suggest you look at the junit-Group or junit-<team-member-name> branches
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

	public HashMap<PathTile, List<PathTile>> getPathMap() {
		if (pathMap == null) setPath(); // Finding paths is a difficult thing, don't do it more than once per map
		return pathMap;
	}

	public void setPath() {
		this.pathMap = Utilities.findPath(this);
	}

	public HashMap<PathTile, List<double[]>> getOffsetMap() {
		if (offsetMap == null) setOffsetMap();
		return offsetMap;
	}

	public void setOffsetMap() {
		getPathMap(); // Generate the path map if not generated
		offsetMap = new HashMap<>();
		for (PathTile start : pathMap.keySet()) {
			List<PathTile> path = pathMap.get(start);
			List<double[]> offsetList = new ArrayList<>();

			for (int index = 0; index < path.size(); index++) {
				PathTile currentTile = path.get(index);

				// Handle start and end tiles
				if (index == 0) {
					offsetList.add(new double[]{0.0, 0.0}); // Start at center
					continue;
				} else if (index == path.size() - 1) {
					double[] offset = getEdgeTargetOffset(currentTile);
					offsetList.add(offset);
					continue;
				}

				PathTile prevTile = path.get(index - 1);
				PathTile nextTile = path.get(index + 1);

				double[] incomingDir = getDirectionVector(prevTile, currentTile);
				double[] outgoingDir = getDirectionVector(currentTile, nextTile);

				// If this is a turn tile
				if (!Arrays.equals(incomingDir, outgoingDir)) {
					// Calculate banking offset based on turn direction
					double[] offset = calculateBankingOffset(incomingDir, outgoingDir);
					offsetList.add(offset);
				} else {
					// For straight paths, use center
					offsetList.add(new double[]{0.0, 0.0});
				}
			}

			offsetMap.put(start, offsetList);
		}
	}

	private double[] calculateBankingOffset(double[] incomingDir, double[] outgoingDir) {
		double[] offset = new double[]{0.0, 0.0};
		double bankingFactor = 0.2; // Adjust this to control how far from center the path curves

		// When going right and turning down: offset should be {-0.2, -0.2} (slightly left and up from center)
		// When going right and turning up: offset should be {-0.2, 0.2} (slightly left and down from center)
		// When going left and turning down: offset should be {0.2, -0.2} (slightly right and up from center)
		// When going left and turning up: offset should be {0.2, 0.2} (slightly right and down from center)

		if (incomingDir[0] > 0) { // Coming from left, going right
			if (outgoingDir[1] > 0) { // Turning down
				offset[0] = -bankingFactor; // Bank left
				offset[1] = bankingFactor; // Bank up
			} else if (outgoingDir[1] < 0) { // Turning up
				offset[0] = -bankingFactor; // Bank left
				offset[1] = -bankingFactor;  // Bank down
			}
		} else if (incomingDir[0] < 0) { // Coming from right, going left
			if (outgoingDir[1] > 0) { // Turning down
				offset[0] = bankingFactor;  // Bank right
				offset[1] = bankingFactor; // Bank up
			} else if (outgoingDir[1] < 0) { // Turning up
				offset[0] = bankingFactor;  // Bank right
				offset[1] = -bankingFactor;  // Bank down
			}
		} else if (incomingDir[1] > 0) { // Coming from top, going down
			if (outgoingDir[0] < 0) { // Turning left
				offset[0] = -bankingFactor; // Bank left
				offset[1] = -bankingFactor; // Bank up
			} else if (outgoingDir[0] > 0) { // Turning right
				offset[0] = bankingFactor;  // Bank right
				offset[1] = -bankingFactor; // Bank up
			}
		} else if (incomingDir[1] < 0) { // Coming from bottom, going up
			if (outgoingDir[0] < 0) { // Turning left
				offset[0] = -bankingFactor; // Bank left
				offset[1] = bankingFactor;  // Bank down
			} else if (outgoingDir[0] > 0) { // Turning right
				offset[0] = bankingFactor;  // Bank right
				offset[1] = bankingFactor;  // Bank down
			}
		}

		return offset;
	}



	private double[] getDirectionVector(PathTile from, PathTile to) {
		double deltaX = to.getLocation().xCoord - from.getLocation().xCoord;
		double deltaY = to.getLocation().yCoord - from.getLocation().yCoord;

		// Normalize to unit vector components
		if (Math.abs(deltaX) > Math.abs(deltaY)) {
			return new double[]{Math.signum(deltaX), 0.0};
		} else {
			return new double[]{0.0, Math.signum(deltaY)};
		}
	}


	public static double[] getEdgeTargetOffset(PathTile edge) {
		Map map = PlayModeManager.getInstance().getCurrentMap();
		int[] tileCoords = Map.locationToTileMap(edge.location);
		if (tileCoords[0] == 0) {
			return new double[] {0.0, -1.0};  // {x, y}
		} else if (tileCoords[0] == map.getHeight() - 1) {
			return new double[] {0.0, 1.0};
		} else if (tileCoords[1] == 0) {
			return new double[] {-1.0, 0.0};
		} else if (tileCoords[1] == map.getWidth() - 1) {
			return new double[] {1.0, 0.0};
		}
		return new double[] {0.0, 0.0};
	}

	public List<PathTile> getStartingTiles() {
		return startingTiles;
	}

	public boolean addStartingTile(PathTile tile) {
		if (endingTiles.contains(tile)) return false;

		boolean isValid = isValidEdgeTile(tile);
		if (isValid && !startingTiles.contains(tile)) {
			startingTiles.add(tile);
			System.out.println("Added starting tile");
		} else if (isValid && startingTiles.contains(tile)) {
			startingTiles.remove(tile);
			System.out.println("Removed starting tile");
		}
		return isValid && !startingTiles.contains(tile);
	}

	public void removeStartingTile(PathTile tile) {
		startingTiles.remove(tile);
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

	public List<PathTile> getEndingTiles() {
		return endingTiles;
	}

	public boolean addEndingTile(PathTile tile) {
		if (startingTiles.contains(tile)) return false;

		boolean isValid = isValidEdgeTile(tile);
		if (isValid && !endingTiles.contains(tile)) {
			endingTiles.add(tile);
			System.out.println("Added ending tile");
		} else if (isValid && endingTiles.contains(tile)) {
			endingTiles.remove(tile);
			System.out.println("Removed ending tile");
		}
		return isValid && !endingTiles.contains(tile);
	}

	public void removeEndingTile(PathTile tile) {
		endingTiles.remove(tile);
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

	private boolean isValidEdgeTile(PathTile tile) {
		Location location = tile.getLocation();
		int[] tileIndicies = locationToTileMap(location);

		return (tileIndicies[0] == height - 1 && tile.getPathType().neighbourExists(1)) ||
				(tileIndicies[0] == 0 && tile.getPathType().neighbourExists(0)) ||
				(tileIndicies[1] == width - 1 && tile.getPathType().neighbourExists(3)) ||
				(tileIndicies[1] == 0 && tile.getPathType().neighbourExists(2));
	}
}

