package domain.tower;

import java.io.Serializable;
import java.util.ArrayList;

import domain.entities.Effect;
import domain.entities.Enemy;
import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;
import domain.map.Tile;
import domain.services.Utilities;

public class Projectile implements Serializable{
	private static final long serialVersionUID = 1L;
	private static int numOfProj = 0;

	private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private int id;

	protected double damage;
	protected double angle;
	protected AttackType attackType;
	protected Enemy target;
	protected Location location;
	
	
	public Projectile(AttackType attackType, double damage, Enemy target, Location location) {
		this.damage = damage;
		this.attackType = attackType;
		this.target = target;
		this.location = new Location(location.xCoord, location.yCoord);
		this.id = getProjID();
		projectiles.add(this);
	}
	/**
     * @requires
     *   - deltaTime ≥ 0
     *   - If PlayModeManager.getInstance().getGameSpeed() ≠ 0, then:
     *       • target ≠ null
     *       • target.getLocation() ≠ null
     *       • this.location ≠ null
     *       • distance = Utilities.euclideanDistance(this.location, target.getLocation()) > 0
     *
     * @modifies
     *   - this.location (projectile location)
     *   - If the pre‐movement distance to target < 1, then:
     *       • target.hitPoints (via target.hitEnemy(...))
     *       • Projectile.projectiles (this instance will be removed)
     *
     * @effects
     *   - If PlayModeManager.getInstance().getGameSpeed() == 0 or target == null, returns immediately
     *     (no changes to any state).
     *   - Otherwise:
     *       1. Let speed = 30 * PlayModeManager.getInstance().getGameSpeed().
     *       2. Compute displacement = speed * deltaTime.
     *       3. Update this.location to:
     *            this.location.xCoord += xUnit * displacement
     *            this.location.yCoord += yUnit * displacement
     *       4. If (pre‐movement) distance < 1, then invoke hitTarget(), which:
     *            • calls target.hitEnemy(damage, attackType) (reducing target’s HP)
     *            • calls killProjectile(), removing this instance from Projectile.projectiles.
     */
	public void update(double deltaTime) {
		if (PlayModeManager.getInstance().getGameSpeed() == 0 || target == null) return;

		double projectileSpeed = 30;
		double displacement = projectileSpeed * PlayModeManager.getInstance().getGameSpeed() * deltaTime;

		double targetX = target.getLocation().getXCoord();
		double targetY = target.getLocation().getYCoord();

		double distance = Utilities.euclideanDistance(location, target.getLocation());

		double deltaX = targetX - location.getXCoord();
		double deltaY = targetY - location.getYCoord();

		double xUnit = deltaX / distance;
		double yUnit = deltaY / distance;

		double angleRadians = Math.atan2(yUnit, xUnit);
		angle = Math.toDegrees(angleRadians);

		location.xCoord += xUnit * displacement;
		location.yCoord += yUnit * displacement;

		if (distance < 1) {
			hitTarget();
		}
	}

	public void hitTarget() {
		target.hitEnemy(damage, attackType);
		if (attackType != AttackType.ARROW) {
			boolean effectNear = false;
			for (Effect effect : Effect.getEffects().values()) {
				if (effect.getAttackType() != attackType) continue;
				if (Utilities.euclideanDistance(effect.getLocation(), target.getLocation()) < Tile.tileLength * 0.5) {
					effectNear = true;
					break;
				}
			}

			if (!effectNear) {
				new Effect(target.getLocation(), attackType);
			}
		}
		killProjectile();
	}

	public boolean followTarget() {
		//Boolean return: If true is returned, remove projectile else do nothing.
	    if (target == null || target.getHitPoints() <= 0) return false; //No target in range or enemy is dead, does nothing.
		if (!target.isInitialized()) {
			killProjectile();
			return false;
		}
	    
	    //Gets the difference from target to projectile and calculates the euclidian distance between them 
	    double dx = target.getLocation().xCoord - location.xCoord; 
	    double dy = target.getLocation().yCoord - location.yCoord;
	    double distance = Utilities.euclideanDistance(location, target.getLocation());
	    
	    //Hits the enemy if the distance is below a certain threshold
	    if (distance < 3) { //Arbitrary threshold value
	        hitTarget();
	        //For removal
	        return true;
	    }
	    	
	    //Calculates unit direction vectors and multiplies them with projectile speed. 
	    double speed = 1; //Arbitrary speed value (Changing values may result unwanted behaviours needs fixing)
	    double nextX = (dx / distance) * speed;
	    double nextY = (dy / distance) * speed;
	    //Updates the projectile's coodrinates in the direction of enemy
	    location.xCoord += nextX;
	    location.yCoord += nextY;
	    //Projectile is still active, does nothing
	    return false;
	}

	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Enemy getTarget() {
		return target;
	}
	public AttackType getAttackType() {
		return attackType;
	}

	public static ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	private void killProjectile() {
		projectiles.remove(this);
	}

	public static void killProjectiles(Enemy target) {
		ArrayList<Projectile> projectileList = new ArrayList<>(projectiles);
		for (Projectile projectile : projectileList) {
			if (projectile.target == target) projectile.killProjectile();
		}
	}

	private int getProjID() {
		return numOfProj++;
	}

	public int getID() {
		return id;
	}

	public static void resetProjectiles() {
		projectiles = new ArrayList<Projectile>();
	}

	public double getAngle() {
		return angle;
	}
}
