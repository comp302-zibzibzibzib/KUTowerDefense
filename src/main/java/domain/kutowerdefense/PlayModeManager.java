package domain.kutowerdefense;

import java.util.ArrayList;
import java.util.List;

import domain.entities.Enemy;
import domain.entities.Wave;
import domain.entities.WaveFactory;
import domain.map.Map;

public class PlayModeManager {
	// Manages game speed/pause
	// Initializes waves sequentially with pre determined delays
	// Gives access to the current map which is being played on
	public static final double GRACE_PERIOD_SECONDS = 4;
	
	private static PlayModeManager instance;
	private Map currentMap;
	private int currentWaveIndex;
	private double previousGameSpeed = 1.0;
	private double gameSpeed = 1.0;
	
	private List<Wave> waves;
	private int waveLength = 0;
	private double timeSinceLastWave = 0;

	private PlayModeManager() {
		this.currentWaveIndex = 0; this.gameSpeed = 1.0; this.previousGameSpeed = 1.0;
		this.waves = new ArrayList<Wave>();
		Enemy.enemies.clear();
		Enemy.resetID();
		this.waveLength = GameOptions.getInstance().getNumberOfWaves();
		for (int i = 0; i < waveLength; i++) waves.add(WaveFactory.createWave(i + 1));
	}
	
	public static PlayModeManager getInstance() {
		if (instance == null) instance = new PlayModeManager();
		return instance;
	}
	
	public static void resetManager() {
		if (instance == null) instance = new PlayModeManager();
		instance.currentMap = null;
		instance.currentWaveIndex = 0;
		instance.timeSinceLastWave = 0;
		instance.gameSpeed = 1.0;
		instance.previousGameSpeed = 1.0;
		instance.waveLength = GameOptions.getInstance().getNumberOfWaves();
		instance.waves = new ArrayList<Wave>();
		Enemy.enemies.clear();
		Enemy.resetID();
		for (int i = 0; i < instance.waveLength; i++) instance.waves.add(WaveFactory.createWave(i + 1));
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
	
	public void initializeWaves(double deltaTime) {
		if (spawnedAllWaves()) return;
		
		if (waves.isEmpty()) {
			Enemy.enemies.clear();
			Enemy.resetID();
			this.waveLength = GameOptions.getInstance().getNumberOfWaves();
			for (int i = 0; i < waveLength; i++) instance.waves.add(WaveFactory.createWave(i + 1));
		}
		
		timeSinceLastWave += deltaTime * gameSpeed;
		// Change the wave number when it is almost time to spawn a new one
		if (currentWaveIndex > 0 && timeSinceLastWave < 10) {
			if (timeSinceLastWave > 8) Player.getInstance().setWaveNumber(currentWaveIndex+1);
			else return;
		}
		
		Wave currentWave = waves.get(currentWaveIndex);

		// If the current wave spawned all of its groups move on to the next one
		if (currentWave.spawnedAllGroups()) {
			currentWaveIndex++;
			timeSinceLastWave = 0;
			return;
		}	
		
		if (!currentWave.isSpawning() && !spawnedAllWaves()) {
			currentWave.startSpawning();
			//System.out.printf("Initializing wave%o!%n", currentWaveIndex + 1);
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
	
	public int getCurrentWaveIndex() {
		return currentWaveIndex;
	}
	
	public int getTotalNumberOfWaves() {
		return waveLength;
	}
	
	public boolean spawnedAllWaves() {
		return currentWaveIndex == waves.size();
	}
	
	public List<Wave> getWaves() {
		return waves;
	}
}
