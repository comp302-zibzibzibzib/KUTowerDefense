package domain.map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import domain.kutowerdefense.PlayModeManager;
import domain.services.Utilities;
import domain.tower.*;

public class MapEditor implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean isStartingTilePlaced = false;
	private boolean isEndingTilePlaced = false;
	private int lotCount = 0;

	public Map map;

	public MapEditor(Map map) {
		this.map = map;
	}

	public void addStartingTile(int height, int width) {
		Tile tile = map.tileMap[height][width];
		if (!(tile instanceof PathTile)) return;

		PathTile pathTile = (PathTile) tile;
		map.addStartingTile(pathTile);

		isStartingTilePlaced = !map.getStartingTiles().isEmpty();
	}

	public void addEndingTile(int height, int width) {
		Tile tile = map.tileMap[height][width];
		if (!(tile instanceof PathTile)) return;

		PathTile pathTile = (PathTile) tile;
		map.addEndingTile(pathTile);

		isEndingTilePlaced = !map.getEndingTiles().isEmpty();
	}

	public void placeTile(TileType type, int height, int width) {
		try {
			if (type == TileType.PATH || type == TileType.TOWER || type == TileType.DECORATIVES) {
			    System.out.println("Error: Wrong method. Use the correct placeTile overload!");
			    return;
			}
			int x = width;
			int y = height;
			Tile existingTile = map.tileMap[y][x];

			// Check whether a tile is already placed at that position
			if (existingTile != null && existingTile.getType() != TileType.GRASS) {
				System.out.println("Error: Cannot place tile on top of another tile. Remove it first.");
				return; // Cancel placement
			}
			double tileCoordX = Tile.tileLength * (x + 0.5);
			double tileCoordY = Tile.tileLength * (y + 0.5);
			
			// Special case: Castle occupies 4 spaces
			if (type.equals(TileType.CASTLE)) {
				if (!(x >= 0 && y >= 0 && x + 1 < map.width && y + 1 < map.height)) {
					System.out.println("Error: Please enter a valid location for Castle!"); // Invalid positions for Castle
				}
				if (map.tileMap[y][x].type != TileType.GRASS || map.tileMap[y][x + 1].type != TileType.GRASS
						|| map.tileMap[y + 1][x].type != TileType.GRASS
						|| map.tileMap[y + 1][x + 1].type != TileType.GRASS) {
					System.out.println("Error: Cannot place castle on occupied tiles. Clear them first."); // One of the four tiles are occupied, cannot place Castle
					return;
				}
				// Place 2x2 castle
				map.tileMap[y][x] = new Tile(TileType.CASTLE,
						new Location(Tile.tileLength * (x + 0.5), Tile.tileLength * (y + 0.5))); // Top Left

				map.tileMap[y][x + 1] = new Tile(TileType.CASTLE,
						new Location(Tile.tileLength * (x + 1.5), Tile.tileLength * (y + 0.5))); // Top Right

				map.tileMap[y + 1][x] = new Tile(TileType.CASTLE,
						new Location(Tile.tileLength * (x + 0.5), Tile.tileLength * (y + 1.5))); // Bottom Right

				map.tileMap[y + 1][x + 1] = new Tile(TileType.CASTLE,
						new Location(Tile.tileLength * (x + 1.5), Tile.tileLength * (y + 1.5))); // Bottom Left
			}
			Location newTileLocation = new Location(tileCoordX, tileCoordY);
			Tile tileToPlace;
			tileToPlace = new Tile(type, newTileLocation);
			if (type.equals(TileType.LOT)) {
				lotCount++; // Special case: Increment lotCount for lot tiles (requirement for a valid map)
				map.setLotCount(lotCount);
				Lot lot = new Lot(newTileLocation); // IMPORTANT: Need some kind of wrapping to access the methods of LOT in-game, alternative implementations are welcomed.
				lot.originallyEmpty(true);
				tileToPlace = lot;
			}

			map.tileMap[y][x] = tileToPlace;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: Please enter a valid location!");
			System.exit(-1); // Won't be possible in the UI can be discarded
		}

	}

	public void placeTile(TileType type, PathType pType, int height, int width) {
		try {
			if (type != TileType.PATH) {
			    System.out.println("Error: Wrong method. Use the correct placeTile overload!");
			    return;
			}
			int x = width;
			int y = height;

			Tile existingTile = map.tileMap[y][x];

			// Check whether a tile is already placed at that position
			if (existingTile != null && existingTile.getType() != TileType.GRASS) {
				System.out.println("Error: Cannot place tile on top of another tile. Remove it first.");
				return; // Cancel placement
			}
			// Coordinates of tile to be placed
			double tileCoordX = Tile.tileLength * (x + 0.5);
			double tileCoordY = Tile.tileLength * (y + 0.5);
			

			Location newTileLocation = new Location(tileCoordX, tileCoordY);
			Tile tileToPlace;
			// Set neighbours (bidirectional) of path tiles
			PathTile newPathTile = new PathTile(pType, newTileLocation);
			tileToPlace = newPathTile;

			if (pType.neighbourExists(0) && y > 0 && map.tileMap[y - 1][x] instanceof PathTile) {
				PathTile up = (PathTile) map.tileMap[y - 1][x];
				if (up.getPathType().neighbourExists(1)) {
					newPathTile.setUp(up);
					up.setDown(newPathTile);
				}
			}
			if (pType.neighbourExists(1) && y < map.height - 1 && map.tileMap[y + 1][x] instanceof PathTile) {
				PathTile down = (PathTile) map.tileMap[y + 1][x];
				if (down.getPathType().neighbourExists(0)) {
					newPathTile.setDown(down);
					down.setUp(newPathTile);
				}
			}
			if (pType.neighbourExists(2) && x > 0 && map.tileMap[y][x - 1] instanceof PathTile) {
				PathTile left = (PathTile) map.tileMap[y][x - 1];
				if (left.getPathType().neighbourExists(3)) {
					newPathTile.setLeft(left);
					left.setRight(newPathTile);
				}
			}
			if (pType.neighbourExists(3) && x < map.width - 1 && map.tileMap[y][x + 1] instanceof PathTile) {
				PathTile right = (PathTile) map.tileMap[y][x + 1];
				if (right.getPathType().neighbourExists(2)) {
					newPathTile.setRight(right);
					right.setLeft(newPathTile);
				}
			}
			// Place the tile on the map
			map.tileMap[y][x] = tileToPlace;

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: Please enter a valid location!");
			System.exit(-1); // Won't be possible in the UI can be discarded
		}
	}
	
	public void placeTile(TileType type, TowerType tType, int height, int width) {
	    try {
	    	if (type != TileType.TOWER) {
			    System.out.println("Error: Wrong method. Use the correct placeTile overload!");
			    return;
			}
	        int x = width;
	        int y = height;

	        Tile existingTile = map.tileMap[y][x];

	        // Check if trying to place a Tower on something other than a Lot
	        if (existingTile != null && existingTile.getType() != TileType.GRASS && existingTile.getType() != TileType.LOT) {
				System.out.println("Error: Cannot place tile on top of another tile. Remove it first.");
				return; // Cancel placement
			}


	        // Create the Tower based on TowerType
	        Tower tower = switch (tType) {
	            case ARCHER -> ArcherTowerFactory.getInstance().createTower();
	            case MAGE -> MageTowerFactory.getInstance().createTower();
	            case ARTILLERY -> ArtilleryTowerFactory.getInstance().createTower();
	        };

	        // Set the location of the tower to match the Lot's location
			map.addTower(tower);
	        tower.setLocation(existingTile.getLocation());

	        // Place the Tower inside the Lot
			Lot lot;
			if (existingTile.getType() != TileType.LOT) {
				lot = new Lot(existingTile.getLocation());
				lot.originallyEmpty(false);
			} else lot = (Lot) existingTile;

	        lot.placeTower(tower,tType); 
	        lot.setType(TileType.TOWER); 

	        map.tileMap[y][x] = lot;

	    } catch (ArrayIndexOutOfBoundsException e) {
	        System.out.println("Error: Array index out of bounds while placing tower!");
	    }
	}
	public void placeTile(TileType type, DecorativeType decorativeType, int height, int width) {
        try {
            if (type != TileType.DECORATIVES) {
                System.out.println("Error: Wrong method. Use the correct placeTile overload!");
                return;
            }

            int x = width;
            int y = height;

            Tile existingTile = map.tileMap[y][x];

            if (existingTile != null && existingTile.getType() != TileType.GRASS) {
                System.out.println("Error: Cannot place tile on top of another tile. Remove it first.");
                return;
            }

            Tile tile = new DecorativeTile(decorativeType, existingTile.getLocation());

            map.tileMap[y][x] = tile;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Array index out of bounds while placing decorative :d!");
        }
    }

	public void removeTile(int height, int width) {
		try {
			int x = width;
			int y = height;

			Tile currentTile = map.tileMap[y][x];
			
			// Special case: Castle tile
			if (currentTile.type == TileType.CASTLE) {
				// Check all possible neighbours and locate the where castle is placed
				for (int dy = -1; dy <= 0; dy++) {
					for (int dx = -1; dx <= 0; dx++) {
						int nx = x + dx;
						int ny = y + dy;
						if (nx >= 0 && ny >= 0 && nx + 1 < map.width && ny + 1 < map.height) {
							if (map.tileMap[ny][nx].type == TileType.CASTLE
									&& map.tileMap[ny][nx + 1].type == TileType.CASTLE
									&& map.tileMap[ny + 1][nx].type == TileType.CASTLE
									&& map.tileMap[ny + 1][nx + 1].type == TileType.CASTLE) {

								map.tileMap[ny][nx] = new Tile(TileType.GRASS, map.tileMap[ny][nx].location);
								map.tileMap[ny][nx + 1] = new Tile(TileType.GRASS, map.tileMap[ny][nx + 1].location);
								map.tileMap[ny + 1][nx] = new Tile(TileType.GRASS, map.tileMap[ny + 1][nx].location);
								map.tileMap[ny + 1][nx + 1] = new Tile(TileType.GRASS,
										map.tileMap[ny + 1][nx + 1].location);
								return;
							}
						}
					}
				}
			}
			// Removal of path tile, resets neighbours
			else if (currentTile instanceof PathTile) {
				PathTile pathTile = (PathTile) currentTile;

				if (pathTile.getUp() != null) {
					pathTile.getUp().setDown(null);
				}
				if (pathTile.getDown() != null) {
					pathTile.getDown().setUp(null);
				}
				if (pathTile.getLeft() != null) {
					pathTile.getLeft().setRight(null);
				}
				if (pathTile.getRight() != null) {
					pathTile.getRight().setLeft(null);
				}

				if (map.getStartingTiles().contains(pathTile)) {
					map.removeStartingTile(pathTile);
				}
				if (map.getEndingTiles() == pathTile) {
					map.removeEndingTile(pathTile);
				}

				map.tileMap[y][x] = new Tile(currentTile.location); // Replace with grass tile

			} 
			else if (currentTile.type == TileType.TOWER) {
				Lot lot = (Lot) map.tileMap[y][x];
				map.removeTower(lot.getTower());
				// When tower is removed a lot remains, since towers are normally placed on top of lots
				lot.removeTower();
				lotCount++;
				map.setLotCount(lotCount);
			}
			else {
				if(currentTile.type == TileType.LOT) {
					lotCount--;
					map.setLotCount(lotCount);
				}
				map.tileMap[y][x] = new Tile(currentTile.location); // Replace with grass tile
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: Invalid tile location for removal.");
		}
	}

	public boolean isValidMap() {
		// Ensure that starting and ending tiles are really placed
		if (isStartingTilePlaced && isEndingTilePlaced && map.getStartingTiles() != null
				&& map.getEndingTiles() != null) {
			//Ensure that there are at least four tiles with empty lots
			if (lotCount < 4) return false;
			HashMap<PathTile, List<PathTile>> reachablePathMap = map.getPathMap();
			if (reachablePathMap.isEmpty()) return false;
			//Ensure that there is a connected path and, first tile and last tiles are starting and ending tiles respectively. (Should work if A* is implemented correctly)
			boolean workingPath = false;
			for (PathTile tile : reachablePathMap.keySet()) {
				if (reachablePathMap.get(tile) == null) continue;

				List<PathTile> path = reachablePathMap.get(tile);
				if (path.getFirst() == tile && map.getEndingTiles().contains(path.getLast())) {
					workingPath = true;
				}
			}
			return workingPath;
		}
		System.out.println("Map does not satisfy the map conditions!");
		return false;
	}

	public void saveMap() {
		// Write map if it is a valid map
		if (isValidMap()) {
			map.setPath();
			Utilities.writeMap(map);
		}
	}

}