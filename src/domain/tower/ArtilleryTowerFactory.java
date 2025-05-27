package domain.tower;

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
        return new ArtilleryTower(costArtillery, upgradeCostArtillery, 1, rangeArtillery, fireRateArtillery);
    }
}
