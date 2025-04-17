package domain.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.PriorityQueue;

import domain.map.Location;
import domain.map.Map;
import domain.map.PathTile;
import domain.map.Tile;
import domain.map.TileType;

// I will add comments later
public final class Utilities {
	private static final String MAP_FILE_PATH = "Data/Maps/";
	
	static class AStarComparator implements Comparator<SimpleEntry<PathTile, Double>> {
		
		@Override
		public int compare(SimpleEntry<PathTile, Double> e1, SimpleEntry<PathTile, Double> e2) {
			if (e1.getValue() < e2.getValue()) return -1;
			if (e1.getValue() > e2.getValue()) return 1;
			return 0;
		}
	}
	
	// I'D RATHER DIE THAN COMMENT THIS GO LOOK UP A* OR SMTH
	// Returns the PathTiles in an arraylist sorted from start to finish
	public static ArrayList<PathTile> findPath(Map map) {
		PathTile start = map.startingTile;
		PathTile end = map.endingTile;
		
		ArrayList<PathTile> pathTileArray = new ArrayList<PathTile>();
		for(Tile[] row : map.tileMap)
			for(Tile tile : row)
				if (tile.getType() == TileType.PATH) pathTileArray.add((PathTile)tile);
		
		HashMap<PathTile, Double> heuristicMap = new HashMap<PathTile, Double>();
		for(PathTile tile : pathTileArray) {
			double distance = euclideanDistance(tile.location, end.location);
			heuristicMap.put(tile, distance);
		}
		
		HashMap<PathTile, Double> dMap = new HashMap<PathTile, Double>();
		for(PathTile tile : pathTileArray)
			dMap.put(tile, Double.MAX_VALUE);
		dMap.put(start, 0.0);
		
		HashMap<PathTile, Double> fMap = new HashMap<PathTile, Double>();
		for(PathTile tile : pathTileArray)
			fMap.put(tile, Double.MAX_VALUE);
		fMap.put(start, 0 + heuristicMap.get(start));
		
		AStarComparator comp = new AStarComparator();
		PriorityQueue<SimpleEntry<PathTile, Double>> open = new PriorityQueue<SimpleEntry<PathTile, Double>>(comp);
		SimpleEntry<PathTile, Double> startEntry = new SimpleEntry<PathTile, Double>(start, fMap.get(start));
		open.add(startEntry);
		
		HashMap<PathTile, PathTile> invPathMap = new HashMap<PathTile, PathTile>();
		while(!open.isEmpty()) {
			PathTile currentTile = open.poll().getKey();
			if (currentTile == end) break;
			
			ArrayList<PathTile> children = new ArrayList<PathTile>();
			if (currentTile.getUp() != null) children.add(currentTile.getUp());
			if (currentTile.getDown() != null) children.add(currentTile.getDown());
			if (currentTile.getLeft() != null) children.add(currentTile.getLeft());
			if (currentTile.getRight() != null) children.add(currentTile.getRight());
			
			for(PathTile childTile : children) {
				double dScore = dMap.get(currentTile) + 1;
				double fScore = dScore + heuristicMap.get(childTile);
				
				if (fScore < fMap.get(childTile)) {
					dMap.put(childTile, dScore);
					fMap.put(childTile, fScore);
					SimpleEntry<PathTile, Double> childEntry = new SimpleEntry<PathTile, Double>(childTile, fScore);
					open.add(childEntry);
					invPathMap.put(childTile, currentTile);
				}
			}
		}
		
		HashMap<PathTile, PathTile> pathMap = new HashMap<PathTile, PathTile>();
		PathTile tile = end;
		while (tile != start) {
			pathMap.put(invPathMap.get(tile), tile);
			tile = invPathMap.get(tile);
		}
		
		ArrayList<PathTile> pathArray = new ArrayList<PathTile>();
		tile = start;
		pathArray.add(start);
		
		while (tile != end) {
			tile = pathMap.get(tile);
			pathArray.add(tile);
		}
		
		return pathArray;
	}
	
	public static double euclideanDistance(Location location1, Location location2) {
		double distanceX = location1.xCoord - location2.xCoord;
		double distanceY = location1.yCoord - location2.yCoord;
		
		return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}
	
	public static void writeMap(Map map) {
		try {
			FileOutputStream fileOut = new FileOutputStream(MAP_FILE_PATH + map.mapName + ".ser");
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOut);
			
			objectOutput.writeObject(map);
			
			objectOutput.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Oops something went wrong while writing map to disk :( ");
		} 
	}
	
	public static Map readMap(String fileName) {
		Map map = null;
		try {
			FileInputStream fileIn = new FileInputStream(MAP_FILE_PATH + fileName + ".ser");
			ObjectInputStream objectInput = new ObjectInputStream(fileIn);
			
			map = (Map)objectInput.readObject();
			
			objectInput.close();
			fileIn.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Sorry couldn't read your map, better luck next time!");
		}
		
		return map;
	}
}

class UtilTest {
	
	private static void mapReadWriteTest() {
		System.out.println("-----Map Read/Write Test-----");
		
		String map1Name = "map1";
		int height1 = 30, width1 = 20;
		Map map1 = new Map(map1Name, height1, width1);
		
		Utilities.writeMap(map1);
		Map map1Test = Utilities.readMap(map1Name);
		
		if (map1Test.height == map1.height && map1Test.width == map1.width && map1Test.mapName.equals(map1.mapName)) {
			System.out.println("Test 1 - PASSED");
		} else { System.out.println("Test 1 - FAILED"); } 
		
		String map2Name = "map2";
		int height2 = 69, width2 = 420;
		Map map2 = new Map(map2Name, height2, width2);
		
		Utilities.writeMap(map2);
		Map map2Test = Utilities.readMap(map2Name);
		
		if (map2Test.height == map2.height && map2Test.width == map2.width && map2Test.mapName.equals(map2.mapName)) {
			System.out.println("Test 2 - PASSED");
		} else { System.out.println("Test 2 - FAILED"); } 
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
		
		ArrayList<PathTile> path = Utilities.findPath(map1);
		// Trust me it works I have proof
	}
	
	
	public static void main(String[] args) {
		mapReadWriteTest();
		testPathFinding();
	}
}
