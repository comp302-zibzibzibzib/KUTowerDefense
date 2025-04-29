package domain.kutowerdefense;

import domain.map.Lot;
import domain.tower.AttackType;

public class Player {
	private static final int STARTING_GOLD = 200; // ??
	private static final int STARTING_LIVES = 20;
	
	private static Player instance;
	
	private int gold;
	private int lives;
	private int waveNumber;
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public void updateGold(int updateAmount) {
		this.gold += updateAmount;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getWaveNumber() {
		if (waveNumber == 0) PlayModeManager.getInstance().getCurrentWaveIndex();
		return waveNumber;
	}

	public void setWaveNumber(int waveNumber) {
		this.waveNumber = waveNumber;
	}

	private Player() {
		GameOptions options = GameOptions.getInstance();
		this.gold = options.getStartingPlayerGold(); this.lives = options.getStartingPlayerLives(); this.waveNumber = 0;
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
	
	public void takeDamage() {
		this.lives -= 1;
	}
}
