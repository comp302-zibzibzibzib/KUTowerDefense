package domain.services;

import java.util.ArrayList;
import java.util.List;

import domain.map.Location;
import domain.map.Map;
import domain.map.PathTile;
import domain.map.Tile;

// Test class to test various components in domain
public final class Test {
	
	static class MapTest {
		public static void printMap(Map map) {
			for (int i = 0; i < map.height; i++) {
				for (int j = 0; j < map.width; j++) {
					System.out.print(map.tileMap[i][j].type.toString());
				}
				System.out.print("\n");
			}
		}
		
		public static void main(String[] args) {
			System.out.println("TEST 1");
			Map map1 = new Map("map1", 5, 6);
			printMap(map1);
			
			System.out.println("TEST 2");
			Location l1 = new Location(8,9);
			Location l2 = new Location(1,0);
			PathTile start = new PathTile(l1);
			PathTile end = new PathTile(l2);
			Map map2 = new Map("map2", start, end ,10, 10); //Do an out of bounds check!!!
			printMap(map2);
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
		
		private static void testPathFinding() {
			Location l1 = new Location(3,4);
			Location l2 = new Location(1,0);
			PathTile start = new PathTile(l1);
			PathTile end = new PathTile(l2);
			Map map1 = new Map("map1", start, end , 5, 5);
			
			Tile[][] tileMap = map1.tileMap;
			Location l3 = new Location(3,3);
			PathTile p33 = new PathTile(l3);
			
			Location l4 = new Location(4,3);
			PathTile p34 = new PathTile(l4);
			
			Location l5 = new Location(4,2);
			PathTile p24 = new PathTile(l5);
			
			Location l6 = new Location(2,3);
			PathTile p32 = new PathTile(l6);
			
			Location l7 = new Location(2,2);
			PathTile p22 = new PathTile(l7);
			
			Location l8 = new Location(1,2);
			PathTile p21 = new PathTile(l8);
			
			Location l9 = new Location(1,1);
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
			
			List<PathTile> path = Utilities.findPath(map1);
			ArrayList<Location> testPathLocation = new ArrayList<Location>();
			testPathLocation.add(new Location(3,4));
			testPathLocation.add(new Location(3,3));
			testPathLocation.add(new Location(2,3));
			testPathLocation.add(new Location(2,2));
			testPathLocation.add(new Location(1,2));
			testPathLocation.add(new Location(1,1));
			testPathLocation.add(new Location(1,0));
			
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
			// Trust me it works I have proof
		}
		
		
		public static void main(String[] args) {
			mapReadWriteTest();
			testPathFinding();
		}
	}
}
