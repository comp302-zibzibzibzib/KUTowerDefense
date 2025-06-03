package domain.entities;

public class BossFactory implements EnemyFactory{

    private static BossFactory instance;
    private BossFactory() {}

    public static BossFactory getInstance() {
        if (instance == null) {
            instance = new BossFactory();
        }
        return instance;
    }

    @Override
    public Enemy createEnemy() {
        return new Boss(BOSS_HIT_POINTS, BOSS_SPEED, null, BOSS_SPELL_REDUCTION, BOSS_ARROW_REDUCTION, BOSS_ARTILLERY_INCREASE);
    }

}