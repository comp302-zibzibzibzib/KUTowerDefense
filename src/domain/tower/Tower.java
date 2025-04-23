package domain.tower;

import java.util.List;

import domain.entities.Enemy;
import domain.map.Location;
import domain.map.PathTile;
import domain.map.Tile;
import domain.services.Utilities;

public abstract class Tower {
	protected int upgradeCost;
    protected int level;
    protected double range;
    protected double fireRate;
    protected AttackType attackType;
    protected Enemy target;
    protected Location location;

    public abstract Projectile createProjectile();

    public void targetEnemy() {
    	int currentpathIndex;
    	int nextpathIndex;
    	double distanceToNext;
    	double progressInTile;
    	double totalProgress = 0.0;
    	double bestProgress = Double.MIN_VALUE;
    	Enemy lastTarget = null;
    	
        for (Enemy e : Enemy.getAllEnemies()) {
        	if(Utilities.euclideanDistance(location, e.getLocation()) <= range) {
        		currentpathIndex = e.getPathIndex();
            	nextpathIndex = currentpathIndex +1;
            	
            	distanceToNext = Utilities.manhattanDistance(e.getLocation(), Enemy.path.get(nextpathIndex).getLocation());
            	progressInTile = Tile.tileLength - distanceToNext;
            	totalProgress = currentpathIndex * Tile.tileLength + progressInTile;
            	
            	if(totalProgress > bestProgress) {
            		bestProgress = totalProgress;
            		lastTarget = e;
            	}
            	
        	}
        	
        }
        target = lastTarget;
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
    
    
}

