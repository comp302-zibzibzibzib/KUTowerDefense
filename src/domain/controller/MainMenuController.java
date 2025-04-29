package domain.controller;

import java.util.ArrayList;

import domain.entities.Enemy;
import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.KUTowerDefense;

public class MainMenuController {
	
	private MainMenuController() {}
	
	public static void startNewGame(String mapName) {
		Enemy.enemies.clear();
		GameOptions.initializeGameOptions();
		MapEditorController.createStaticMap();
		KUTowerDefense.newGame(mapName);
	}
	
	public static void initOptions() {
		GameOptions.initializeGameOptions();
	}
	
	public static void quitGame() {
		KUTowerDefense.quitGame();
	}
}
