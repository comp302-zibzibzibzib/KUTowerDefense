package domain.entities;

import domain.map.Location;

public class Knight extends Enemy {
	
	float arrowDamageReduction;
	
	public Knight(float hitPoints, float speed, Location location, float arrowDamageReduction) {//var can be changed
		this.hitPoints = hitPoints;
		this.speed = speed;
		this.location = location;
		this.arrowDamageReduction = arrowDamageReduction;
	}

}
