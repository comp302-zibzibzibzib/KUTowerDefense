package domain.tower;

import domain.entities.Enemy;
import domain.map.Location;

public class Projectile {
	protected double damage;
	protected AttackType attacktype;
	protected Enemy target;
	protected Location location;
	
	
	public Projectile(AttackType attacktype, Enemy target, Location location) {
		super();
		this.damage = attacktype.getDamage();
		this.attacktype = attacktype;
		this.target = target;
		this.location = location;
	}

	public void hitTarget() {
		target.hitEnemy(damage);
	}
	
	public void followTarget() {
		
	}

}
