package domain.kutowerdefense;

import domain.map.Map;
import domain.map.MapEditor;
import domain.services.Utilities;

public class KUTowerDefense {
	private static PlayModeManager playModeManager = PlayModeManager.getInstance();

	
	private KUTowerDefense() {} // Can't create instance of KUTowerDefense
	
	public static void newGame(String mapName) {
		Map map = Utilities.readMap(mapName);
		playModeManager.setCurrentMap(map);
	}
	
	public static void enterMapEditor() {
	}
	
	public static void openOptions() {
		
	}
	
	public static void quitGame() {
		System.exit(0);
	}
}
