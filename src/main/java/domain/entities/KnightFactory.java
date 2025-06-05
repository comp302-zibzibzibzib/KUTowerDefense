package domain.entities;

import domain.kutowerdefense.GameOptions;

public class KnightFactory implements EnemyFactory {
    private static KnightFactory instance;
    private KnightFactory() {}

    public static KnightFactory getInstance() {
        if (instance == null) {
            instance = new KnightFactory();
        }
        return instance;
    }

    @Override
    public Enemy createEnemy() {
        GameOptions options = GameOptions.getInstance();
        return new Knight(options.getKnightHealth(), options.getKnightSpeed(), null, KNIGHT_ARROW_REDUCTION);
    }
}
