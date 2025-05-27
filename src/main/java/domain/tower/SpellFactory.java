package domain.tower;

import domain.entities.Enemy;
import domain.map.Location;

public class SpellFactory implements ProjectileFactory {
    private static SpellFactory instance;
    private SpellFactory() {}

    public static SpellFactory getInstance() {
        if (instance == null) {
            instance = new SpellFactory();
        }
        return instance;
    }

    @Override
    public Projectile createProjectile(Enemy target, Location sourceLocation) {
        return new Projectile(AttackType.SPELL, target, sourceLocation);
    }
}
