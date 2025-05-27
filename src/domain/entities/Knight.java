package domain.entities;

import domain.map.Location;
import domain.map.Tile;
import domain.services.Utilities;
import domain.tower.AttackType;

public class Knight extends Enemy {
	private static final double SYNERGY_CHECK_INTERVAL = 1;

	private double defaultSpeed;
	private double arrowDamageReduction;
    private boolean spedUp;

	private double timeSinceLastCheck;
  
	public Knight(double hitPoints, double speed, Location location, double arrowDamageReduction) {
		super(hitPoints, speed, location);
		defaultSpeed = speed;
		this.arrowDamageReduction = arrowDamageReduction;
        spedUp = false;
		timeSinceLastCheck = 0;
	}

	//if distance between a knight and goblin less than width of tile speed up knight
	//might need a place to store default knight speed
	public void knightSpeedUp() {
		boolean goblinNear = false;
		for(Enemy enemy : Enemy.getActiveEnemies()) {
			if(!(enemy instanceof Goblin)) continue;
			double distance = Utilities.euclideanDistance(this.location, enemy.getLocation());
			if(distance < Tile.tileLength) {
				spedUp = true;
				speed = (defaultSpeed + enemy.speed) / 2; //values can be switched to default goblin/knight variables if added
				return;
			}
		}
		spedUp = false;
		speed = defaultSpeed;
	}

	@Override
	public void moveEnemy(long deltaTime) {
		super.moveEnemy(deltaTime);
		knightSpeedUp();
	}

	@Override
	public void hitEnemy(double damage, AttackType attackType) {
		double reducedDamage = damage;
		if (attackType == AttackType.ARROW) reducedDamage = (1 - arrowDamageReduction) * reducedDamage;
		super.hitEnemy(reducedDamage, attackType);
	}

	public boolean isFast() {
		return spedUp;
	}
}
