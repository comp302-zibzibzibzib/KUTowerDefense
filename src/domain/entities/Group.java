package domain.entities;

import java.util.List;

import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;

class EnemyMover implements Runnable{
	private Enemy enemy;
	
	protected EnemyMover(Enemy enemy) {
		this.enemy = enemy;
	}
	@Override
	public void run() {
		enemy.moveEnemy();
	}
	
}

public class Group {
	private int numberOfEnemies;
	private List<Enemy> composition;
	
	public Group(int numberOfEnemies, List<Enemy> composition) {
		this.numberOfEnemies = numberOfEnemies;
		this.composition = composition;
	}
	
	public void initializeEnemies() {//MIGHT NOT WORK
		Location startLocation = PlayModeManager.getInstance().getCurrentMap().getStartingTile().getLocation();
		int delay = 500;
		if(PlayModeManager.getInstance().getGameSpeed() == 2.0) {
			delay = 250;
		}
		for(int i = 0; i < numberOfEnemies; i++) {
			Enemy enemy = composition.get(i);
			enemy.getLocation().xCoord = startLocation.xCoord;
			enemy.getLocation().yCoord = startLocation.yCoord;
			EnemyMover mover = new EnemyMover(enemy); //Creating thread for every enemy might cause problems
			Thread enemyMoverThread = new Thread(mover);
			enemyMoverThread.start();
			try {
				Thread.sleep(delay); //should be executed on another thread(init by GroupSpawner thread) instead of main thread
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		}
		
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
}
