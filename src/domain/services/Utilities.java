package domain.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import domain.kutowerdefense.GameOptions;
import domain.map.Location;
import domain.map.Map;
import domain.map.PathTile;
import domain.map.Tile;
import domain.map.TileType;

public final class Utilities {
	private static final String MAP_FILE_PATH = "Data/Maps/";
	private static final String OPTIONS_FILE_PATH = "Data/Options/";
	
	/**
	 * Comparator for the priority queue used in findPath
	 */
	static class AStarComparator implements Comparator<SimpleEntry<PathTile, Double>> {
		
		@Override
		public int compare(SimpleEntry<PathTile, Double> e1, SimpleEntry<PathTile, Double> e2) {
			if (e1.getValue() < e2.getValue()) return -1;
			if (e1.getValue() > e2.getValue()) return 1;
			return 0;
		}
	}
	
	/**
	 * 
	 * @param map
	 * @return Returns a List of PathTile objects sorted from starting tile to ending tile of given map 
	 */
	public static List<PathTile> findPath(Map map) {
		// MAL BEDO YANLIŞ YAPMIŞ VİSİT İŞARETLE
		PathTile start = map.startingTile;
		PathTile end = map.endingTile;
		
		// Gets all PathTile objects from map.tileMap
		ArrayList<PathTile> pathTileArray = new ArrayList<PathTile>();
		for(Tile[] row : map.tileMap)
			for(Tile tile : row)
				if (tile.getType() == TileType.PATH) {
					pathTileArray.add((PathTile)tile);
				}
		
		// Chose to use euclidean distance for the heuristic medium of A* algorithm
		// Create a map of PathTiles as keys with their euclidean distance to the ending tile as values
		HashMap<PathTile, Double> heuristicMap = new HashMap<PathTile, Double>();
		for(PathTile tile : pathTileArray) {
			double distance = euclideanDistance(tile.location, end.location);
			heuristicMap.put(tile, distance);
		}
		
		// Create a map of path tiles and direct distance covered so far up to the path tile
		// Same logic as Dijstra's search algorithm
		HashMap<PathTile, Double> dMap = new HashMap<PathTile, Double>();
		for(PathTile tile : pathTileArray)
			dMap.put(tile, Double.MAX_VALUE);		// Initialize distances to infinity for a start and update them later on
		dMap.put(start, 0.0);
		
		// A map of heuristic + direct distance
		HashMap<PathTile, Double> fMap = new HashMap<PathTile, Double>();
		for(PathTile tile : pathTileArray)
			fMap.put(tile, Double.MAX_VALUE);		// Initialize distances to infinity for a start and update them later on
		fMap.put(start, 0 + heuristicMap.get(start));
		
		// Comparator for priority queue
		AStarComparator comp = new AStarComparator();
		
		// A priority queue sorted with the Double value and provides PathTiles in order, stored in key of SimpleEntry.
		// The ones with lowest distance are prioritised
		PriorityQueue<SimpleEntry<PathTile, Double>> open = new PriorityQueue<SimpleEntry<PathTile, Double>>(comp);
		SimpleEntry<PathTile, Double> startEntry = new SimpleEntry<PathTile, Double>(start, fMap.get(start));
		open.add(startEntry); 		// Add start to queue
		
		HashMap<PathTile, PathTile> invPathMap = new HashMap<PathTile, PathTile>();
		while(!open.isEmpty()) { 	// Until the queue is empty
			PathTile currentTile = open.poll().getKey();
			if (currentTile == end) break;	// Stop if end is reached
			
			// Get all possible neighbours of currentTile
			ArrayList<PathTile> children = new ArrayList<PathTile>();
			if (currentTile.getUp() != null) children.add(currentTile.getUp());
			if (currentTile.getDown() != null) children.add(currentTile.getDown());
			if (currentTile.getLeft() != null) children.add(currentTile.getLeft());
			if (currentTile.getRight() != null) children.add(currentTile.getRight());
			
			for(PathTile childTile : children) {
				double dScore = dMap.get(currentTile) + 1;
				double fScore = dScore + heuristicMap.get(childTile);
				
				// If total distance is less than the stored value -> update
				if (fScore < fMap.get(childTile)) {
					dMap.put(childTile, dScore);
					fMap.put(childTile, fScore);
					SimpleEntry<PathTile, Double> childEntry = new SimpleEntry<PathTile, Double>(childTile, fScore);
					open.add(childEntry);	// Add child to priority queue so that the algorithm can continue search from there
					invPathMap.put(childTile, currentTile);	// A map where a child points to their parent forming a path from end to start	
				}
			}
		}
		
		// We want path from start to end
		// So we turn it into a list and reverse
		ArrayList<PathTile> pathArray = new ArrayList<PathTile>();
		PathTile tile = end;
		pathArray.add(end);
		
		while (tile != start && tile != null) {
			tile = invPathMap.get(tile);
			pathArray.add(tile);
		}
		
		Collections.reverse(pathArray);
		if (pathArray.get(0) != start || pathArray.get(pathArray.size()-1) != end) return null;
		
		return pathArray;
	}
	
	/**
	 * 
	 * @param location1
	 * @param location2
	 * @return Returns Euclidean distance between two location objects
	 */
	public static double euclideanDistance(Location location1, Location location2) {
		double distanceX = location1.xCoord - location2.xCoord;
		double distanceY = location1.yCoord - location2.yCoord;
		
		return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}

	private static String getDataDirectory() {
		// For packaged app (jpackage)
		String appDir = System.getProperty("app.dir", "");
		if (!appDir.isEmpty()) {
			return appDir + "/app/Data/";
		}
		// For development
		return "Data/";
	}

	private static void ensureDirectoryExists(String path) throws IOException {
		Path dirPath = Paths.get(path);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
	}

	public static void writeMap(Map map) {
		try {
			ensureDirectoryExists(MAP_FILE_PATH);
			Path filePath = Paths.get(MAP_FILE_PATH + map.mapName + ".ser");
			try (ObjectOutputStream out = new ObjectOutputStream(
					Files.newOutputStream(filePath))) {
				out.writeObject(map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map readMap(String fileName) {
		try {
			Path filePath = Paths.get(MAP_FILE_PATH + fileName + ".ser");
			try (ObjectInputStream in = new ObjectInputStream(
					Files.newInputStream(filePath))) {
				return (Map) in.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeOptions() {
		try {
			ensureDirectoryExists(OPTIONS_FILE_PATH);
			Path filePath = Paths.get(OPTIONS_FILE_PATH + "user_options.ser");
			try (ObjectOutputStream out = new ObjectOutputStream(
					Files.newOutputStream(filePath))) {
				out.writeObject(GameOptions.getInstance());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeDefaultOptions() {
		try {
			ensureDirectoryExists(OPTIONS_FILE_PATH);
			Path filePath = Paths.get(OPTIONS_FILE_PATH + "default_options.ser");
			try (ObjectOutputStream out = new ObjectOutputStream(
					Files.newOutputStream(filePath))) {
				out.writeObject(GameOptions.getDefaultOptions());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static GameOptions readOptions() {
		try {
			Path filePath = Paths.get(OPTIONS_FILE_PATH + "user_options.ser");
			if (!Files.exists(filePath)) {
				writeDefaultOptions(); // Create default if missing
			}
			try (ObjectInputStream in = new ObjectInputStream(
					Files.newInputStream(filePath))) {
				return (GameOptions) in.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return readDefaultOptions(); // Fallback to defaults
		}
	}

	public static GameOptions readDefaultOptions() {
		try {
			Path defaultPath = Paths.get(OPTIONS_FILE_PATH + "default_options.ser");

			// Create default file if missing
			if (!Files.exists(defaultPath)) {
				writeDefaultOptions();
			}

			try (ObjectInputStream in = new ObjectInputStream(
					Files.newInputStream(defaultPath))) {
				return (GameOptions) in.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			// Ultimate fallback - generate fresh defaults
			GameOptions options = GameOptions.getDefaultOptions();
			writeDefaultOptions(); // Try writing again
			return options;
		}
	}
	
	public static double manhattanDistance(Location location1, Location location2) {
		double distanceX = location1.getXCoord() - location2.getXCoord();
		double distanceY = location1.getYCoord() - location2.getYCoord();
		
		return Math.abs(distanceX) +Math.abs(distanceY);
	}
}
