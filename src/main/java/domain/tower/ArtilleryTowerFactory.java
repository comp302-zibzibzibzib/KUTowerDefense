package domain.tower;

import domain.kutowerdefense.GameOptions;

public class ArtilleryTowerFactory implements TowerFactory {
    private static ArtilleryTowerFactory instance;

    private ArtilleryTowerFactory() {}

    public static ArtilleryTowerFactory getInstance() {
        if (instance == null) {
            instance = new ArtilleryTowerFactory();
        }
        return instance;
    }

    @Override
    public Tower createTower() {
        GameOptions options = GameOptions.getInstance();
        return new ArtilleryTower(options.getArtilleryCost(), options.getArtilleryUpgradeCost(), 1, options.getArtilleryRange(), options.getArtilleryFireRate());
    }
}
