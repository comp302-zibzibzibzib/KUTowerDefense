package domain.entities;

import domain.kutowerdefense.GameOptions;

public class GoblinFactory implements EnemyFactory {
    private static GoblinFactory instance;
    private GoblinFactory() {}

    public static GoblinFactory getInstance() {
        if (instance == null) {
            instance = new GoblinFactory();
        }
        return instance;
    }

    @Override
    public Enemy createEnemy() {
        return new Goblin(GOBLIN_HIT_POINTS, 6, null, GOBLIN_SPELL_REDUCTION);
    }
}
