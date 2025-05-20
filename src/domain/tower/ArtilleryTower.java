package domain.tower;

public class ArtilleryTower extends Tower {

	public ArtilleryTower(int cost, int upgradeCost, int level, double range, double fireRate) {
		super();
		this.cost = cost;
		this.upgradeCost = upgradeCost;
		this.attackType = AttackType.ARTILLERY;
		this.level = level;
		this.target = null;
		this.range = range;
		this.fireRate = fireRate;
	}

	@Override
	public Projectile createProjectile() {
		Projectile projectile = ProjectileFactory.createArtilleryShell(target, location);
		System.out.printf("%f",projectile.damage);
		return projectile;
	}
}
