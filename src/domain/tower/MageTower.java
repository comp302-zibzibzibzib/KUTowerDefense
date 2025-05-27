package domain.tower;

public class MageTower extends Tower {
	private static final long serialVersionUID = 1L;

	public MageTower(int cost, int upgradeCost, int level, double range, double fireRate) {
		super(cost, upgradeCost, AttackType.SPELL, level, range, fireRate);
	}
}
