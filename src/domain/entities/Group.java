package domain.entities;

import java.util.List;

public class Group {
	private int numberOfEnemies;
	private List<Enemy> composition;
	
	public Group(int numberOfEnemies, List<Enemy> composition) {
		this.numberOfEnemies = numberOfEnemies;
		this.composition = composition;
	}
	
	public void initializeEnemies() {//stub
		
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
