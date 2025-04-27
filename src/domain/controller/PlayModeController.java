package domain.controller;

import domain.kutowerdefense.PlayModeManager;
import domain.map.DecorativeTile;
import domain.map.Lot;
import domain.map.Map;
import domain.map.PathTile;
import domain.map.Tile;
import domain.map.TileType;

public class PlayModeController {
	private static Map currentMap;
	private static PlayModeManager playModeManager;
	
	private PlayModeController() {}
	
	public static void setCurrentMap() {
		if (playModeManager == null) playModeManager = PlayModeManager.getInstance();
		currentMap = playModeManager.getCurrentMap();
	}
	
	public static int getMapHeight() {
		if (currentMap == null) setCurrentMap();
		return currentMap.getHeight();
	}
	
	public static int getMapWidth() {
		if (currentMap == null) setCurrentMap();
		return currentMap.getWidth();
	}
	
	public static double getTileXCoord(int x, int y) {
		if (currentMap == null) setCurrentMap();
		return currentMap.tileMap[y][x].getLocation().getXCoord();
	}
	
	public static double getTileYCoord(int x, int y) {
		if (currentMap == null) setCurrentMap();
		return currentMap.tileMap[y][x].getLocation().getYCoord();
	}
	
	public static double getTileLength() {
		return Tile.tileLength;
	}
	
	public static String getAssetName(int x, int y) {
		String name = null;
		Tile tile = currentMap.tileMap[y][x];
		
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
			name ="hugetower";
		}
		
		else if(tile.getType().equals(TileType.TOWER)) {
			Lot tower = (Lot) tile;
			name = tower.getTowerType().getAssetName();
			
		}
		else if(tile.getType().equals(TileType.LOT)) {
			name = "lot";
		}
		return name;
		
	}
}
