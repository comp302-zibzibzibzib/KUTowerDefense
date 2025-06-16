package domain.controller;

import java.util.function.Consumer;

import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;

public class PlayerController {
	// Controller responsible for ui and player singleton communication
	// Retrieves information from the player or adds listeners to player listener fields in Player.java
	// Helps with observer pattern
	
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
	
	public static void addGoldListener(Consumer<Integer> consumer) {
		player.getGoldListener().addListener(consumer);
	}
	
	public static void addLivesListener(Consumer<Integer> consumer) {
		player.getLivesListener().addListener(consumer);
	}
	
	public static void addWaveNumberListener(Consumer<Integer> consumer) {
		player.getWaveNumberListener().addListener(consumer);
	}
	
	public static void resetPlayer() {
		player.resetPlayer();
	}
}
