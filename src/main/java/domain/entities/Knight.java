package domain.entities;

import domain.map.Location;

public class Knight extends Enemy {
	
	double arrowDamageReduction;
	
	public Knight(double hitPoints, double speed, Location location, double arrowDamageReduction) {//var can be changed
		this.hitPoints = hitPoints;
		this.speed = speed;
		this.location = new Location();
		if (location != null) {
			this.location.xCoord = location.xCoord;
			this.location.yCoord = location.yCoord;
		}
		this.arrowDamageReduction = arrowDamageReduction;
		Enemy.enemies.add(this);
		this.enemyID = getID();
	}

}
