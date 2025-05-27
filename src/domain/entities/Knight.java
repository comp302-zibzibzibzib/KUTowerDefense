package domain.entities;

import domain.map.Location;
import domain.map.Tile;
import domain.services.Utilities;

public class Knight extends Enemy {
	
	double arrowDamageReduction;
  boolean spedUp;
  
	public Knight(double hitPoints, double speed, Location location, double arrowDamageReduction) {
		super(hitPoints, speed, location);
		this.arrowDamageReduction = arrowDamageReduction;
    spedUp = false;
	}

	//if distance between a knight and goblin less than width of tile speed up knight
	//might need a place to store default knight speed
	public void knightSpeedUp() {
		boolean goblinNear = false;
		for( Enemy enemy : Enemy.getAllEnemies()) {
			if(enemy instanceof Goblin) {
				double distance = Utilities.euclideanDistance(this.location, enemy.getLocation());
				if(distance < Tile.tileLength) {
					if(this.spedUp == false) {
						goblinNear = true;
						this.spedUp = true;
						this.speed = (this.speed+enemy.speed)/2; //values can be switched to default goblin/knight variables if added
						break;
					}
				}
			}
		}
		if(goblinNear == false && this.spedUp == true) {
			this.spedUp = false;
			//need someway to access default 
		}
		
	}
}
