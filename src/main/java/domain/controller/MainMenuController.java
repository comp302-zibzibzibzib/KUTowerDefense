package domain.controller;

import domain.entities.Enemy;
import domain.kutowerdefense.KUTowerDefense;
import domain.kutowerdefense.PlayModeManager;

public class MainMenuController {
	
	private MainMenuController() {}
	
	public static void startNewGame(String mapName) {
		Enemy.enemies.clear();
		GameOptionsController.initializeGameOptions();
		MapEditorController.createStaticMap2();
		PlayModeManager.resetManager();
		PlayerController.resetPlayer();
		MapEditorController.resetMap();
		KUTowerDefense.newGame(mapName);
		EntityController.startEntityLogic();
		TowerController.startTowerLogic();
	}

	public static void cleanupSession() {
		EntityController.stop();
		TowerController.stop();
		PlayModeManager.resetManager();
	}
	
	public static void quitGame() {
		KUTowerDefense.quitGame();
	}
}
