package domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import domain.controller.EntityController;
import domain.entities.Enemy;
import domain.entities.EnemyFactory;
import domain.entities.Goblin;
import domain.entities.Knight;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.DecorativeType;
import domain.map.Location;
import domain.map.Lot;
import domain.map.Map;
import domain.map.MapEditor;
import domain.map.PathTile;
import domain.map.PathType;
import domain.map.Tile;
import domain.map.TileType;
import domain.map.TowerType;
import domain.tower.ArcherTower;
import domain.tower.Tower;
import domain.tower.TowerFactory;

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
			System.out.println("Pre-Built Map");
			Map map = new Map("Pre-Built Map", 9, 16);
			MapEditor me = new MapEditor(map);
			me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0,4);
			me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1,4);
			me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 2,4);
			me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 3,4);
			me.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 4,4);
			me.placeTile(TileType.PATH, PathType.TOPRIGHT, 4,5);
			me.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,5,5);
			me.placeTile(TileType.PATH, PathType.TOPLEFT, 5,4);
			me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,6,4);
			me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,7,4);
			me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 8,4);
			me.placeTile(TileType.CASTLE, 0, 2);
			me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 0, 0);
			me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 0, 15);
			me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 8, 0);
			me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 8, 15);
			me.placeTile(TileType.LOT, 3, 2);
			me.placeTile(TileType.LOT, 5, 2);
			me.placeTile(TileType.LOT, 3, 6);
			me.placeTile(TileType.LOT, 8, 5);
			me.placeTile(TileType.TOWER, TowerType.ARCHER, 0,5);
			me.placeTile(TileType.TOWER, TowerType.MAGE, 8,3);
			me.placeTile(TileType.DECORATIVES,DecorativeType.HOUSE1, 5, 14);
			me.placeTile(TileType.DECORATIVES,DecorativeType.HOUSE2, 7, 13);
			me.placeTile(TileType.DECORATIVES,DecorativeType.WELL, 7, 10);
			
			me.saveMap();
			Map loadedMap = Utilities.readMap("Pre-Built Map");
			printMap(loadedMap);
			
			System.out.println("TEST 1");
			Map map1 = new Map("map1", 5, 6);
			MapEditor me1 = new MapEditor(map1);
			me1.placeTile(TileType.PATH,PathType.VERTICAL_MIDDLE, 4,5);
			me1.placeTile(TileType.TOWER, TowerType.MAGE, 4,4);
			me1.placeTile(TileType.TOWER, TowerType.ARCHER, 2,4);
			me1.placeTile(TileType.LOT, 1, 0);
			me1.removeTile(1, 0);
			me1.placeTile(TileType.CASTLE,0,0);
			me1.placeTile(TileType.DECORATIVES, 1, 1);
			me1.placeTile(TileType.DECORATIVES,2,2);
			System.out.println(map1.tileMap[4][4].getClass().toString());
			
			printMap(map1);
			printTileLocations(map1);
			
			System.out.println("TEST 2");
			Location location1 = new Location(8,8);
			Location location2 = new Location(1,0);
			PathTile s = new PathTile(PathType.VERTICAL_MIDDLE,location1);
			PathTile e = new PathTile(PathType.BOTTOMRIGHT,location2);
			Map map2 = new Map("map2", s, e ,9, 16); 
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
			
			Map map3 = new Map("map3", 9, 16);
			Utilities.writeMap(map3);
			Map map3Test = Utilities.readMap("map3");
			if (map3Test.height == map3.height && map3Test.width == map3.width && map3Test.mapName.equals(map3.mapName)) {
				System.out.println("MapReadWrite Test 2 - PASSED");
			} else { System.out.println("MapReadWrite Test 2 - FAILED"); }
			
		}
		
		private static void testPathFinding() {
			System.out.println("-----Path Finding Test-----");
			Map map1 = new Map("map1", 5, 5);
			MapEditor me3 = new MapEditor(map1);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,4,3);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0,1);
			me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 3, 3);
			me3.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 3, 4);
			me3.placeTile(TileType.PATH, PathType.VERTICAL_END_TOP, 2, 4);
			me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 3, 2);
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
				return;
			}
			
			for (int i = 0; i < testPathLocation.size(); i++) {
				Location tileLocation = path.get(i).location;
				Location testLocation = testPathLocation.get(i);
				if (tileLocation.xCoord != testLocation.xCoord || tileLocation.yCoord != testLocation.yCoord) {
					System.out.println("PathFinding Test - FAILED");
					return;
				}
			}
			
			System.out.println("PathFinding Test - PASSED");
			MapTest.printMap(map1);
			for(PathTile p : path) {
				System.out.println(p.getPathType());
			}
			return;
		}
		           
		
		public static void main(String[] args) {
			mapReadWriteTest();
			testPathFinding();
		}
	}
	
	static class TowerTest {
		private static void testTargetEnemy() {
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
			me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1, 0);
			
		
			
			Tile towerTile = map1.tileMap[2][3];
			towerTile.setType(TileType.TOWER);
			Lot lot = new Lot(towerTile.getLocation());
			Tower archerTower = TowerFactory.createArcherTower();

			lot.placeTower(archerTower, TowerType.ARCHER);
			
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
			
			//enemy constructor no longer adds them to enemies
			Enemy.enemies.add(enemy1);
			Enemy.enemies.add(enemy2);
			Enemy.enemies.add(enemy3);
			Enemy.enemies.add(enemy4);
			
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
	
	static class enemyMovement {
		
		private static long lastUpdate = 0;
		private static int s = 0;
		private static int totalTimeElapsed = 0;
		
		private static void testMovement() {
		
		Map map1 = new Map("map1", 5, 5);
		MapEditor me3 = new MapEditor(map1);
		PlayModeManager man = PlayModeManager.getInstance();
		
		
		me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,4,3);
		me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0,1);
		me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 3, 3);
		me3.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 3, 4);
		me3.placeTile(TileType.PATH, PathType.VERTICAL_END_TOP, 2, 4);
		me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 3, 2);
		me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 2, 2);
		me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 2, 1);
		me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1, 1);
		
		man.setCurrentMap(map1);
		System.out.println("map created");
		
		List<PathTile> path = Utilities.findPath(map1);
		map1.endingTile = path.get(path.size()-1);
		map1.startingTile = path.get(0);
		
		Enemy e1 = EnemyFactory.createGoblin();
		e1.setLocation(map1.startingTile.location);
		e1.setPathIndex(0);
		Enemy.path = path;
		System.out.println(e1.getLocation());
		System.out.println(map1.getStartingTile().getLocation());
		System.out.println("enemy created and put in map");
		
		
	        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	        lastUpdate = System.nanoTime(); // Initialize lastUpdate

	        executor.scheduleAtFixedRate(() -> {
	            try {
	                long now = System.nanoTime();
	                long deltaTime = (now - lastUpdate);
	                lastUpdate = now;


	                e1.moveEnemy(deltaTime);
	                System.out.printf("%f x, %f y \n",e1.getLocation().xCoord, e1.getLocation().yCoord);
	                
	                s++;
	                if (s == 60) {
	                    System.out.println("1 sec");
	                    s = 0;
	                    totalTimeElapsed ++;
	                }
	                
	                if(e1.getPathIndex() == path.size()-1) {
	                	System.out.printf("Enemy has reaced end in: %d seconds\n",totalTimeElapsed);
	                	System.out.println("reached End");
	                	executor.shutdown();
	                }

	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }, 0, 16, TimeUnit.MILLISECONDS);
		}
		
		public static void main(String[] args) {
			testMovement();
		}
	}
}
