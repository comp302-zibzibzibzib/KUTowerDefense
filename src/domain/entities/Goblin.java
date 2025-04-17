package domain.entities;

import domain.map.Location;

public class Goblin extends Enemy {
	
	private float spellDamageReduction;
	
	public Goblin(float hitPoints, float speed, Location location, float spellDamageReduction) { //var can be set/changed thus needs a parameter
		this.hitPoints = hitPoints;
		this.speed = speed;
		this.location = location;
		this.spellDamageReduction = spellDamageReduction;
	}

	public float getSpellDamageReduction() {
		return spellDamageReduction;
	}

	public void setSpellDamageReduction(float spellDamageReduction) {
		this.spellDamageReduction = spellDamageReduction;
	}
	
	
	
	
}
