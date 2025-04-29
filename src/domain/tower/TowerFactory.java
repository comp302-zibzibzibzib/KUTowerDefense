package domain.tower;

public class TowerFactory {
    private static final int costArcher = 50;
    private static final int upgradeCostArcher = 50;
    private static final double rangeArcher = 7.5;
    private static final double fireRateArcher = 6;
    private static final int costArtillery = 50;
    private static final int upgradeCostArtillery = 50;
    private static final double rangeArtillery = 7.5;
    private static final double fireRateArtillery = 2;
    private static final int costMage = 50;
    private static final int upgradeCostMage = 50;
    private static final double rangeMage = 7.5;
    private static final double fireRateMage = 4;

    public static ArcherTower createArcherTower() {
        return new ArcherTower(costArcher, upgradeCostArcher, 1, rangeArcher, fireRateArcher);
    }

    public static ArtilleryTower createArtilleryTower() {
        return new ArtilleryTower(costArtillery, upgradeCostArtillery, 1, rangeArtillery, fireRateArtillery);
    }

    public static MageTower createMageTower() {
        return new MageTower(costMage, upgradeCostMage, 1, rangeMage, fireRateMage);
    }
}