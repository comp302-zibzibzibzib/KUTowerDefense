package domain.entities;

import java.util.List;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;

public class Group {
	// Group is responsible for spawning enemies with a delay that gets shorter as the game approaches the ending wave
	private int numberOfEnemies;
	private int index;
	private double timeAfterEnemySpawn;
	
	private boolean startSpawning;
	
	private List<Enemy> composition;
	
	public Group(int numberOfEnemies, List<Enemy> composition) {
		this.numberOfEnemies = numberOfEnemies;
		this.composition = composition;
		index = 0;
		timeAfterEnemySpawn = 0;
		startSpawning = false;
	}
	
	public void initializeEnemies(double deltaTime) {
		// Spawn enemies if given the command to start spawning
		if (!startSpawning || spawnedAllEnemies()) return;

		// Enough time has to pass between enemy spawns to spawn a new one
		// The delay gets cutoff more and more calculated as the percentage of the current wave to the end wave grows larger
		double delay = 2;
		double delayCutoff = (delay * 0.7) * (double) Player.getInstance().getWaveNumber() / (double) GameOptions.getInstance().getNumberOfWaves();
		delay -= delayCutoff;
		
		timeAfterEnemySpawn += deltaTime * PlayModeManager.getInstance().getGameSpeed(); //amount of time passed since first spawn
		
		if(timeAfterEnemySpawn >= delay) {
			//set location of enemy at start
			Enemy enemy = composition.get(index);
			enemy.initialize();
			
			timeAfterEnemySpawn = 0.0;
			index++;
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNumberOfEnemies() {
		return numberOfEnemies;
	}

	public void setNumberOfEnemies(int numberOfEnemies) {
		this.numberOfEnemies = numberOfEnemies;
	}

	public List<Enemy> getComposition() {
		return composition;
	}

	public void setComposition(List<Enemy> composition) {
		this.composition = composition;
	}
	
	public boolean spawnedAllEnemies() {
		return index == numberOfEnemies;
	}
	
	public void startSpawning() {
		this.startSpawning = true;
	}
	
	public boolean isSpawning() {
		return startSpawning;
	}
}
