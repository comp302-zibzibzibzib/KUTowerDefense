package domain.tower;

import domain.kutowerdefense.GameOptions;

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
        GameOptions options = GameOptions.getInstance();
        return new MageTower(options.getMageCost(), options.getMageUpgradeCost(), 1, options.getMageRange(), options.getMageFireRate());
    }
}

