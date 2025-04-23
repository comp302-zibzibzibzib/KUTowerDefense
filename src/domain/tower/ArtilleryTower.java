package domain.tower;

import domain.map.Location;

public class ArtilleryTower extends Tower {

	public ArtilleryTower(int upgradeCost, int level, double range, double fireRate, Location location) {
		super();
		this.upgradeCost = upgradeCost;
		this.attackType = AttackType.ARTILLERY;
		this.level = level;
		this.target = null;
		this.location = location;
	}

	@Override
	public Projectile createProjectile() {
		Projectile projectile = new Projectile(attackType, target, location);
		System.out.printf("%f",projectile.damage);
		return projectile;
	}
}
