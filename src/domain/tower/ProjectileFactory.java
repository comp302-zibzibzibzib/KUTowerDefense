package domain.tower;

import domain.entities.Enemy;
import domain.map.Location;

public class ProjectileFactory {
	private static final double SPLASH_RADIUS = 8;
	
	public static Projectile createArrow(Enemy target, Location location) {
		if (target == null) return null;
		Location projLocation = new Location(location.xCoord, location.yCoord);
		
		return new Projectile(AttackType.ARROW, target, projLocation);
	}
	
	public static Projectile createSpell(Enemy target, Location location) {
		if (target == null) return null;
		Location projLocation = new Location(location.xCoord, location.yCoord);
		
		return new Projectile(AttackType.SPELL, target, projLocation);
	}
	
	public static Projectile createArtilleryShell(Enemy target, Location location) {
		if (target == null) return null;
		Location projLocation = new Location(location.xCoord, location.yCoord);

		return new AOEProjectile(target, projLocation, SPLASH_RADIUS);
	}
}
