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
	
	public static void resetMap() {
		mapEditor = null;
		instance = null;
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
	
	public static void createStaticMap1() {
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
	
	public static void createStaticMap2() {
		Map map_1 = new Map("Pre-Built Map 2", 9, 16);
		MapEditor me_1 = new MapEditor(map_1);
		me_1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,0,2);
		me_1.placeTile(TileType.PATH, PathType.TOPLEFT, 8, 1);
		me_1.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,1,2);
		me_1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,1,1);
		me_1.placeTile(TileType.PATH, PathType.TOPLEFT,1,0);
		me_1.placeTile(TileType.PATH, PathType.LEFT,2,0);
		me_1.placeTile(TileType.PATH, PathType.BOTTOMLEFT,3,0);
		me_1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,3,1);
		me_1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,3,2);
		me_1.placeTile(TileType.PATH, PathType.TOPRIGHT,3,3);
		me_1.placeTile(TileType.PATH, PathType.BOTTOMLEFT,4,3);
		me_1.placeTile(TileType.PATH, PathType.TOPRIGHT,4,4);
		me_1.placeTile(TileType.PATH, PathType.LEFT,5,4);
		me_1.placeTile(TileType.PATH, PathType.BOTTOMLEFT,6,4);
		me_1.placeTile(TileType.PATH, PathType.BOTTOM,6,5);
		me_1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,6,6);
		me_1.placeTile(TileType.PATH, PathType.TOP,6,7);
		me_1.placeTile(TileType.PATH, PathType.TOPRIGHT,6,8);
		me_1.placeTile(TileType.PATH, PathType.RIGHT,7,8);
		me_1.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,8,8);
		me_1.placeTile(TileType.PATH, PathType.BOTTOM,8,7);
		me_1.placeTile(TileType.PATH, PathType.BOTTOMLEFT,8,6);
		me_1.placeTile(TileType.PATH, PathType.TOPRIGHT,7,6);
		me_1.placeTile(TileType.PATH, PathType.TOP,7,5);
		me_1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,7,4);
		me_1.placeTile(TileType.PATH, PathType.TOP,7,3);
		me_1.placeTile(TileType.PATH, PathType.TOPLEFT,7,2);
		me_1.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,8,2);
		me_1.placeTile(TileType.CASTLE, 0,3);
		me_1.placeTile(TileType.CASTLE, 3, 10);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 7, 14);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.TREE2, 4, 15);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.TREE3, 0, 8);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.TREE3, 1, 6);
		me_1.placeTile(TileType.LOT, 0,15);
		me_1.placeTile(TileType.LOT, 6,9);
		me_1.placeTile(TileType.LOT, 7,1);
		me_1.placeTile(TileType.LOT, 8,0);
		me_1.placeTile(TileType.LOT, 8,4);
		me_1.placeTile(TileType.TOWER, TowerType.ARCHER, 2,1);
		me_1.placeTile(TileType.TOWER, TowerType.ARCHER, 7,9);
		me_1.placeTile(TileType.TOWER, TowerType.MAGE, 5,5);
		me_1.placeTile(TileType.TOWER, TowerType.ARTILLERY, 7,7);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.HOUSE1, 0,0);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.HOUSE2, 8,12);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.ROCK1, 7,10);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.WELL, 7,11);
		me_1.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 5,10);
		me_1.saveMap();
	}
}
