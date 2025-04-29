package domain.controller;

import domain.kutowerdefense.GameOptions;
import domain.services.Utilities;

public class GameOptionsController {
	private static GameOptions options = GameOptions.getInstance();
	
	public static void initializeGameOptions() {
		GameOptions.initializeGameOptions();
		options = GameOptions.getInstance();
	}
	
	public static int getStartingLives() {
		return options.getStartingPlayerLives();
	}
	
	public static int getStartingGold() {
		return options.getStartingPlayerGold();
	}
	
	public static int getNumberOfWaves() {
		return options.getNumberOfWaves();
	}
	
	public static double getEnemySpeed() {
		return options.getEnemySpeed();
	}
	
	public static void setStartingLives(int value) {
		options.setStartingPlayerLives(value);
	}
	
	public static void setStartingGold(int value) {
		options.setStartingPlayerGold(value);
	}
	
	public static void setNumberOfWaves(int value) {
		options.setNumberOfWaves(value);
	}
	
	public static void setEnemySpeed(double value) {
		options.setEnemySpeed(value);
	}
	
	public static void saveOptions() {
		Utilities.writeOptions();
	}
	
	public static void resetOptions() {
		options.resetOptions();
	}
}
