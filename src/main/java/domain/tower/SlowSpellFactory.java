package domain.tower;

import domain.entities.Enemy;
import domain.kutowerdefense.GameOptions;
import domain.map.Location;

public class SlowSpellFactory implements ProjectileFactory {
    private static SlowSpellFactory instance;
    private SlowSpellFactory() {}

    public static SlowSpellFactory getInstance() {
        if (instance == null) {
            instance = new SlowSpellFactory();
        }
        return instance;
    }

    @Override
    public Projectile createProjectile(Enemy target, Location sourceLocation) {
        GameOptions options = GameOptions.getInstance();
        return new Projectile(AttackType.SLOW_SPELL, options.getSpellDamage(), target, sourceLocation);
    }
}
