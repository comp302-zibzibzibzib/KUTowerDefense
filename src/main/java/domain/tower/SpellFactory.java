package domain.tower;

import domain.entities.Enemy;
import domain.kutowerdefense.GameOptions;
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
        GameOptions options = GameOptions.getInstance();
        return new Projectile(AttackType.SPELL, options.getSpellDamage(), target, sourceLocation);
    }
}
