package domain.tower;

import domain.entities.Enemy;
import domain.map.Location;

public class ArtilleryFactory implements ProjectileFactory {
    private static final double SPLASH_RADIUS = 8.0;
    private static ArtilleryFactory instance;

    private ArtilleryFactory() {}

    public static ArtilleryFactory getInstance() {
        if (instance == null) {
            instance = new ArtilleryFactory();
        }
        return instance;
    }

    @Override
    public Projectile createProjectile(Enemy target, Location sourceLocation) {
        return new AOEProjectile(target, sourceLocation, SPLASH_RADIUS);
    }
}
