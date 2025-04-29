package domain.kutowerdefense;

import java.util.ArrayList;
import java.util.List;

import domain.entities.Enemy;
import domain.entities.Wave;
import domain.entities.WaveFactory;
import domain.map.Map;

public class PlayModeManager {
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
		this.waveLength = GameOptions.getInstance().getNumberOfWaves();
		for (int i = 0; i < waveLength; i++) waves.add(WaveFactory.createWave()); // Temporarily has 3 identical waves back to back
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
		instance.waves = new ArrayList<Wave>();
		Enemy.enemies.clear();
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
		
		if (waves.isEmpty()) 
			this.waveLength = GameOptions.getInstance().getNumberOfWaves();
			for (int i = 0; i < waveLength; i++) instance.waves.add(WaveFactory.createWave()); // Temporarily has 3 identical waves back to back
		
		timeSinceLastWave += deltaTime * gameSpeed;
		if (currentWaveIndex > 0 && timeSinceLastWave < 10) return;
		
		Wave currentWave = waves.get(currentWaveIndex);
		
		if (currentWave.spawnedAllGroups()) {
			currentWaveIndex++;
			timeSinceLastWave = 0;
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
