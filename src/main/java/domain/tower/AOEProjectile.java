package domain.tower;

import java.util.ArrayList;

import domain.entities.Enemy;
import domain.map.Location;
import domain.services.Utilities;

public class AOEProjectile extends Projectile {
	private double splashRadius;
	

	public AOEProjectile(Enemy target, Location location, double splashRadius) {
		super(AttackType.ARTILLERY, target, location);
		// TODO Auto-generated constructor stub
		this.splashRadius = splashRadius;
	}
	public void hitArea() {
		ArrayList<Enemy> enemies = Enemy.getAllEnemies();
		for(Enemy e: enemies) {
			if(Utilities.euclideanDistance(e.getLocation(), location)<= splashRadius) {
				e.hitEnemy(damage);
			}
		}
	}

}
