package domain.tower;

public enum AttackType {
	ARROW(5),
    SPELL(8),
	SLOW_SPELL(8),
    ARTILLERY(15);

	private final double damage;

	private AttackType(double damage) {
		this.damage = damage;
	}
	public double getDamage() {
		return damage;
	}

}
