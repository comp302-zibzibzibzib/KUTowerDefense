package domain.kutowerdefense;

public class Player {
	// Player singleton which stores data related to the player while in play mode
	private static Player instance;

	// These listeners are for any update behavior to happen when there is a change in their corresponding fields
	// Invoked in setters
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
		goldListener.invoke(gold);
	}

	public void updateGold(int updateAmount) {
		this.gold += updateAmount;
		goldListener.invoke(gold);
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
		livesListener.invoke(lives);
	}

	public int getWaveNumber() {
		waveNumber = PlayModeManager.getInstance().getCurrentWaveIndex() + 1;
		return waveNumber;
	}

	public void setWaveNumber(int waveNumber) {
		this.waveNumber = waveNumber;
		waveNumberListener.invoke(waveNumber);
	}

	private Player() {
		GameOptions options = GameOptions.getInstance();
		this.gold = options.getStartingPlayerGold();
		this.lives = options.getStartingPlayerLives();
		this.waveNumber = 0;
	}

	public static Player getInstance() {
		if (instance == null)
			instance = new Player();
		return instance;
	}

	public static void resetPlayer() {
		if (instance == null)
			instance = new Player();
		instance.gold = GameOptions.getInstance().getStartingPlayerGold();
		instance.lives = GameOptions.getInstance().getStartingPlayerLives();
		instance.waveNumber = 0;
		instance.goldListener.invoke(GameOptions.getInstance().getStartingPlayerGold());
		instance.livesListener.invoke(GameOptions.getInstance().getStartingPlayerLives());
		instance.waveNumberListener.invoke(GameOptions.getInstance().getNumberOfWaves());
	}

	public void takeDamage() {
		this.lives -= 1;
		livesListener.invoke(lives);
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
