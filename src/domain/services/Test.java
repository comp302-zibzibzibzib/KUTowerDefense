package domain.services;

import java.util.ArrayList;
import java.util.List;

import domain.entities.Enemy;
import domain.entities.Knight;
import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;
import domain.map.Lot;
import domain.map.Map;
import domain.map.MapEditor;
import domain.map.PathTile;
import domain.map.PathType;
import domain.map.Tile;
import domain.map.TileType;
import domain.tower.ArcherTower;
import domain.tower.Tower;

// Test class to test various components in domain
public final class Test {
	static class MapTest {
		public static void printMap(Map map) {
			for (int i = 0; i < map.height; i++) {
				for (int j = 0; j < map.width; j++) {
					if(map.tileMap[i][j] instanceof PathTile) {
						System.out.print(((PathTile)map.tileMap[i][j]).getPathType().toString());
					}
					else {
						System.out.print(map.tileMap[i][j].type.toString());
					}
				}
				System.out.print("\n");
			}
		}
		public static void printTileLocations(Map map) {
			for (int i = 0; i < map.height; i++) {	
				for (int j = 0; j < map.width; j++) {
					System.out.printf("---Tile [%d,%d] Coordinates---\n",i,j);
					System.out.printf("%f,%f\n",map.tileMap[i][j].location.xCoord,map.tileMap[i][j].location.yCoord);	
					}
				}
				System.out.print("\n");
			}			
		
		
		public static void main(String[] args) {
			System.out.println("TEST 1");
			Map map1 = new Map("map1", 5, 6);
			MapEditor me1 = new MapEditor(map1);
			me1.placeTile(TileType.PATH,PathType.VERTICAL_MIDDLE, 4,5);
			me1.placeTile(TileType.TOWER, 4,4);
			me1.placeTile(TileType.CASTLE,0,0);
			me1.placeTile(TileType.DECORATIVES,2,2);
			me1.removeTile(4,4);
			System.out.println(map1.tileMap[4][4].getClass().toString());
			
			printMap(map1);
			printTileLocations(map1);
			
			System.out.println("TEST 2");
			Location location1 = new Location(8,8);
			Location location2 = new Location(1,0);
			PathTile s = new PathTile(PathType.VERTICAL_MIDDLE,location1);
			PathTile e = new PathTile(PathType.BOTTOMRIGHT,location2);
			Map map2 = new Map("map2", s, e ,9, 16); //Do an out of bounds check!!!
			printMap(map2);
			
			System.out.println("TEST 3");
			Map map3 = new Map("map3", 5, 5);
			MapEditor me3 = new MapEditor(map3);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,4,3);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0,1);
			me3.placeTile(TileType.PATH, PathType.TOPLEFT, 3, 3);
			me3.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 3, 4);
			me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 2, 4);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 3, 2);
			me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 2, 2);
			me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 2, 1);
			me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 1, 1);
			printMap(map3);
			printTileLocations(map3);
		}
	}
	static class UtilTest {
		
		// Test if attributes are the same after read and write
		private static void mapReadWriteTest() {
			System.out.println("-----Map Read/Write Test-----");
			
			String map1Name = "map1";
			int height1 = 30, width1 = 20;
			Map map1 = new Map(map1Name, height1, width1);
			
			Utilities.writeMap(map1);
			Map map1Test = Utilities.readMap(map1Name);
			
			if (map1Test.height == map1.height && map1Test.width == map1.width && map1Test.mapName.equals(map1.mapName)) {
				System.out.println("MapReadWrite Test 1 - PASSED");
			} else { System.out.println("MapReadWrite Test 1 - FAILED"); } 
			
			String map2Name = "map2";
			int height2 = 69, width2 = 420;
			Map map2 = new Map(map2Name, height2, width2);
			
			Utilities.writeMap(map2);
			Map map2Test = Utilities.readMap(map2Name);
			
			if (map2Test.height == map2.height && map2Test.width == map2.width && map2Test.mapName.equals(map2.mapName)) {
				System.out.println("MapReadWrite Test 2 - PASSED");
			} else { System.out.println("MapReadWrite Test 2 - FAILED"); } 
		}
		
		private static List<PathTile> testPathFinding() {
			System.out.println("-----Path Finding Test-----");
			Map map1 = new Map("map1", 5, 5);
			MapEditor me3 = new MapEditor(map1);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,4,3);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0,1);
			me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 3, 3);
			me3.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 3, 4);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_END_TOP, 2, 4);
			me3.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 3, 2);
			me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 2, 2);
			me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 2, 1);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1, 1);
			
			List<PathTile> path = Utilities.findPath(map1);
			ArrayList<Location> testPathLocation = new ArrayList<Location>();
			testPathLocation.add(new Location(17.5,22.5));
			testPathLocation.add(new Location(17.5,17.5));
			testPathLocation.add(new Location(12.5,17.5));
			testPathLocation.add(new Location(12.5,12.5));
			testPathLocation.add(new Location(7.5,12.5));
			testPathLocation.add(new Location(7.5,7.5));
			testPathLocation.add(new Location(7.5,2.5));
			
			if (path.size() != testPathLocation.size()) {
				System.out.println("PathFinding Test - FAILED");
				return null;
			}
			
			for (int i = 0; i < testPathLocation.size(); i++) {
				Location tileLocation = path.get(i).location;
				Location testLocation = testPathLocation.get(i);
				if (tileLocation.xCoord != testLocation.xCoord || tileLocation.yCoord != testLocation.yCoord) {
					System.out.println("PathFinding Test - FAILED");
					return null;
				}
			}
			
			System.out.println("PathFinding Test - PASSED");
			MapTest.printMap(map1);
			for(PathTile p : path) {
				System.out.println(p.getPathType());
			}
			return path;
		}
		           
		
		public static void main(String[] args) {
			mapReadWriteTest();
			testPathFinding();
		}
	}
	
	static class TowerTest {
		private static void testTargetEnemy() {
			Location l1 = new Location(17.5,22.5);
			Location l2 = new Location(7.5,2.5);
			PathTile start = new PathTile(l1);
			PathTile end = new PathTile(l2);
			Map map1 = new Map("map1", 5, 5);
			map1.setStartingTile(start);
			map1.setEndingTile(end);
			
			Tile[][] tileMap = map1.tileMap;
			Location l3 = new Location(17.5,17.5);
			PathTile p33 = new PathTile(l3);
			
			Location l4 = new Location(22.5,17.5);
			PathTile p34 = new PathTile(l4);
			
			Location l5 = new Location(22.5,12.5);
			PathTile p24 = new PathTile(l5);
			
			Location l6 = new Location(12.5,17.5);
			PathTile p32 = new PathTile(l6);
			
			Location l7 = new Location(12.5,12.5);
			PathTile p22 = new PathTile(l7);
			
			Location l8 = new Location(7.5,12.5);
			PathTile p21 = new PathTile(l8);
			
			Location l9 = new Location(7.5,7.5);
			PathTile p11 = new PathTile(l9);
			
			start.setUp(p33);
			
			p33.setRight(p34);
			p33.setLeft(p32);
			p33.setDown(start);
			
			p34.setUp(p24);
			p34.setLeft(p33);
			
			p24.setDown(p34);
			
			p32.setRight(p33);
			p32.setUp(p22);
			
			p22.setDown(p32);
			p22.setLeft(p21);
			
			p21.setRight(p22);
			p21.setUp(p11);
			
			p11.setDown(p21);
			p11.setUp(end);
			
			end.setDown(p11);
			
			tileMap[3][3] = p33;
			tileMap[3][4] = p34;
			tileMap[2][4] = p24;
			tileMap[3][2] = p32;
			tileMap[2][1] = p21;
			tileMap[2][2] = p22;
			tileMap[1][1] = p11;
			
			Tile towerTile = tileMap[2][3];
			towerTile.setType(TileType.TOWER);
			Lot lot = new Lot(towerTile);
			Tower archerTower = new ArcherTower(200, 1, 7.5, 2);
			lot.placeTower(archerTower);
			
			List<PathTile> path = Utilities.findPath(map1);
			Map.printMap(map1);
			
			PlayModeManager.getInstance().setCurrentMap(map1); // Set current map
			
			System.out.println("-----Target Enemy Test-----");
			
			// TEST1 - Base Test
			double hitpoints = 200;
			double speed = 10;
			double damageReduction = 40;
			Enemy enemy1 = new Knight(hitpoints, speed, new Location(7.5, 2.5), damageReduction); // Enemy on ending tile
			enemy1.setPathIndex(6);
			Enemy enemy2 = new Knight(hitpoints, speed, new Location(12.5, 12.5), damageReduction); // Enemy left of tower
			enemy2.setPathIndex(3);
			Enemy enemy3 = new Knight(hitpoints, speed, new Location(17.5, 17.5), damageReduction); // Enemy below tile
			enemy3.setPathIndex(1);
			Enemy enemy4 = new Knight(hitpoints, speed, new Location(17.5, 22.5), damageReduction); // Enemy on starting tile
			enemy4.setPathIndex(0);
			
			Enemy.setPath();
			
			archerTower.targetEnemy();
			if (archerTower.getTarget() == enemy2) System.out.println("Target Enemy Test1 - PASSED");
			else System.out.println("Target Enemy Test1 - FAILED");
			
			// TEST2 - Test when more than two enemies in a single tile
			Enemy enemy5 = new Knight(hitpoints, speed, new Location(12.2, 12.5), damageReduction); // Enemy just a little left of enemy2
			enemy5.setPathIndex(3);
			Enemy enemy6 = new Knight(hitpoints, speed, new Location(12.7, 12.5), damageReduction); // Enemy just a little right of enemy2
			enemy6.setPathIndex(3);
			// Enemy 5 is closer to finish than 2 and 2 is closer than 6 and 6 is closer than 3
			// The others are out of range
			
			archerTower.targetEnemy();
			if(archerTower.getTarget() == enemy5) System.out.println("Target Enemy Test2 - PASSED");
			else System.out.println("Target Enemy Test2 - FAILED");
			
		}
		
		public static void main(String[] args) {
			testTargetEnemy();
		}
	}
}
