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

    public void targetEnemy(List<Enemy> enemies) {
        for (Enemy e : enemies) {
            if (Utilities.euclideanDistance(this.location, e.getLocation()) <= this.range) {
                this.target = e;
                break;
            }
        }
    }
}

