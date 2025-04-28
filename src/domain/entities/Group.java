package domain.entities;

import java.util.List;

import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;
import javafx.animation.AnimationTimer;

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
	private List<Enemy> composition;
	
	public Group(int numberOfEnemies, List<Enemy> composition) {
		this.numberOfEnemies = numberOfEnemies;
		this.composition = composition;
	}
	
	public void initializeEnemies() {//MIGHT NOT WORK
		Location startLocation = PlayModeManager.getInstance().getCurrentMap().getStartingTile().getLocation();
		double delay = 0.4;
		
		AnimationTimer enemyInitTimer = new AnimationTimer() {
			private long lastUpdate = 0; //last time handle was called
			private double timeAfterEnemySpawn = 0.0;
			int index = 0;

			@Override
			public void handle(long now) {
				if(lastUpdate > 0) { //skips first frame to not cause problems
					
					double deltaSecond = (now - lastUpdate)/1_000_000_000.0;// should be 1/60 of a second
					timeAfterEnemySpawn += deltaSecond * PlayModeManager.getInstance().getGameSpeed(); //amount of time passed since first spawn
					
					if(timeAfterEnemySpawn >= delay) { 
						//set location of enemy at start and move
						Enemy enemy =  composition.get(index);
						enemy.getLocation().xCoord = startLocation.xCoord;
						enemy.getLocation().yCoord = startLocation.yCoord;
						
						//enemy.moveEnemy();
						
						timeAfterEnemySpawn = 0.0;
						index++;
						
						if(index == numberOfEnemies) {
							this.stop();
						}
					}
					
					
				}
				lastUpdate = now;
				
			}
			
		};
		enemyInitTimer.start();
		
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
