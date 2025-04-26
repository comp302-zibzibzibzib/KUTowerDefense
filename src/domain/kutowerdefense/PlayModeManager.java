package domain.kutowerdefense;

import java.util.List;
import java.util.concurrent.*;

import domain.entities.Enemy;
import domain.entities.Wave;
import domain.map.Map;

public class PlayModeManager {
	private static PlayModeManager instance;
	private Map currentMap;
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
		instance.currentMap = null;
	}
	
	public void accelerateGame() {
		this.gameSpeed = 2.0;
	}
	
	public void decelerateGame() {
		this.gameSpeed = 1.0;
	}
	
	public void pauseGame() {
		this.previousGameSpeed = this.gameSpeed;
		this.gameSpeed = 0;
	}
	
	public void resumeGame() {
		if (this.gameSpeed == 0) {
			this.gameSpeed = this.previousGameSpeed;
		}
	}
	
	public void returnToMainMenu() {
		resetManager();
	}
	
	public void quitGame() {
		System.exit(0);
	}
	
	public void initializeWaves() {
		/*GameOptions.getInstance();
		Will create waves of groups, IMPLEMENT GAME OPTIONS FOR WAVE INITIALIZATION PARAMETERS*/
		
		///Delays new wave for 4 seconds than starts them in another thread (Can be put somewhere else)
		if(Enemy.enemies.isEmpty()) {//if condition can be put somewhere else
			int delay = 4;
			
			if(this.gameSpeed == 2.0) {
				delay = 2;
			}
			
			ScheduledExecutorService newWaveScheduler = Executors.newSingleThreadScheduledExecutor();
			newWaveScheduler.schedule(() -> {
				Wave currentWave = waves.get(currentWaveIndex);
				currentWave.spawnGroups();
				currentWaveIndex++;
				
			}, delay, TimeUnit.SECONDS);
			
			
		}
		
	}
	
	public void setCurrentMap(Map map) {
	    this.currentMap = map;
	}

	public Map getCurrentMap() {
	    return currentMap;
	}
	
	public double getGameSpeed() {
		return this.gameSpeed;
	}
}
