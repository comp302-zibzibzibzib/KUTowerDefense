package domain.tower;

public enum AttackType {
	ARROW(5),
    SPELL(10),
    ARTILLERY(20);

	private final double damage;

	private AttackType(double damage) {
		this.damage = damage;
	}
	public double getDamage() {
		return damage;
	}

}
