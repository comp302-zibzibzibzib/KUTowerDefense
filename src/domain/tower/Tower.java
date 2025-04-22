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
    	double bestProgress = 0.0;
    	Enemy lastTarget = null;
    	List<PathTile> path;
    	
        for (Enemy e : Enemy.getAllEnemies()) {
        	if(Utilities.manhattanDistance(location, e.getLocation()) <= range) {
        		currentpathIndex = e.getPathIndex();
            	nextpathIndex = currentpathIndex +1;
            	
            	distanceToNext = Utilities.manhattanDistance(e.getLocation(), Enemy.path.get(nextpathIndex).getLocation());
            	progressInTile = Tile.tileLength - distanceToNext;
            	progressInTile = Math.max(0, Math.min(progressInTile, Tile.tileLength));
            	totalProgress = currentpathIndex * Tile.tileLength + progressInTile;
            	
            	if(totalProgress > bestProgress) {
            		bestProgress = totalProgress;
            		lastTarget = target;
            	}
            	
        	}
        	
        }
        target = lastTarget;
    }
}

