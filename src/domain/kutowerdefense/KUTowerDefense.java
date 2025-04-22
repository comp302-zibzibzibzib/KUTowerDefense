package domain.kutowerdefense;

import domain.map.Map;
import domain.map.MapEditor;

public class KUTowerDefense {
	private Map currentMap;
	public static PlayModeManager playModeManager = new PlayModeManager() ;

	
	private KUTowerDefense() {} // Can't create instance of KUTowerDefense
	
	public static void newGame(Map map) {
		playModeManager.setCurrentMap(map);
	}
	
	public static void enterMapEditor() {
		MapEditor editor = new MapEditor(); // idk
	}
	
	public static void openOptions() {
		
	}
	
	public static void quitGame() {
		System.exit(0);
	}
	public void setCurrentMap(Map map) {
	    this.currentMap = map;
	}

}
