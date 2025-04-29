package domain.controller;

import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.DecorativeType;
import domain.map.Lot;
import domain.map.Map;
import domain.map.MapEditor;
import domain.map.PathType;
import domain.map.Tile;
import domain.map.TileType;
import domain.map.TowerType;
import domain.services.Utilities;
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
		mapEditor.placeTile(TileType.TOWER, TowerType.MAGE, y, x);
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
		player.updateGold((int) (((Lot)tileToRemove).getTower().getCost() * 0.6));
	}
	
	public static void createStaticMap() {
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
	}
}
