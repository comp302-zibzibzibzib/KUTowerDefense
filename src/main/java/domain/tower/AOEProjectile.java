package domain.tower;

import java.util.ArrayList;

import domain.entities.Enemy;
import domain.map.Location;
import domain.services.Utilities;

public class AOEProjectile extends Projectile {
	private double splashRadius;

	public AOEProjectile(double damage, Enemy target, Location location, double splashRadius) {
		super(AttackType.ARTILLERY, damage, target, location);
		// TODO Auto-generated constructor stub
		this.splashRadius = splashRadius;
	}

	private void hitArea() {
		ArrayList<Enemy> enemies = new ArrayList<>(Enemy.getActiveEnemies());
		for(Enemy e: enemies) {
			if (e == target) continue;
			if(Utilities.euclideanDistance(e.getLocation(), location) <= splashRadius) {
				e.hitEnemy(damage, AttackType.ARTILLERY);
			}
		}
	}

	@Override
	public void hitTarget() {
		super.hitTarget();
		hitArea();
	}
}
