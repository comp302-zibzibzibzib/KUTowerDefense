package domain.tower;

public class ArcherTower extends Tower {
	private static final long serialVersionUID = 1L;

	public ArcherTower(int cost, int upgradeCost, int level, double range, double fireRate) {
		super(cost, upgradeCost, AttackType.ARROW, level, range, fireRate);
	}

	@Override
	public void upgradeTower() {
		if (level == 2) return;
		range *= 1.5;
		fireRate *= 2;
	}
}
