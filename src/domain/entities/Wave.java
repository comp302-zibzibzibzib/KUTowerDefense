package domain.entities;

import java.util.List;

import domain.kutowerdefense.PlayModeManager;
import javafx.animation.AnimationTimer;

/*class GroupSpawner implements Runnable { //Deprecated Class, left just in case
	//NOT THREAD SAFE, MIGHT NEED TO CHANGE EVERY LIST IN ENTITIES TO THREAD SAFE VERS.
	//MIGHT NEED TO ADD VOLATILE KEYWORD TO VARIABLES IN GROUP AND ENEMY
	//WHEN GAME IS PAUSED MUST PAUSE THIS TOO
	private int numberOfGroups;
	private List<Group> groups;
	private List<Double> groupSpawnDelays;
	
	protected GroupSpawner(int numberOfGroups, List<Group> groups, List<Double> groupSpawnDelays) {
		this.numberOfGroups = numberOfGroups;
		this.groupSpawnDelays = groupSpawnDelays;
		this.groups = groups;
	}

	@Override
	public void run() {
		for(int i = 0; i < numberOfGroups; i++) {
			groups.get(i).initializeEnemies();
			
			try {
				Thread.sleep((long) (groupSpawnDelays.get(i)*1000)); //does not really like casting Double to long
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
}*/


public class Wave {
	private int numberOfGroups;
	private List<Group> groups;
	private List<Double> groupSpawnDelays;
	
	public Wave(int numberOfGroups, List<Group> groups, List<Double> groupSpawnDelays) {
		this.numberOfGroups = numberOfGroups;
		this.groups = groups;
		this.groupSpawnDelays = groupSpawnDelays;
	}
	
	public void spawnGroups() {//MIGHT NOT WORK
		
		AnimationTimer groupSpawnerTimer = new AnimationTimer() {
			private long lastUpdate = 0; //last time handle was called
			private double timeAfterGroup = 0.0;
			int index = 0;

			@Override
			public void handle(long now) { //now is system time
				if(lastUpdate > 0) { //skips first frame to not cause problems
					
					double deltaSecond = (now - lastUpdate)/1_000_000_000.0;// should be 1/60 of a second
					timeAfterGroup += deltaSecond * PlayModeManager.getInstance().getGameSpeed(); //amount of time passed since first spawn
					
					if(timeAfterGroup > groupSpawnDelays.get(index)) { //first delay should be 0
						groups.get(index).initializeEnemies();
						timeAfterGroup = 0.0;//resets amount of time passed and increases index
						index++;
						if(index == numberOfGroups) {
							//idk if ending this timer causes issues with move enemy
							//finished spawning every group
							this.stop();
						}
					}
					
				}
				lastUpdate = now;
			}
			
		};
		groupSpawnerTimer.start();
		
		//deprecated code!
		//GroupSpawner spawner = new GroupSpawner(numberOfGroups, groups, groupSpawnDelays);
		//Thread GroupSpawnerThread = new Thread(spawner);
		//GroupSpawnerThread.start();
	}

	public int getNumberofGroups() {
		return numberOfGroups;
	}

	public void setNumberofGroups(int numberofGroups) {
		this.numberOfGroups = numberofGroups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Double> getGroupSpawnDelays() {
		return groupSpawnDelays;
	}

	public void setGroupSpawnDelays(List<Double> groupSpawnDelays) {
		this.groupSpawnDelays = groupSpawnDelays;
	}
	
	
	
	
}
