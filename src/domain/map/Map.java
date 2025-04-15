package domain.map;

public class Map {
	public Tile startingTile;
	public Tile endingTile;
	public int height;
	public int width;
	public Tile[][] tileMap;
	//Default map with all grass tiles
	public Map(int height, int width) {
		this.height = height;
		this.width = width;
		tileMap = new Tile[height][width];
		constructMap(height,width);
	}
	//Starting tile must be somewhere at the bottom end ending tile must be somewhere on the top
	//Types of these tiles are PATH, isWalkable = 1
	public Map(Tile startingTile, Tile endingTile, int height, int width) {
		this.height = height;
		this.width = width;
		this.startingTile = (startingTile.location.yCoord == height-1) ? startingTile : new Tile(TileType.INVALID); //Display an error message in UI???
		this.endingTile = (endingTile.location.yCoord == 0) ? endingTile : new Tile(TileType.INVALID); 
		this.tileMap = new Tile[height][width];
		tileMap[startingTile.location.yCoord][startingTile.location.xCoord] = this.startingTile;
		tileMap[endingTile.location.yCoord][endingTile.location.xCoord] = this.endingTile;
		constructMap(height, width);
	}
	
	private void constructMap(int h, int w) {
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if(tileMap[i][j] == null) {
					Tile tile = new Tile();
					tileMap[i][j] = tile;
				}
			}
		}	
	}
	public Tile getStartingTile() {
		return startingTile;
	}

	public void setStartingTile(Tile startingTile) {
		this.startingTile = startingTile;
	}

	public Tile getEndingTile() {
		return endingTile;
	}

	public void setEndingTile(Tile endingTile) {
		this.endingTile = endingTile;
	}
}
/* For testing purposes
class MapTest {
	public void printMap(Map map) {
		for (int i = 0; i < map.height; i++) {
			for (int j = 0; j < map.width; j++) {
				switch (map.tileMap[i][j].type) {
					case BOTTOM:
						System.out.print(" B ");
						break;
					case BOTTOMLEFT:
						System.out.print("BL ");
						break;
					case BOTTOMRIGHT:
						System.out.print("BR ");
						break;
					case GRASS:
						System.out.print(" G ");
						break;
					case LEFT:
						System.out.print(" L ");
						break;
					case OBSTACLES:
						System.out.print(" O ");
						break;
					case PATH:
						System.out.print(" P ");
						break;
					case RIGHT:
						System.out.print(" R ");
						break;
					case STRAIGHT:
						System.out.print(" S ");
						break;
					case TOPLEFT:
						System.out.print("TL ");
						break;
					case TOPRIGHT:
						System.out.print("TR ");
						break;
					case UPWARDS:
						System.out.print(" U ");
						break;
					default:
						System.out.print(" ? ");
						break;
				}
			}
			System.out.print("\n");
		}
	}
	
	public static void main(String[] args) {
		System.out.println("TEST 1");
		MapTest test1 = new MapTest();
		Map map1 = new Map(5, 6);
		test1.printMap(map1);
		
		System.out.println("TEST 2");
		MapTest test2 = new MapTest();
		Location l1 = new Location(8,9);
		Location l2 = new Location(1,0);
		Tile start = new Tile(TileType.PATH, true, l1);
		Tile end = new Tile(TileType.PATH, true, l2);
		Map map2 = new Map(start, end ,10, 10); //Do an out of bounds check!!!
		test2.printMap(map2);
	}
}
*/

