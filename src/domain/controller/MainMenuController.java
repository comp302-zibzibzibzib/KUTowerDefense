package domain.controller;

import java.util.ArrayList;

import domain.entities.Enemy;
import domain.kutowerdefense.GameOptions;
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
	
	public static void quitGame() {
		KUTowerDefense.quitGame();
	}
}
