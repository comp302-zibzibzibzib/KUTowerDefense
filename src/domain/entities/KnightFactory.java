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
        return new Knight(KNIGHT_HIT_POINTS, 3, null, KNIGHT_ARROW_REDUCTION);
    }
}
