package domain.tower;

import domain.entities.Enemy;
import domain.kutowerdefense.GameOptions;
import domain.map.Location;

public class BetterArtilleryFactory implements ProjectileFactory {
    private static final double SPLASH_RADIUS = 8.0;
    private static BetterArtilleryFactory instance;

    private BetterArtilleryFactory() {}

    public static BetterArtilleryFactory getInstance() {
        if (instance == null) {
            instance = new BetterArtilleryFactory();
        }
        return instance;
    }

    @Override
    public Projectile createProjectile(Enemy target, Location sourceLocation) {
        GameOptions options = GameOptions.getInstance();
        return new AOEProjectile(options.getArtilleryDamage() * 1.2, target, sourceLocation, options.getAoeRange());
    }
}
