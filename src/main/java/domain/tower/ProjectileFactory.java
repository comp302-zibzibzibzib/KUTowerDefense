package domain.tower;

import domain.entities.Enemy;
import domain.map.Location;

public interface ProjectileFactory {
	
	Projectile createProjectile(Enemy target, Location sourceLocation);
}
