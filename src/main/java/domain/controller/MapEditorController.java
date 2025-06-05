package domain.controller;

import java.util.HashMap;
import java.util.Set;
import java.util.*;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.DecorativeType;
import domain.map.Location;
import domain.map.Lot;
import domain.map.Map;
import domain.map.MapEditor;
import domain.map.PathTile;
import domain.map.PathType;
import domain.map.Tile;
import domain.map.TileType;
import domain.map.TowerType;
import domain.services.Utilities;
import domain.tower.TowerFactory;

public class MapEditorController {
	private Map forcedMap;
	private static MapEditor mapEditor;
	private static Player player = Player.getInstance();
	private static PlayModeManager playModeManager = PlayModeManager.getInstance();
	private static MapEditorController instance;
	private static HashMap<String, PathType> paths = new HashMap<String, PathType>();
	private static HashMap<String, TowerType> towers = new HashMap<String, TowerType>();
	private static HashMap<String, DecorativeType> decoratives = new HashMap<String, DecorativeType>();
	private static PathType[] pathSet = PathType.values(); 
	private static DecorativeType[] decorativesSet = DecorativeType.values();
	private static TowerType[] towersSet = TowerType.values();
	
	private MapEditorController(boolean editorMode) {
		Map usedMap;
		if(editorMode){
			forcedMap = new Map(9, 16);
			usedMap = forcedMap;
		}
		else{
			usedMap = PlayModeManager.getInstance().getCurrentMap();
		}
		//Map currentMap = playModeManager.getCurrentMap();
		mapEditor = new MapEditor(usedMap);
		
		for(PathType path : pathSet) {
			paths.put(path.getAssetName(), path);
		}
		for(TowerType tower : towersSet) {
			towers.put(tower.getAssetName(), tower);
		}
		for(DecorativeType decorative : decorativesSet) {
			decoratives.put(decorative.getAssetName(), decorative);
		}
	}
	
	public static void resetMap() {
		mapEditor = null;
		instance = null;
	}
	
	public static MapEditorController getInstance(boolean editorMode) {
		if (instance == null) instance = new MapEditorController(editorMode);
		return instance;
	}

	public void createArcherTower(int x, int y) {
		if (player.getGold() < GameOptions.getInstance().getArcherCost()) return;
		
		mapEditor.removeTile(y, x);
		mapEditor.placeTile(TileType.TOWER, TowerType.ARCHER, y, x);
		player.updateGold(-GameOptions.getInstance().getArcherCost());
	}
	
	public void createMageTower(int x, int y) {
		if (player.getGold() < GameOptions.getInstance().getMageCost()) return;

		mapEditor.placeTile(TileType.TOWER, TowerType.MAGE, y, x);
		player.updateGold(-GameOptions.getInstance().getMageCost());
	}

	public void createArtilleryTower(int x, int y) {
		if (player.getGold() < GameOptions.getInstance().getArtilleryCost()) return;
		
		mapEditor.removeTile(y, x);
		mapEditor.placeTile(TileType.TOWER, TowerType.ARTILLERY, y, x);
		player.updateGold(-GameOptions.getInstance().getArtilleryCost());
	}
	
	public void removeTower(int x, int y) {
		Tile tileToRemove = playModeManager.getCurrentMap().tileMap[y][x];
		if (tileToRemove.getType() != TileType.TOWER) return;
		player.updateGold((int) (((Lot)tileToRemove).getTower().getCost() * 0.6));
		mapEditor.removeTile(y, x);
	}
	
	public void forcePlaceTile(String name, int x, int y) {
		mapEditor.removeTile(y, x);
		PathType pathType = paths.get(name);
		TowerType towerType = towers.get(name);
		DecorativeType decorativeType = decoratives.get(name);
		
		if (pathType != null){
			mapEditor.placeTile(TileType.PATH,pathType, y, x);

		}
		else if (towerType != null) {
			mapEditor.placeTile(TileType.TOWER, towerType, y, x);

		}
		else if(decorativeType != null) {
			mapEditor.placeTile(TileType.DECORATIVES,decorativeType, y, x);
		}
		else {
			mapEditor.placeTile(TileType.GRASS, y, x);
		}
	}
	
	public void forcePlaceCastle(int x, int y) {
		mapEditor.removeTile(y, x);
		mapEditor.placeTile(TileType.CASTLE,y,x);
	}

	public void forcePlaceLot(int x, int y) {
		mapEditor.removeTile(y, x);
		mapEditor.placeTile(TileType.LOT,y,x);
	}

	public void removeTile(int x, int y) {
		mapEditor.removeTile(y, x);
	}
	
	public void printMap() {
		Map.printMap(forcedMap);
	}
	public boolean validateMap() {
		return mapEditor.isValidMap();
	}
	public void saveMap(String name) {
		Map newMap = forcedMap;
		newMap.mapName = name;
		Utilities.writeMap(newMap);
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
		me_1.addEndingTile(0, 2);
		me_1.placeTile(TileType.PATH, PathType.TOPLEFT, 8, 1);
		me_1.addStartingTile(8, 1);
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

	public static void createStaticMap3() {
		Map map = new Map("Multiple Way Map", 9, 16);
		MapEditor me = new MapEditor(map);

		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0, 8);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0, 12);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1, 8);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1, 12);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 2, 8);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 2, 12);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 3, 8);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 3, 12);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 0);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 1);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 2);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 3);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 4);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 5);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 6);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 7);
		me.placeTile(TileType.PATH, PathType.TJUNCNOBOTTOM, 4, 8);
		me.placeTile(TileType.PATH, PathType.TJUNCNOTOP, 4, 9);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 10);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 11);
		me.placeTile(TileType.PATH, PathType.TJUNCNOBOTTOM, 4, 12);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 13);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 14);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 4, 15);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 5, 9);
		me.placeTile(TileType.PATH, PathType.TOPLEFT, 6, 7);
		me.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 6, 8);
		me.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 6, 9);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 7, 7);
		me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 8, 7);
		me.addEndingTile(0, 8);
		me.addEndingTile(0, 12);
		me.addStartingTile(4, 0);
		me.addStartingTile(4, 15);
		me.addStartingTile(8, 7);

		me.placeTile(TileType.TOWER, TowerType.ARCHER, 3, 7);
		me.placeTile(TileType.LOT, 3,  9);
		me.placeTile(TileType.LOT, 3, 11);
		me.placeTile(TileType.LOT, 5, 8);
		me.placeTile(TileType.LOT, 3, 3);
		me.placeTile(TileType.LOT, 3, 14);
		me.placeTile(TileType.LOT, 5, 13);
		me.placeTile(TileType.LOT, 3, 3);
		me.placeTile(TileType.LOT, 3, 11);

		me.placeTile(TileType.CASTLE, 0, 9);

		me.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 0, 1);
		me.placeTile(TileType.DECORATIVES, DecorativeType.TREE3, 6, 2);
		me.placeTile(TileType.DECORATIVES, DecorativeType.TREE2, 6, 5);
		me.placeTile(TileType.DECORATIVES, DecorativeType.TREE3, 1, 14);
		me.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 7, 10);
		me.placeTile(TileType.DECORATIVES, DecorativeType.TREE2, 8, 10);
		me.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 3, 10);

		me.placeTile(TileType.DECORATIVES, DecorativeType.ROCK1, 1, 1);
		me.placeTile(TileType.DECORATIVES, DecorativeType.ROCK1, 8, 3);
		me.placeTile(TileType.DECORATIVES, DecorativeType.ROCK2, 7, 5);

		me.placeTile(TileType.DECORATIVES, DecorativeType.HOUSE1, 1, 4);
		me.placeTile(TileType.DECORATIVES, DecorativeType.HOUSE2, 6, 13);
		me.placeTile(TileType.DECORATIVES, DecorativeType.WELL, 6, 14);
		me.placeTile(TileType.DECORATIVES, DecorativeType.WOOD, 1, 3);

		me.saveMap();
	}
}
