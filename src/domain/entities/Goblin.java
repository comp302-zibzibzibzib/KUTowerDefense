package domain.entities;

import domain.map.Location;

public class Goblin extends Enemy {
	
	private double spellDamageReduction;
	
	public Goblin(double hitPoints, double speed, Location location, double spellDamageReduction) { //var can be set/changed thus needs a parameter
		this.hitPoints = hitPoints;
		this.speed = speed;
		this.location = location;
		this.spellDamageReduction = spellDamageReduction;
	}

	public double getSpellDamageReduction() {
		return spellDamageReduction;
	}

	public void setSpellDamageReduction(double spellDamageReduction) {
		this.spellDamageReduction = spellDamageReduction;
	}
	
	
	
	
}
