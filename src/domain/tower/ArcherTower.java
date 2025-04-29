package domain.tower;

public class ArcherTower extends Tower {

	public ArcherTower(int cost,int upgradeCost, int level, double range, double fireRate) {
		super();
		this.upgradeCost = upgradeCost;
		this.attackType = AttackType.ARROW;
		this.level = level;
		this.target = null;
		this.range = range;
		this.cost = cost;
	}

	@Override
	public Projectile createProjectile() {
		Projectile projectile = ProjectileFactory.createArrow(target, location);
		System.out.printf("%f",projectile.damage);
		return projectile;
	}

}
