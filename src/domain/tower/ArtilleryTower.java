package domain.tower;

public class ArtilleryTower extends Tower {
	private static final long serialVersionUID = 1L;

	public ArtilleryTower(int cost, int upgradeCost, int level, double range, double fireRate) {
		super(cost, upgradeCost, AttackType.ARTILLERY, level, range, fireRate);
	}

	@Override
	public void upgradeTower() {
		if (level == 2) return;
		level = 2;
		range *= 1.2;
	}
}
