package domain.tower;

import domain.kutowerdefense.GameOptions;

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
        GameOptions options = GameOptions.getInstance();
        return new ArcherTower(options.getArcherCost(), options.getArcherUpgradeCost(), 1, options.getArcherRange(), options.getArcherFireRate());
    }
}
