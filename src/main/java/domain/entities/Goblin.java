package domain.entities;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.tower.AttackType;

public class Goblin extends Enemy {
	
	private double spellDamageReduction;
	
	public Goblin(double hitPoints, double speed, Location location, double spellDamageReduction) {
		super(hitPoints, speed, location);
		this.spellDamageReduction = spellDamageReduction;
	}

	public double getSpellDamageReduction() {
		return spellDamageReduction;
	}

	public void setSpellDamageReduction(double spellDamageReduction) {
		this.spellDamageReduction = spellDamageReduction;
	}

	@Override
	public void hitEnemy(double damage, AttackType attackType) {
		double reducedDamage = damage;
		if (attackType == AttackType.SPELL || attackType == AttackType.SLOW_SPELL) reducedDamage = (1 - spellDamageReduction) * reducedDamage;
		super.hitEnemy(reducedDamage, attackType);
	}

	@Override
	public void killEnemy() {
		Player.getInstance().updateGold(GameOptions.getInstance().getGoblinReward());
		super.killEnemy();
	}
}
