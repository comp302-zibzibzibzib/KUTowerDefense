package domain.tower;

import java.util.List;

import domain.entities.Enemy;
import domain.map.Location;
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
    	
        for (Enemy e : Enemy.getAllEnemies()) {
        	currentpathIndex = e.getPathIndex();
        	nextpathIndex = currentpathIndex +1;
            if () {
               
            }
        }
    }
}

