package domain.kutowerdefense;

import domain.map.Map;
import domain.services.Utilities;

public class KUTowerDefense {
	
	private KUTowerDefense() {} // Can't create instance of KUTowerDefense
	
	public static void newGame(String mapName) {
		Map map = Utilities.readMap(mapName);
		PlayModeManager.getInstance().setCurrentMap(map);
	}

	public static void quitGame() {
		System.exit(0);
	}
}
