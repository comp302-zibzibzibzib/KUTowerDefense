package domain.controller;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Lot;
import domain.map.Map;
import domain.map.Tile;
import domain.tower.Projectile;
import domain.tower.Tower;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

class TowerLoop extends AnimationTimer {
	// Animation timer that handles projectile and tower targeting logic in real time
	// Gets injected into the JavaFX application loop and gets executed every frame (handle method)
	long lastUpdate = 0;

	@Override
	public void handle(long now) {
		if(PlayModeManager.getInstance().getGameSpeed() == 0) {
			lastUpdate = now;
			return;
		}

		if (lastUpdate == 0) {
			lastUpdate = now;
			return;
		}

		double deltaTime = (double) (now - lastUpdate) / 1_000_000_000;
		lastUpdate = now;

		List<Tower> towerList = PlayModeManager.getInstance().getCurrentMap().getTowerList();
		for (int i = 0; i < towerList.size(); i++) {
			Tower tower = towerList.get(i);
			tower.update(deltaTime);
		}


		List<Projectile> projectiles = new ArrayList<>(Projectile.getProjectiles());
		//System.out.println(projectiles.size());
		for (Projectile projectile : projectiles) {
			projectile.update(deltaTime);
		}
	}
}

public class TowerController {
	// Controller responsible for bridging ui and projectile logic as well as tower behaviour
	private static final TowerLoop towerLoop = new TowerLoop();

	// Check if the player can build a type of tower
	public static boolean canBuildArcher() {
		return Player.getInstance().getGold() >= GameOptions.getInstance().getArcherCost();
	}
	
	public static boolean canBuildMage() {
		return Player.getInstance().getGold() >= GameOptions.getInstance().getMageCost();
	}
	
	public static boolean canBuildArtillery() {
		return Player.getInstance().getGold() >= GameOptions.getInstance().getArtilleryCost();
	}
	public static boolean canUpgrade(int x, int y) {
		Tile tile = PlayModeManager.getInstance().getCurrentMap().tileMap[y][x];
		Tower tower = ((Lot) tile).getTower();
		return Player.getInstance().getGold() >= tower.getUpgradeCost() && tower.getLevel() == 1;
	}

	// Projectile information retrieval methods
	public static int getNumberOfProjectiles() {
		return Projectile.getProjectiles().size();
	}

	public static int getProjectileID(int i) {
		return Projectile.getProjectiles().get(i).getID();
	}

	public static double getProjectileX(int i) {
		return Projectile.getProjectiles().get(i).getLocation().getXCoord();
	}

	public static double getProjectileY(int i) {
		return Projectile.getProjectiles().get(i).getLocation().getYCoord();
	}

	public static double getProjectileAngle(int i) {
		return Projectile.getProjectiles().get(i).getAngle();
	}

	public static void startTowerLogic() {
		// Start tower targeting logic and projectile motion at the start of a play session
		towerLoop.start();
		Projectile.resetProjectiles();
		PlayModeManager.getInstance().getCurrentMap().resetTowers();
	}

	public static boolean projectileDead(int id) {
		// Check if a projectile has died
		for (Projectile proj : Projectile.getProjectiles()) {
			if (proj.getID() == id) return false;
		}
		return true;
	}

	public static String getProjectileName(int id) {
		// Get projectile name (its type) with the ID associated with it
		Projectile proj = getProjectile(id);
		return switch (proj.getAttackType()) {
			case ARROW -> "arrow";
			case ARTILLERY -> "bomb";
			case SPELL -> "red_ball";
			case SLOW_SPELL -> "blue_ball";
		};
	}

	public static void stop() {
		towerLoop.stop();
		Projectile.resetProjectiles();
		PlayModeManager.getInstance().getCurrentMap().resetTowers();
	}

	public static void setTowerAttributes() {
		// Load tower attributes from current tower game option variables
		Map currentMap = PlayModeManager.getInstance().getCurrentMap();
		for (Tower tower : currentMap.getTowerList()) {
			double range = switch (tower.getAttackType()) {
				case ARROW -> GameOptions.getInstance().getArcherRange();
				case ARTILLERY -> GameOptions.getInstance().getArtilleryRange();
				case SPELL, SLOW_SPELL -> GameOptions.getInstance().getMageRange();
            };

			double fireRate = switch (tower.getAttackType()) {
				case ARROW -> GameOptions.getInstance().getArcherFireRate();
				case ARTILLERY -> GameOptions.getInstance().getArtilleryFireRate();
				case SPELL, SLOW_SPELL -> GameOptions.getInstance().getMageFireRate();
            };

			int cost = switch (tower.getAttackType()) {
				case ARROW -> GameOptions.getInstance().getArcherCost();
				case ARTILLERY -> GameOptions.getInstance().getArtilleryCost();
				case SPELL, SLOW_SPELL -> GameOptions.getInstance().getMageCost();
			};

			int upgradeCost = switch (tower.getAttackType()) {
				case ARROW -> GameOptions.getInstance().getArcherUpgradeCost();
				case ARTILLERY -> GameOptions.getInstance().getArtilleryUpgradeCost();
				case SPELL, SLOW_SPELL -> GameOptions.getInstance().getMageUpgradeCost();
			};

			tower.setRange(range);
			tower.setFireRate(fireRate);
			tower.setCost(cost);
			tower.setUpgradeCost(upgradeCost);
			tower.setLevel(1);
		}
	}

	private static Projectile getProjectile(int id) {
		for (Projectile proj : Projectile.getProjectiles()) {
			if (proj.getID() == id) return proj;
		}
		return null;
	}
}
