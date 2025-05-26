package domain.entities;

import domain.map.Location;

public class Knight extends Enemy {
	
	double arrowDamageReduction;

	public Knight(double hitPoints, double speed, Location location, double arrowDamageReduction) {
		super(hitPoints, speed, location);
		this.arrowDamageReduction = arrowDamageReduction;
	}
}
