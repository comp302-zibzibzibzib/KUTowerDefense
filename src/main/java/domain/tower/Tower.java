package domain.tower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import domain.entities.Enemy;
import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;
import domain.map.Tile;
import domain.services.Utilities;

public abstract class Tower implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final double FREEZE_DURATION = 4.0;

	protected transient int cost;
	protected transient int upgradeCost;
    protected transient int level;
    protected transient double range;
    protected transient double fireRate;
    protected AttackType attackType;
    protected Enemy target;
    protected Location location;

	protected boolean isFrozen;
	protected double timeSinceFrozen;

	private double timeSinceLastShot;
	private double firePeriod;

	public Tower(int cost, int upgradeCost, AttackType attackType, int level, double range, double fireRate) {
		this.cost = cost;
		this.upgradeCost = upgradeCost;
		this.attackType = attackType;
		this.level = level;
		this.target = null;
		this.range = range;
		this.fireRate = fireRate;
		firePeriod = 1/fireRate;
		timeSinceLastShot = firePeriod;
	}

	public abstract void upgradeTower();

    public Projectile createProjectile() {
		return switch (attackType) {
			case AttackType.ARROW -> ArrowFactory.getInstance().createProjectile(target, location);
			case AttackType.SPELL -> SpellFactory.getInstance().createProjectile(target, location);
			case AttackType.SLOW_SPELL -> SlowSpellFactory.getInstance().createProjectile(target, location);
			case AttackType.ARTILLERY -> createArtillery(target, location);
		};
	}

	private Projectile createArtillery(Enemy target, Location location) {
		if (level == 2) {
			return BetterArtilleryFactory.getInstance().createProjectile(target, location);
		} else {
			return ArtilleryFactory.getInstance().createProjectile(target, location);
		}
	}

    public void targetEnemy() {
    	int currentpathIndex;
    	int nextpathIndex;
    	double distanceToNext;
    	double progressInTile;
    	double totalProgress = 0.0;
    	double bestProgress = -Double.MIN_VALUE;
    	Enemy lastTarget = null;

		if (target != null && Utilities.euclideanDistance(location, target.getLocation()) > range) {
			target = null;
		}

		List<Enemy> enemyList = new ArrayList<>(Enemy.getActiveEnemies());
    	for (Enemy e : enemyList) {
			double totalPathLength = e.path.size() * Tile.tileLength;
    	    if (Utilities.euclideanDistance(location, e.getLocation()) <= range) {
    	        currentpathIndex = e.getPathIndex();
    	        nextpathIndex = currentpathIndex + 1;

    	        if (nextpathIndex < 0 || nextpathIndex >= e.path.size()) {
    	            continue; 
    	        }

    	        distanceToNext = Utilities.manhattanDistance(
    	            e.getLocation(), 
    	            e.path.get(nextpathIndex).getLocation()
    	        );
    	        progressInTile = Tile.tileLength - distanceToNext;
    	        totalProgress = currentpathIndex * Tile.tileLength + progressInTile;

				double progressPercentage = totalProgress / totalPathLength;
    	        if (progressPercentage > bestProgress) {
    	            bestProgress = progressPercentage;
    	            lastTarget = e;
   	        	}
   	    	}
   	    }
		target = lastTarget;
    }

	public void update(double dt) {
		dt = dt * PlayModeManager.getInstance().getGameSpeed();
		targetEnemy();
		timeSinceLastShot += dt; //Tracks how much time has passed since last creation

		if(isFrozen) {
			this.timeSinceFrozen += dt;
			if(this.timeSinceFrozen < FREEZE_DURATION) {
				return;
			}
			else {
				isFrozen = false;
				timeSinceFrozen = 0.0;
			}
		}
		//Projectile creation period
		//If not enough time has passed does not create another projectile
		if (timeSinceLastShot < firePeriod || target == null) {
			return;
		}
		//Allocates target if is in range
		//Resets the time (enough time has passed)
		timeSinceLastShot = 0;
		//Creates projectile based on attack type of the tower
		//IMPORTANT: Location of the projectile is the same
		//as the location of the tower at creation, add offset if convenient
		createProjectile();
	}
    
	public int getUpgradeCost() {
		return upgradeCost;
	}

	public void setUpgradeCost(int upgradeCost) {
		this.upgradeCost = upgradeCost;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getFireRate() {
		return fireRate;
	}

	public void setFireRate(double fireRate) {
		this.fireRate = fireRate;
	}

	public AttackType getAttackType() {
		return attackType;
	}

	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
	}

	public Enemy getTarget() {
		return target;
	}

	public void setTarget(Enemy target) {
		this.target = target;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
    
    public int getCost() {
    	return cost;
    }

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setIsFrozen(boolean value) {
		isFrozen = value;
	}
}

