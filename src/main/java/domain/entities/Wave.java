package domain.entities;

import java.util.List;

import domain.kutowerdefense.PlayModeManager;

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
	private int index;
	private int numberOfGroups;
	private double timeAfterGroup;
	
	private boolean startSpawning;
	
	private List<Group> groups;
	private List<Double> groupSpawnDelays;
	
	public Wave(int numberOfGroups, List<Group> groups, List<Double> groupSpawnDelays) {
		this.numberOfGroups = numberOfGroups;
		this.groups = groups;
		this.groupSpawnDelays = groupSpawnDelays;
		index = 0;
		timeAfterGroup = 0;
		startSpawning = false;
	}
	
	public void spawnGroups(double deltaTime) {//MIGHT NOT WORK
		if (!startSpawning || spawnedAllGroups()) return;
		timeAfterGroup += deltaTime * PlayModeManager.getInstance().getGameSpeed(); //amount of time passed since first spawn
		
		if(groups.get(index).spawnedAllEnemies()) {
			index++;
			timeAfterGroup = 0;
		}
		
		if(!spawnedAllGroups() && !groups.get(index).isSpawning()
				&& timeAfterGroup > groupSpawnDelays.get(index)) { //first delay should be 0
			groups.get(index).startSpawning();
			//System.out.printf("Initializing group%o!%n", index + 1);
			timeAfterGroup = 0.0;//resets amount of time passed and increases index
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
	
	public boolean spawnedAllGroups() {
		return index == numberOfGroups;
	}
	
	public void startSpawning() {
		this.startSpawning = true;
	}
	
	public boolean isSpawning() {
		return startSpawning;
	}
}
