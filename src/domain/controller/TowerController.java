package domain.controller;

import domain.kutowerdefense.Player;
import domain.tower.TowerFactory;

public class TowerController {
	
	public static boolean canBuildArcher() {
		return Player.getInstance().getGold() >= TowerFactory.costArcher;
	}
	
	public static boolean canBuildMage() {
		return Player.getInstance().getGold() >= TowerFactory.costMage;
	}
	
	public static boolean canBuildArtillery() {
		return Player.getInstance().getGold() >= TowerFactory.costArtillery;
	}
}
