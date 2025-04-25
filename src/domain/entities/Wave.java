package domain.entities;

import java.util.List;

class GroupSpawner implements Runnable { 
	//NOT THREAD SAFE, MIGHT NEED TO CHANGE EVERY LIST IN ENTITIES TO THREAD SAFE VERS.
	//MIGHT NEED TO ADD VOLATILE KEYWORD TO VARIABLES IN GROUP AND ENEMY
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
	
}


public class Wave {
	private int numberOfGroups;
	private List<Group> groups;
	private List<Double> groupSpawnDelays;
	
	public Wave(int numberOfGroups, List<Group> groups, List<Double> groupSpawnDelays) {
		this.numberOfGroups = numberOfGroups;
		this.groups = groups;
		this.groupSpawnDelays = groupSpawnDelays;
	}
	
	public void spawnGroups() {//might not work
		GroupSpawner spawner = new GroupSpawner(numberOfGroups, groups, groupSpawnDelays);
		spawner.run();
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
