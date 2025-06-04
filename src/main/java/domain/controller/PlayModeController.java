package domain.controller;

import domain.kutowerdefense.PlayModeManager;
import domain.map.DecorativeTile;
import domain.map.Lot;
import domain.map.Map;
import domain.map.PathTile;
import domain.map.Tile;
import domain.map.TileType;

public class PlayModeController {
	private static PlayModeManager playModeManager = PlayModeManager.getInstance();
	
	private PlayModeController() {}
	
	private static boolean mapExists() { return playModeManager.getCurrentMap() != null; }
	
	public static Map getCurrentMap() {
	    return playModeManager.getCurrentMap();
	}
	public static int getMapHeight() {
		if (mapExists()) return playModeManager.getCurrentMap().getHeight();
		return -1;
	}
	
	public static int getMapWidth() {
		if (mapExists()) return playModeManager.getCurrentMap().getWidth();
		return -1;
	}
	
	public static double getTileXCoord(int x, int y) {
		if (mapExists()) {
			try {
				return playModeManager.getCurrentMap().tileMap[y][x].getLocation().getXCoord();
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Error: Wrong method. Use the correct placeTile overload!");
			}
			return -1;
		} return -1;
	}
	
	public static double getTileYCoord(int x, int y) {
		if (mapExists()) {
			try {
				return playModeManager.getCurrentMap().tileMap[y][x].getLocation().getYCoord();
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Error: Wrong method. Use the correct placeTile overload!");
			}
			return -1;
		} return -1;
	}
	
	public static double getTileLength() {
		return Tile.tileLength;
	}
	
	public static String getAssetName(int x, int y) {
		String name = null;
		try {
			Tile tile = playModeManager.getCurrentMap().tileMap[y][x];
			
			if (tile.type.equals(TileType.GRASS)) {
				name = "grass";
			}
			else if (tile.getType().equals(TileType.PATH)) {
				PathTile path = (PathTile) tile;
				name = path.getPathType().getAssetName();
				
			}
			else if(tile.getType().equals(TileType.DECORATIVES)) {
				DecorativeTile decorative = (DecorativeTile) tile;
				name = decorative.getDecorativeType().getAssetName();
			}
			
			else if(tile.getType().equals(TileType.CASTLE)) {
				name = "castle";
			}
			
			else if(tile.getType().equals(TileType.TOWER)) {
				Lot tower = (Lot) tile;
				name = tower.getTowerType().getAssetName();
				
			}
			else if(tile.getType().equals(TileType.LOT)) {
				name = "lot";
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: Wrong method. Use the correct placeTile overload!");
		}
		return name;
	}
	
	public static double getGameSpeed() {
		return playModeManager.getGameSpeed();
	}
	
	public static void accelerateGame() {
		playModeManager.accelerateGame();
	}
	
	public static void decelerateGame() {
		playModeManager.decelerateGame();
	}
	
	public static void pauseGame() {
		playModeManager.pauseGame();
	}
	
	public static void resumeGame() {
		playModeManager.resumeGame();
	}
	
	public static void quitGame() {
		playModeManager.quitGame();
	}
	
	public static double getTowerRange(int x, int y) {
		try {
			Tile tile = playModeManager.getCurrentMap().tileMap[y][x];
			if (!(tile instanceof Lot)) return -1;
			
			Lot lot = (Lot) tile;
			return lot.getTower().getRange();
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public static void resetManager() {
		PlayModeManager.resetManager();
		
	}
}
