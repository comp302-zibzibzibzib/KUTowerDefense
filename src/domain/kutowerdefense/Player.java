package domain.kutowerdefense;

import domain.map.Lot;
import domain.tower.AttackType;

public class Player {
	private static final int STARTING_GOLD = 200; // ??
	private static final int STARTING_LIVES = 20;
	
	private static Player instance;
	
	public int gold;
	public int lives;
	public int waveNumber;
	
	private Player() {
		this.gold = STARTING_GOLD; this.lives = STARTING_LIVES; this.waveNumber = 0;
	}
	
	public static void constructTower(Lot lot, AttackType attackType) {
		
	}
	
	public static void sellTower(Lot lot) {
		
	}
	
	public static Player getInstance() {
		if (instance == null) instance = new Player();
		return instance;
	}
	
	public static void resetPlayer() {
		if (instance == null) instance = new Player();
		instance.gold = STARTING_GOLD;
		instance.lives = STARTING_LIVES;
		instance.waveNumber = 0;
	}
	
	public static void takeDamage() {
		if (instance == null) instance = new Player();
		instance.lives -= 1;
	}
}
