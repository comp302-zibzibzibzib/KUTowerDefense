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
        GameOptions options = GameOptions.getInstance();
        return new Goblin(options.getGoblinHealth(), options.getGoblinSpeed(), null, GOBLIN_SPELL_REDUCTION);
    }
}
