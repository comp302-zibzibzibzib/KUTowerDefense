package domain.entities;

import domain.map.Location;

public class Knight extends Enemy {
	
	double arrowDamageReduction;
	
	public Knight(double hitPoints, double speed, Location location, double arrowDamageReduction) {//var can be changed
		this.hitPoints = hitPoints;
		this.speed = speed;
		this.location = location;
		this.arrowDamageReduction = arrowDamageReduction;
		
	}

}
