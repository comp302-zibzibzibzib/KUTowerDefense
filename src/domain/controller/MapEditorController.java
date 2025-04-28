package domain.controller;

import domain.kutowerdefense.PlayModeManager;
import domain.map.Map;
import domain.map.MapEditor;
import domain.map.Tile;
import domain.map.TileType;
import domain.map.TowerType;

public class MapEditorController {
	private static MapEditor mapEditor;
	private static PlayModeManager playModeManager;
	private static MapEditorController instance;
	
	private MapEditorController() {
		Map currentMap = playModeManager.getCurrentMap();
		mapEditor = new MapEditor(currentMap);
	}
	
	public static MapEditorController getInstance() {
		if (instance == null) return new MapEditorController();
		return instance;
	}
	
	public void createArcherTower(int x, int y) {
		mapEditor.placeTile(TileType.TOWER, TowerType.ARCHER, y, x);
	}
	
	public void createMageTower(int x, int y) {
		mapEditor.placeTile(TileType.TOWER, TowerType.MAGE, y, x);
	}
	
	public void createArtilleryTower(int x, int y) {
		mapEditor.placeTile(TileType.TOWER, TowerType.ARTILLERY, x, y);
	}
	
	public void removeTower(int x, int y) {
		Tile tileToRemove = playModeManager.getCurrentMap().tileMap[y][x];
		if (tileToRemove.getType() != TileType.TOWER) return;
		
		mapEditor.removeTile(y, x);
	}
}
