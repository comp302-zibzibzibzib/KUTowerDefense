package domain.kutowerdefense;

public class Player {
	private static final int STARTING_GOLD = 200; // ??
	private static final int STARTING_LIVES = 20;
	
	private static Player instance;
	
	private PlayerValueListener<Integer> goldListener = new PlayerValueListener<Integer>();
	private PlayerValueListener<Integer> livesListener = new PlayerValueListener<Integer>();
	private PlayerValueListener<Integer> waveNumberListener = new PlayerValueListener<Integer>();
	
	private int gold;
	private int lives;
	private int waveNumber;
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
		goldListener.invoke();
	}
	
	public void updateGold(int updateAmount) {
		this.gold += updateAmount;
		goldListener.invoke();
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
		livesListener.invoke();
	}

	public int getWaveNumber() {
		waveNumber = PlayModeManager.getInstance().getCurrentWaveIndex() + 1;
		return waveNumber;
	}

	public void setWaveNumber(int waveNumber) {
		this.waveNumber = waveNumber;
		waveNumberListener.invoke();
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
		instance.goldListener.invoke();
		instance.livesListener.invoke();
		instance.waveNumberListener.invoke();
	}
	
	public void takeDamage() {
		this.lives -= 1;
		livesListener.invoke();
	}
	
	public PlayerValueListener getGoldListener() {
		return goldListener;
	}
	
	public PlayerValueListener getLivesListener() {
		return livesListener;
	}
	
	public PlayerValueListener getWaveNumberListener() {
		return waveNumberListener;
	}
	
}
