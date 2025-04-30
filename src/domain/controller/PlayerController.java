package domain.controller;

import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;

public class PlayerController {
	private static Player player = Player.getInstance();
	private static PlayModeManager playModeManager = PlayModeManager.getInstance();
	
	public static int getPlayerLives() {
		return player.getLives();
	}
	
	public static int getPlayerGold() {
		return player.getGold();
	}
	
	public static int getWaveNumber() {
		return player.getWaveNumber();
	}
	
	public static int getTotalNumberOfWaves() {
		return playModeManager.getTotalNumberOfWaves();
	}
	
}
