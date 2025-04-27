package domain.controller;

import domain.kutowerdefense.KUTowerDefense;

public class MainMenuController {
	
	private MainMenuController() {}
	
	public static void startNewGame(String mapName) {
		KUTowerDefense.newGame(mapName);
	}
	
	public static void quitGame() {
		KUTowerDefense.quitGame();
	}
}
