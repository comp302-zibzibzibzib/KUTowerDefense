package domain.tower;

public interface TowerFactory {
	static final int costArcher = 50;
	static final int upgradeCostArcher = 50;
	static final double rangeArcher = 20;
	static final double fireRateArcher = 5;
	static final int costArtillery = 50;
	static final int upgradeCostArtillery = 50;
	static final double rangeArtillery = 10;
	static final double fireRateArtillery = 1;
	static final int costMage = 50;
	static final int upgradeCostMage = 50;
	static final double rangeMage = 12;
	static final double fireRateMage = 2;
	
	Tower createTower();
}

