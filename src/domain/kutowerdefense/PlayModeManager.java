package domain.kutowerdefense;

import java.util.List;

import domain.entities.Wave;

public class PlayModeManager {
	private static PlayModeManager instance;
	
	private int currentWaveIndex;
	private double previousGameSpeed = 1.0;
	private double gameSpeed = 1.0;
	
	private List<Wave> waves;
	
	private PlayModeManager() {
		this.currentWaveIndex = 0; this.gameSpeed = 1.0; this.previousGameSpeed = 1.0;
	}
	
	public static PlayModeManager getInstance() {
		if (instance == null) instance = new PlayModeManager();
		return instance;
	}
	
	public static void resetManager() {
		if (instance == null) instance = new PlayModeManager();
		instance.currentWaveIndex = 0;
		instance.gameSpeed = 1.0;
		instance.previousGameSpeed = 1.0;
		instance.waves = null;
	}
	
	public static void accelerateGame() {
		if (instance == null) instance = new PlayModeManager();
		instance.gameSpeed = 2.0;
	}
	
	public static void decelerateGame() {
		if (instance == null) instance = new PlayModeManager();
		instance.gameSpeed = 1.0;
	}
	
	public static void pauseGame() {
		if (instance == null) instance = new PlayModeManager();
		instance.previousGameSpeed = instance.gameSpeed;
		instance.gameSpeed = 0;
	}
	
	public static void resumeGame() {
		if (instance == null) instance = new PlayModeManager();
		if (instance.gameSpeed == 0) {
			instance.gameSpeed = instance.previousGameSpeed;
		}
	}
	
	public static void returnToMainMenu() {
		
	}
	
	public static void quitGame() {
		System.exit(0);
	}
	
	public static void initializeWaves() {
		if (instance == null) instance = new PlayModeManager();
		/*GameOptions.getInstance();
		Will create waves of groups, IMPLEMENT GAME OPTIONS FOR WAVE INITIALIZATION PARAMETERS*/
	}
}
