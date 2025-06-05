package domain.entities;

import java.util.List;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.Tile;
import domain.services.Utilities;

/*class EnemyMover implements Runnable{ //deprecated class
	private Enemy enemy;
	
	protected EnemyMover(Enemy enemy) {
		this.enemy = enemy;
	}
	@Override
	public void run() {
		enemy.moveEnemy();
	}
	
}*/

public class Group {
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
	
	public void initializeEnemies(double deltaTime) {//MIGHT NOT WORK
		if (!startSpawning || spawnedAllEnemies()) return;
		
		double delay = 1;
		double delayCutoff = (delay * 0.5) * (double) Player.getInstance().getWaveNumber() / (double) GameOptions.getInstance().getNumberOfWaves();
		delay -= delayCutoff;
		
		timeAfterEnemySpawn += deltaTime * PlayModeManager.getInstance().getGameSpeed(); //amount of time passed since first spawn
		
		if(timeAfterEnemySpawn >= delay) {
			//set location of enemy at start and move
			Enemy enemy = composition.get(index);
			enemy.initialize();
			//System.out.printf("Initializing enemy%o!%n", index + 1);
			
			//enemy.moveEnemy();
			
			timeAfterEnemySpawn = 0.0;
			index++;
		}
		
		//deprecated code
		//for(int i = 0; i < numberOfEnemies; i++) {
		//	Enemy enemy = composition.get(i);
		//	enemy.getLocation().xCoord = startLocation.xCoord;
		//	enemy.getLocation().yCoord = startLocation.yCoord;
		//	EnemyMover mover = new EnemyMover(enemy); //Creating thread for every enemy might cause problems
		//	Thread enemyMoverThread = new Thread(mover);
		//	enemyMoverThread.start();
		//	try {
		//		Thread.sleep(delay); 
		//should be executed on another thread(init by GroupSpawner thread) instead of main thread
		//	} catch (InterruptedException e) {
		//		e.printStackTrace();
		//	}
		//}
		
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
