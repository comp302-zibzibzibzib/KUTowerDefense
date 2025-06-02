package domain.kutowerdefense;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import domain.services.Utilities;


public class GameOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static GameOptions instance;

	private int numberOfWaves;
	private int maxGroupPerWave;
	private int minGroupPerWave;
	private int maxEnemiesPerGroup;
	private int minEnemiesPerGroup;
	private double waveDelay;
	private double groupDelay;
	private double enemyDelay;

	private double knightPercentage;
	private double goblinPercentage;

	private int startingPlayerGold;
    private int startingPlayerLives;

	private double goblinHealth;
	private double goblinSpeed;
	private int golbinReward;

	private double knightHealth;
	private double knightSpeed;
	private int knightReward;

	private double arrowDagame;
	private double artilleryDamage;
	private double spellDamage;

	private int archerCost;
	private int artilleryCost;
	private int mageCost;

	private double archerRange;
	private double artilleryRange;
	private double mageRange;
	private double aoeRange;
    
    public static GameOptions getDefaultOptions() {
    	return new GameOptions();
    }
    
    public void resetOptions() {
    	GameOptions defaultOptions = Utilities.readDefaultOptions();

		for (Field field : GameOptions.class.getDeclaredFields()) {
			if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
				continue;
			}

			field.setAccessible(true);
			try {
				Object value = field.get(defaultOptions);
				field.set(this, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
    	
    	Utilities.writeOptions();
    }
    
    public static void initializeGameOptions() {
    	try {
			instance = Utilities.readOptions();
		} catch (Exception e) {
			instance = Utilities.readDefaultOptions();
		}
    }
    
    public static GameOptions getInstance() {
    	if (instance == null) {
    		try {
    			instance = Utilities.readOptions();
    		} catch (Exception e) {
    			instance = Utilities.readDefaultOptions();
    		}
    	}
    	return instance;
    }
    
    private GameOptions() {
		numberOfWaves = 10;
		maxGroupPerWave = 5;
		minGroupPerWave = 1;
		maxEnemiesPerGroup = 6;
		minEnemiesPerGroup = 2;
		waveDelay = 4.0;
		groupDelay = 1.0;
		enemyDelay = 0.5;

		knightPercentage = 0.5;
		goblinPercentage = 0.5;

		startingPlayerGold = 200;
		startingPlayerLives = 20;

		goblinHealth = 50.0;
		goblinSpeed = 6.0;
		golbinReward = 10;

		knightHealth = 80.0;
		knightSpeed = 3.0;
		knightReward = 20;

		arrowDagame = 5.0;
		artilleryDamage = 20.0;
		spellDamage = 10.0;

		archerCost = 150;
		artilleryCost = 350;
		mageCost = 250;

		archerRange = 7.0;
		artilleryRange = 5.0;
		mageRange = 6.0;
		aoeRange = 3.0;
    }

	public static void setInstance(GameOptions instance) {
		GameOptions.instance = instance;
	}

	public int getNumberOfWaves() {
		return numberOfWaves;
	}

	public void setNumberOfWaves(int numberOfWaves) {
		this.numberOfWaves = numberOfWaves;
	}

	public int getMaxGroupPerWave() {
		return maxGroupPerWave;
	}

	public void setMaxGroupPerWave(int maxGroupPerWave) {
		this.maxGroupPerWave = maxGroupPerWave;
	}

	public int getMinGroupPerWave() {
		return minGroupPerWave;
	}

	public void setMinGroupPerWave(int minGroupPerWave) {
		this.minGroupPerWave = minGroupPerWave;
	}

	public int getMaxEnemiesPerGroup() {
		return maxEnemiesPerGroup;
	}

	public void setMaxEnemiesPerGroup(int maxEnemiesPerGroup) {
		this.maxEnemiesPerGroup = maxEnemiesPerGroup;
	}

	public int getMinEnemiesPerGroup() {
		return minEnemiesPerGroup;
	}

	public void setMinEnemiesPerGroup(int minEnemiesPerGroup) {
		this.minEnemiesPerGroup = minEnemiesPerGroup;
	}

	public double getWaveDelay() {
		return waveDelay;
	}

	public void setWaveDelay(double waveDelay) {
		this.waveDelay = waveDelay;
	}

	public double getGroupDelay() {
		return groupDelay;
	}

	public void setGroupDelay(double groupDelay) {
		this.groupDelay = groupDelay;
	}

	public double getEnemyDelay() {
		return enemyDelay;
	}

	public void setEnemyDelay(double enemyDelay) {
		this.enemyDelay = enemyDelay;
	}

	public double getKnightPercentage() {
		return knightPercentage;
	}

	public void setKnightPercentage(double knightPercentage) {
		this.knightPercentage = knightPercentage;
	}

	public double getGoblinPercentage() {
		return goblinPercentage;
	}

	public void setGoblinPercentage(double goblinPercentage) {
		this.goblinPercentage = goblinPercentage;
	}

	public int getStartingPlayerGold() {
		return startingPlayerGold;
	}

	public void setStartingPlayerGold(int startingPlayerGold) {
		this.startingPlayerGold = startingPlayerGold;
	}

	public int getStartingPlayerLives() {
		return startingPlayerLives;
	}

	public void setStartingPlayerLives(int startingPlayerLives) {
		this.startingPlayerLives = startingPlayerLives;
	}

	public double getGoblinHealth() {
		return goblinHealth;
	}

	public void setGoblinHealth(double goblinHealth) {
		this.goblinHealth = goblinHealth;
	}

	public double getGoblinSpeed() {
		return goblinSpeed;
	}

	public void setGoblinSpeed(double goblinSpeed) {
		this.goblinSpeed = goblinSpeed;
	}

	public int getGolbinReward() {
		return golbinReward;
	}

	public void setGolbinReward(int golbinReward) {
		this.golbinReward = golbinReward;
	}

	public double getKnightHealth() {
		return knightHealth;
	}

	public void setKnightHealth(double knightHealth) {
		this.knightHealth = knightHealth;
	}

	public double getKnightSpeed() {
		return knightSpeed;
	}

	public void setKnightSpeed(double knightSpeed) {
		this.knightSpeed = knightSpeed;
	}

	public int getKnightReward() {
		return knightReward;
	}

	public void setKnightReward(int knightReward) {
		this.knightReward = knightReward;
	}

	public double getArrowDagame() {
		return arrowDagame;
	}

	public void setArrowDagame(double arrowDagame) {
		this.arrowDagame = arrowDagame;
	}

	public double getArtilleryDamage() {
		return artilleryDamage;
	}

	public void setArtilleryDamage(double artilleryDamage) {
		this.artilleryDamage = artilleryDamage;
	}

	public double getSpellDamage() {
		return spellDamage;
	}

	public void setSpellDamage(double spellDamage) {
		this.spellDamage = spellDamage;
	}

	public int getArcherCost() {
		return archerCost;
	}

	public void setArcherCost(int archerCost) {
		this.archerCost = archerCost;
	}

	public int getArtilleryCost() {
		return artilleryCost;
	}

	public void setArtilleryCost(int artilleryCost) {
		this.artilleryCost = artilleryCost;
	}

	public int getMageCost() {
		return mageCost;
	}

	public void setMageCost(int mageCost) {
		this.mageCost = mageCost;
	}

	public double getArcherRange() {
		return archerRange;
	}

	public void setArcherRange(double archerRange) {
		this.archerRange = archerRange;
	}

	public double getArtilleryRange() {
		return artilleryRange;
	}

	public void setArtilleryRange(double artilleryRange) {
		this.artilleryRange = artilleryRange;
	}

	public double getMageRange() {
		return mageRange;
	}

	public void setMageRange(double mageRange) {
		this.mageRange = mageRange;
	}

	public double getAoeRange() {
		return aoeRange;
	}

	public void setAoeRange(double aoeRange) {
		this.aoeRange = aoeRange;
	}
}
