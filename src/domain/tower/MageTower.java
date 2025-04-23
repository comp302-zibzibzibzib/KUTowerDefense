package domain.tower;

import domain.map.Location;

public class MageTower extends Tower {

	public MageTower(int upgradeCost, int level, double range, double fireRate, AttackType attackType) {
		super();
		this.upgradeCost = upgradeCost;
		this.level = level;
		this.attackType = AttackType.SPELL;
		this.target = null;
		this.range = range;
	}

	@Override
	public Projectile createProjectile() {
		Projectile projectile = new Projectile(attackType, target, location);
		System.out.printf("%f",projectile.damage);
		return projectile;
	}

}
