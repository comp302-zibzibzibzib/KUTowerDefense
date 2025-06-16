package domain.entities;

import java.util.List;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;

public class Wave {
	// Responsible for the management and spawning of group objects
	// Tells group objects to start spawning enemies after a delay when the previous group is done spawning
	private int index;
	private int numberOfGroups;
	private double timeAfterGroup;
	
	private boolean startSpawning;
	
	private List<Group> groups;
	private double groupSpawnDelay;
	
	public Wave(int numberOfGroups, List<Group> groups) {
		this.numberOfGroups = numberOfGroups;
		this.groups = groups;
		this.groupSpawnDelay = GameOptions.getInstance().getGroupDelay();
		index = 0;
		timeAfterGroup = 0;
		startSpawning = false;
	}
	
	public void spawnGroups(double deltaTime) {
		// Command group instances to start spawning their enemies when the amount of time elapsed since last spawn is enough
		if (!startSpawning || spawnedAllGroups()) return;
		timeAfterGroup += deltaTime * PlayModeManager.getInstance().getGameSpeed(); //amount of time passed since first spawn
		
		if(groups.get(index).spawnedAllEnemies()) {
			index++;
			timeAfterGroup = 0;
		}
		
		if(!spawnedAllGroups() && !groups.get(index).isSpawning()
				&& timeAfterGroup > groupSpawnDelay) { //first delay should be 0
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
