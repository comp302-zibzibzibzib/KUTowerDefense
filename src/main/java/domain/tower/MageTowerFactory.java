package domain.tower;

public class MageTowerFactory implements TowerFactory {
    private static MageTowerFactory instance;

    private MageTowerFactory() {}

    public static MageTowerFactory getInstance() {
        if (instance == null) {
            instance = new MageTowerFactory();
        }
        return instance;
    }

    @Override
    public Tower createTower() {
        return new MageTower(costMage, upgradeCostMage, 1, rangeMage, fireRateMage);
    }
}

