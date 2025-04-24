package domain.map;

import java.io.Serializable;
import java.util.List;

import domain.services.Utilities;

public class MapEditor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private boolean isStartingTilePlaced = false;
	private boolean isEndingTilePlaced = false;
	private int lotCount = 0;
	
	public Map map;

	public MapEditor(Map map) {
		this.map = map;
	}
	
	public void placeTile(TileType type, int height, int width) {
	    placeTile(type, null, height, width);
	}
	//Overloaded method to place tiles, could be implemented differently as separate methods
	public void placeTile(TileType type, PathType ptype, int height, int width) {
		try {
	        int x = width;
	        int y = height;
	        
	        Tile existingTile = map.tileMap[y][x];
	        //Check wheter a tile is already placed at that position
	        if (existingTile != null && existingTile.getType() != TileType.GRASS) {
	            System.out.println("Error: Cannot place tile on top of another tile. Remove it first.");
	            return; // Cancel placement
	        }
	        //Coordinates of tile to be placed
	        double tileCoordX = Tile.tileLength * (x + 0.5);
	        double tileCoordY = Tile.tileLength * (y + 0.5);
	        //Special case: Castle occupies 4 spaces
	        if(type.equals(TileType.CASTLE)) {
	        	if (!(x >= 0 &&
	        			y >= 0 &&
	        			x + 1 < map.width &&
	        			y + 1 < map.height)) {
	        		System.out.println("Error: Please enter a valid location for Castle!"); //Invalid positions for Castle
	        	}
	        	if (map.tileMap[y][x].type != TileType.GRASS ||
	        	        map.tileMap[y][x + 1].type != TileType.GRASS ||
	        	        map.tileMap[y + 1][x].type != TileType.GRASS ||
	        	        map.tileMap[y + 1][x + 1].type != TileType.GRASS) {
	        	        System.out.println("Error: Cannot place castle on occupied tiles. Clear them first."); //One of the four tiles are occupied, cannot place Castle
	        	       	return;
	        	}
	        	//Place 2x2 castle 
	        	map.tileMap[y][x] = new Tile(TileType.CASTLE,
	                new Location(Tile.tileLength * (x + 0.5), Tile.tileLength * (y + 0.5))); //Top Left

	       	    map.tileMap[y][x + 1] = new Tile(TileType.CASTLE,
	       	        new Location(Tile.tileLength * (x + 1.5), Tile.tileLength * (y + 0.5))); //Top Right

	       	    map.tileMap[y + 1][x] = new Tile(TileType.CASTLE,
	       	        new Location(Tile.tileLength * (x + 0.5), Tile.tileLength * (y + 1.5))); //Bottom Right

	       	    map.tileMap[y + 1][x + 1] = new Tile(TileType.CASTLE,
	       	        new Location(Tile.tileLength * (x + 1.5), Tile.tileLength * (y + 1.5))); //Bottom Left
	        }
	        
	        Location newTileLocation = new Location(tileCoordX, tileCoordY);
	        Tile tileToPlace;
	        //Set neighbours (bidirectional) of path tiles 
	        if (type.equals(TileType.PATH) && ptype != null) {
	            PathTile newPathTile = new PathTile(ptype, newTileLocation, null, null, null, null);
	            tileToPlace = newPathTile;

	            if (!isStartingTilePlaced && y == map.height - 1) { 
	                map.setStartingTile(newPathTile); //Starting tile is set
	                isStartingTilePlaced = true;
	            }
	            if (!isEndingTilePlaced && y == 0) {
	                map.setEndingTile(newPathTile); //Ending tile is set
	                isEndingTilePlaced = true;
	            }
	            
	            if (y > 0 && map.tileMap[y - 1][x] instanceof PathTile) {
	                PathTile up = (PathTile) map.tileMap[y - 1][x];
	                newPathTile.setUp(up);
	                up.setDown(newPathTile);
	            }
	            if (y < map.height - 1 && map.tileMap[y + 1][x] instanceof PathTile) {
	                PathTile down = (PathTile) map.tileMap[y + 1][x];
	                newPathTile.setDown(down);
	                down.setUp(newPathTile);
	            }
	            if (x > 0 && map.tileMap[y][x - 1] instanceof PathTile) {
	                PathTile left = (PathTile) map.tileMap[y][x - 1];
	                newPathTile.setLeft(left);
	                left.setRight(newPathTile);
	            }
	            if (x < map.width - 1 && map.tileMap[y][x + 1] instanceof PathTile) {
	                PathTile right = (PathTile) map.tileMap[y][x + 1];
	                newPathTile.setRight(right);
	                right.setLeft(newPathTile);
	            }

	        }
	        else {		
	            tileToPlace = new Tile(type, newTileLocation);
	            if(type.equals(TileType.LOT)) {
	            	lotCount ++; //Special case: Increment lotCount for lot tiles (requirement for a valid map)
	            	Lot lot = new Lot(newTileLocation); //IMPORTANT: Need some kind of wrapping to access the methods of LOT in-game, alternative implementations are welcomed.
	                tileToPlace = lot;
	            }
	        }
	        //Place the tile on the map
	        map.tileMap[y][x] = tileToPlace;
	        

	    } 
	    catch (ArrayIndexOutOfBoundsException e) {
	        System.out.println("Error: Please enter a valid location!"); 
	        System.exit(-1); //Won't be possible in the UI can be discarded
	    }
		
	}
	
	public void removeTile(int height, int width) {
		 try {
		        int x = width;
		        int y = height;

		        Tile currentTile = map.tileMap[y][x];
		        //Special case: Castle tile
		        if (currentTile.type == TileType.CASTLE) {
		        	//Check all possible neighbours and locate the where castle is placed
		            for (int dy = -1; dy <= 0; dy++) {
		                for (int dx = -1; dx <= 0; dx++) {
		                    int nx = x + dx;
		                    int ny = y + dy;
		                    if (nx >= 0 && ny >= 0 && nx + 1 < map.width && ny + 1 < map.height) {
		                        if (map.tileMap[ny][nx].type == TileType.CASTLE &&
		                            map.tileMap[ny][nx + 1].type == TileType.CASTLE &&
		                            map.tileMap[ny + 1][nx].type == TileType.CASTLE &&
		                            map.tileMap[ny + 1][nx + 1].type == TileType.CASTLE) {
		                            
		                            map.tileMap[ny][nx] = new Tile(TileType.GRASS, map.tileMap[ny][nx].location);
		                            map.tileMap[ny][nx + 1] = new Tile(TileType.GRASS, map.tileMap[ny][nx + 1].location);
		                            map.tileMap[ny + 1][nx] = new Tile(TileType.GRASS, map.tileMap[ny + 1][nx].location);
		                            map.tileMap[ny + 1][nx + 1] = new Tile(TileType.GRASS, map.tileMap[ny + 1][nx + 1].location);
		                            return;
		                        }
		                    }
		                }
		            }
		        }
		        //Removal of path tile, resets neighbours
		        if (currentTile instanceof PathTile) {
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
		            
		            if (map.getStartingTile() == pathTile) {
		                map.setStartingTile(null); //Resets starting tile
		                isStartingTilePlaced = false;
		            }
		            if (map.getEndingTile() == pathTile) {
		                map.setEndingTile(null); //Resets ending tile
		                isEndingTilePlaced = false;
		            }

		            map.tileMap[y][x] = new Tile(currentTile.location); //Replace with grass tile

		        } 
		        else if (currentTile.type == TileType.TOWER) {
		        	Lot lot = new Lot(currentTile.getLocation());
		            map.tileMap[y][x] = lot; //When tower is removed a lot remains, since towers are normally placed on top of lots
		            lotCount++;
		        } 
		        else {
		            map.tileMap[y][x] = new Tile(currentTile.location); //Replace with grass tile
		        }

		    }
		 catch (ArrayIndexOutOfBoundsException e) {
		        System.out.println("Error: Invalid tile location for removal.");
		 }
	}
	
	public boolean isValidMap(Map map) {
		//Ensure that starting and ending tiles are really placed
		if(isStartingTilePlaced && isEndingTilePlaced && map.getStartingTile() != null && map.getEndingTile() != null) { 
			List<PathTile> reachablePath = Utilities.findPath(map);
			//Ensure that there is a connected path and, first tile and last tiles are starting and ending tiles respectively. (Should work if A* is implemented correctly)
			if(reachablePath.getFirst().equals(map.getStartingTile()) && reachablePath.get(reachablePath.size()-1).equals(map.getEndingTile())) {
				//Ensure that there are at least four tiles with empty lots
				if(lotCount >= 4) {
					return true;
				}
			}
		}
		System.out.println("Map does not satisfy the map conditions!");
		return false;
	}
	
	public void saveMap(Map map) {
		//Write map if it is a valid map
		if(isValidMap(map)) {
			Utilities.writeMap(map);
		}
	}
	
}
