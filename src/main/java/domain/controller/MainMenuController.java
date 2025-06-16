package domain.controller;

import domain.entities.Effect;
import domain.entities.Enemy;
import domain.kutowerdefense.KUTowerDefense;
import domain.kutowerdefense.PlayModeManager;
import domain.tower.Projectile;

public class MainMenuController {
	// A Façade controller that provides an easy to ue interface for starting a new game
	
	private MainMenuController() {}

	public static void startNewGame(String mapName) {
		// Complex starting a new game logic
		Enemy.enemies.clear(); // Clear any remnant enemies
		Effect.resetEffects(); // Clear any remnant effects
		Projectile.resetProjectiles(); // Clear any remnant projectiles
		GameOptionsController.initializeGameOptions();
		PlayModeManager.resetManager();
		PlayerController.resetPlayer();
		MapEditorController.resetMap();
		KUTowerDefense.newGame(mapName);
		TowerController.setTowerAttributes();
		EntityController.startEntityLogic();
		TowerController.startTowerLogic();
	}

	public static void cleanupSession() {
		// End a play mode session
		EntityController.stop();
		TowerController.stop();
		PlayModeManager.resetManager();
	}
	
	public static void quitGame() {
		// Quit the application.
		KUTowerDefense.quitGame();
	}
}
