package domain.controller;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.tower.Projectile;
import domain.tower.Tower;
import domain.tower.TowerFactory;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

class TowerLoop extends AnimationTimer {
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
	private static final TowerLoop towerLoop = new TowerLoop();

	public static boolean canBuildArcher() {
		return Player.getInstance().getGold() >= GameOptions.getInstance().getArcherCost();
	}
	
	public static boolean canBuildMage() {
		return Player.getInstance().getGold() >= GameOptions.getInstance().getMageCost();
	}
	
	public static boolean canBuildArtillery() {
		return Player.getInstance().getGold() >= GameOptions.getInstance().getArtilleryCost();
	}

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
		towerLoop.start();
		Projectile.resetProjectiles();
		PlayModeManager.getInstance().getCurrentMap().resetTowers();
	}

	public static boolean projectileDead(int id) {
		for (Projectile proj : Projectile.getProjectiles()) {
			if (proj.getID() == id) return false;
		}
		return true;
	}

	public static void stop() {
		towerLoop.stop();
		Projectile.resetProjectiles();
		PlayModeManager.getInstance().getCurrentMap().resetTowers();
	}

	private static Projectile getProjectile(int id) {
		for (Projectile proj : Projectile.getProjectiles()) {
			if (proj.getID() == id) return proj;
		}
		return null;
	}
}
