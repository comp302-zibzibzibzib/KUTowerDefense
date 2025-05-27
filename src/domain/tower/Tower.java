package domain.tower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import domain.entities.Enemy;
import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;
import domain.map.PathTile;
import domain.map.Tile;
import domain.services.Utilities;

public abstract class Tower implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected int cost;
	protected int upgradeCost;
    protected int level;
    protected double range;
    protected double fireRate;
    protected AttackType attackType;
    protected Enemy target;
    protected Location location;

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
		PlayModeManager.getInstance().getCurrentMap().addTower(this);
	}

    public Projectile createProjectile() {
		return switch (attackType) {
			case AttackType.ARROW -> ArrowFactory.getInstance().createProjectile(target, location);
			case AttackType.SPELL -> SpellFactory.getInstance().createProjectile(target, location);
			case AttackType.ARTILLERY -> ArtilleryFactory.getInstance().createProjectile(target, location);
		};
	}

    public void targetEnemy() {
    	int currentpathIndex;
    	int nextpathIndex;
    	double distanceToNext;
    	double progressInTile;
    	double totalProgress = 0.0;
    	double bestProgress = Double.MIN_VALUE;
    	Enemy lastTarget = null;

		if (target != null && Utilities.euclideanDistance(location, target.getLocation()) > range) {
			double a = Utilities.euclideanDistance(location, target.getLocation());
			target = null;
		}

		List<Enemy> enemyList = new ArrayList<>(Enemy.getActiveEnemies());
    	for (Enemy e : enemyList) {
    	    if (Utilities.euclideanDistance(location, e.getLocation()) <= range) {
    	        currentpathIndex = e.getPathIndex();
    	        nextpathIndex = currentpathIndex + 1;

    	        if (nextpathIndex < 0 || nextpathIndex >= Enemy.path.size()) {
    	            continue; 
    	        }

    	        distanceToNext = Utilities.manhattanDistance(
    	            e.getLocation(), 
    	            Enemy.path.get(nextpathIndex).getLocation()
    	        );
    	        progressInTile = Tile.tileLength - distanceToNext;
    	        totalProgress = currentpathIndex * Tile.tileLength + progressInTile;

    	        if (totalProgress > bestProgress) {
    	            bestProgress = totalProgress;
    	            lastTarget = e;
   	        	}
   	    	}
   	    }
		target = lastTarget;
    }
    
    public Projectile update(double dt) {
		targetEnemy();
        timeSinceLastShot += dt; //Tracks how much time has passed since last creation
		//Projectile creation period
        //If not enough time has passed does not create another projectile
        if (timeSinceLastShot < firePeriod || target == null) {
            return null;
        }
        //Allocates target if is in range
        //Resets the time (enough time has passed) 
        timeSinceLastShot = 0;
        //Creates projectile based on attack type of the tower
		//IMPORTANT: Location of the projectile is the same
		//as the location of the tower at creation, add offset if convenient
        return createProjectile();
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
}

