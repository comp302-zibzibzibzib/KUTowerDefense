package domain.controller;

import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Lot;
import domain.map.Map;
import domain.map.MapEditor;
import domain.map.Tile;
import domain.map.TileType;
import domain.map.TowerType;
import domain.tower.TowerFactory;

public class MapEditorController {
	private static MapEditor mapEditor;
	private static Player player = Player.getInstance();
	private static PlayModeManager playModeManager = PlayModeManager.getInstance();
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

		if (player.getGold() < TowerFactory.costArcher) return;
		mapEditor.removeTile(y, x);
		mapEditor.placeTile(TileType.TOWER, TowerType.ARCHER, y, x);
		player.updateGold(-TowerFactory.costArcher);
	}
	
	public void createMageTower(int x, int y) {
		if (player.getGold() < TowerFactory.costMage) return;

		mapEditor.removeTile(y, x);
		mapEditor.placeTile(TileType.TOWER, TowerType.ARCHER, y, x);
		Map.printMap(playModeManager.getCurrentMap());
		player.updateGold(-TowerFactory.costMage);
	}

	
	public void createArtilleryTower(int x, int y) {
		if (player.getGold() < TowerFactory.costArtillery) return;
		mapEditor.removeTile(y, x);
		mapEditor.placeTile(TileType.TOWER, TowerType.ARTILLERY, y, x);
		player.updateGold(-TowerFactory.costArtillery);

		
	}
	
	public void removeTower(int x, int y) {
		Tile tileToRemove = playModeManager.getCurrentMap().tileMap[y][x];
		if (tileToRemove.getType() != TileType.TOWER) return;
		mapEditor.removeTile(y, x);
		player.updateGold(((Lot)tileToRemove).getTower().getCost());

	}
}
