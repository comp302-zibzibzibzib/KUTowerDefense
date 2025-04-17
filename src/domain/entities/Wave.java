package domain.entities;

import java.util.List;

public class Wave {
	private int numberofGroups;
	private List<Group> groups;
	private List<Float> groupSpawnDelays;
	
	public Wave(int numberofGroups, List<Group> groups, List<Float> groupSpawnDelays) {
		this.numberofGroups = numberofGroups;
		this.groups = groups;
		this.groupSpawnDelays = groupSpawnDelays;
	}
	
	public void spawnGroups() {//stub
		
	}

	public int getNumberofGroups() {
		return numberofGroups;
	}

	public void setNumberofGroups(int numberofGroups) {
		this.numberofGroups = numberofGroups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Float> getGroupSpawnDelays() {
		return groupSpawnDelays;
	}

	public void setGroupSpawnDelays(List<Float> groupSpawnDelays) {
		this.groupSpawnDelays = groupSpawnDelays;
	}
	
	
	
	
}
