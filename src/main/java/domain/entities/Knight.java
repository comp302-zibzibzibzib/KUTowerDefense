package domain.entities;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.Tile;
import domain.services.Utilities;
import domain.tower.AttackType;

public class Knight extends Enemy {
	private double arrowDamageReduction;
    private boolean spedUp;

	private double timeSinceLastCheck;
  
	public Knight(double hitPoints, double speed, Location location, double arrowDamageReduction) {
		super(hitPoints, speed, location);
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
				speedUp(); //values can be switched to default goblin/knight variables if added
				return;
			}
		}
		endSpeedUp();
	}

	@Override
	public void updateEnemy(double deltaTime) {
		super.updateEnemy(deltaTime);
		knightSpeedUp();
	}

	@Override
	public void hitEnemy(double damage, AttackType attackType) {
		double reducedDamage = damage;
		// knights take reduced damage from arrows
		if (attackType == AttackType.ARROW) reducedDamage = (1 - arrowDamageReduction) * reducedDamage;
		super.hitEnemy(reducedDamage, attackType);
	}

	@Override
	public void slowDown() {
		timeSinceSlowedDown = 0;
		slowedDown = true;
		determineSpeed();
	}

	@Override
	public void endSlowDown() {
		timeSinceSlowedDown = 0;
		slowedDown = false;
		determineSpeed();
	}

	@Override
	public void killEnemy() {
		Player.getInstance().updateGold(GameOptions.getInstance().getKnightReward());
		super.killEnemy();
	}

	private void speedUp() {
		if (isFast()) return;
		spedUp = true;
		determineSpeed();
	}

	private void endSpeedUp() {
		if (!isFast()) return;
		spedUp = false;
		determineSpeed();
	}

	private void determineSpeed() {
		if (spedUp) {
			speed = (defaultSpeed + GameOptions.getInstance().getGoblinSpeed()) / 2;
			if (slowedDown) speed *= 0.8;
		} else if (slowedDown) {
			speed = defaultSpeed * 0.8;
		} else {
			speed = defaultSpeed;
		}
	}

	public boolean isFast() {
		return spedUp;
	}
}
