package domain.tower;

import domain.entities.Enemy;

public class ArcherTowerFactory implements TowerFactory {
    private static ArcherTowerFactory instance;

    private ArcherTowerFactory() {}

    public static ArcherTowerFactory getInstance() {
        if (instance == null) {
            instance = new ArcherTowerFactory();
        }
        return instance;
    }

    @Override
    public Tower createTower() {
        return new ArcherTower(costArcher, upgradeCostArcher, 1, rangeArcher, fireRateArcher);
    }
}
