package domain.tower;

import domain.entities.Enemy;
import domain.map.Location;

public class AOEProjectile extends Projectile {
	private double splashRadius;
	

	public AOEProjectile(AttackType attacktype, Enemy enemy, Location location, double splashRadius) {
		super(attacktype, enemy, location);
		// TODO Auto-generated constructor stub
		this.splashRadius = splashRadius;
	}
	public void hitArea() {
			
	}

}
