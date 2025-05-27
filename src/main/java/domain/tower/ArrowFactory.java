package domain.tower;

import domain.entities.Enemy;
import domain.map.Location;

public class ArrowFactory implements ProjectileFactory {
    private static ArrowFactory instance;
    private ArrowFactory() {}

    public static ArrowFactory getInstance() {
        if (instance == null) {
            instance = new ArrowFactory();
        }
        return instance;
    }

    @Override
    public Projectile createProjectile(Enemy target, Location sourceLocation) {
        return new Projectile(AttackType.ARROW, target, sourceLocation);
    }
}
