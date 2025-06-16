package domain.controller;

import domain.kutowerdefense.PlayModeManager;
import domain.map.DecorativeTile;
import domain.map.Lot;
import domain.map.Map;
import domain.map.PathTile;
import domain.map.Tile;
import domain.map.TileType;

public class PlayModeController {

	private PlayModeController() {}
	
	private static boolean mapExists() { return PlayModeManager.getInstance().getCurrentMap() != null; }
	
	public static Map getCurrentMap() {
	    return PlayModeManager.getInstance().getCurrentMap();
	}
	public static int getMapHeight() {
		if (mapExists()) return PlayModeManager.getInstance().getCurrentMap().getHeight();
		return -1;
	}
	
	public static int getMapWidth() {
		if (mapExists()) return PlayModeManager.getInstance().getCurrentMap().getWidth();
		return -1;
	}

	public static String getMapName() {
		if (mapExists()) return PlayModeManager.getInstance().getCurrentMap().getMapName();
		return null;
	}
	
	public static double getTileXCoord(int x, int y) {
		if (mapExists()) {
			try {
				return PlayModeManager.getInstance().getCurrentMap().tileMap[y][x].getLocation().getXCoord();
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Error: Wrong method. Use the correct placeTile overload!");
			}
			return -1;
		} return -1;
	}
	
	public static double getTileYCoord(int x, int y) {
		if (mapExists()) {
			try {
				return PlayModeManager.getInstance().getCurrentMap().tileMap[y][x].getLocation().getYCoord();
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
			Tile tile = PlayModeManager.getInstance().getCurrentMap().tileMap[y][x];
			
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
		return PlayModeManager.getInstance().getGameSpeed();
	}
	
	public static void accelerateGame() {
		PlayModeManager.getInstance().accelerateGame();
	}
	
	public static void decelerateGame() {
		PlayModeManager.getInstance().decelerateGame();
	}
	
	public static void pauseGame() {
		PlayModeManager.getInstance().pauseGame();
	}
	
	public static void resumeGame() {
		PlayModeManager.getInstance().resumeGame();
	}
	
	public static void quitGame() {
		PlayModeManager.getInstance().quitGame();
	}
	
	public static double getTowerRange(int x, int y) {
		try {
			Tile tile = PlayModeManager.getInstance().getCurrentMap().tileMap[y][x];
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
