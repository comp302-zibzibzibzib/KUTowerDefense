package domain.kutowerdefense;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import domain.services.Utilities;


public class GameOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static GameOptions instance;

	// Spawning
	private int numberOfWaves;
	private int maxGroupPerWave;
	private int minGroupPerWave;
	private int maxEnemiesPerGroup;
	private int minEnemiesPerGroup;
	private double knightPercentage;
	private double goblinPercentage;
	private double waveDelay;
	private double groupDelay;
	private double enemyDelay;

	// Player
	private int startingPlayerGold;
    private int startingPlayerLives;

	// Enemies
	private double goblinHealth;
	private double goblinSpeed;
	private int golbinReward;
	private double knightHealth;
	private double knightSpeed;
	private int knightReward;

	// Projectiles
	private double arrowDamage;
	private double artilleryDamage;
	private double spellDamage;
	private double aoeRange;

	// Towers
	private int archerCost;
	private int artilleryCost;
	private int mageCost;
	private double archerRange;
	private double artilleryRange;
	private double mageRange;
    
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

		arrowDamage = 5.0;
		artilleryDamage = 20.0;
		spellDamage = 15.0;

		archerCost = 150;
		artilleryCost = 350;
		mageCost = 250;

		archerRange = 7.0;
		artilleryRange = 5.0;
		mageRange = 6.0;
		aoeRange = 3.0;
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

	public int getMinGroupPerWave() {
		return minGroupPerWave;
	}

	public int getMaxEnemiesPerGroup() {
		return maxEnemiesPerGroup;
	}

	public int getMinEnemiesPerGroup() {
		return minEnemiesPerGroup;
	}

	public double getWaveDelay() {
		return waveDelay;
	}

	public double getGroupDelay() {
		return groupDelay;
	}

	public double getEnemyDelay() {
		return enemyDelay;
	}

	public double getKnightPercentage() {
		return knightPercentage;
	}

	public double getGoblinPercentage() {
		return goblinPercentage;
	}

	public int getStartingPlayerGold() {
		return startingPlayerGold;
	}

	public int getStartingPlayerLives() {
		return startingPlayerLives;
	}

	public double getGoblinHealth() {
		return goblinHealth;
	}

	public double getGoblinSpeed() {
		return goblinSpeed;
	}

	public int getGolbinReward() {
		return golbinReward;
	}

	public double getKnightHealth() {
		return knightHealth;
	}

	public double getKnightSpeed() {
		return knightSpeed;
	}

	public int getKnightReward() {
		return knightReward;
	}

	public double getArrowDamage() {
		return arrowDamage;
	}

	public double getArtilleryDamage() {
		return artilleryDamage;
	}

	public double getSpellDamage() {
		return spellDamage;
	}

	public int getArcherCost() {
		return archerCost;
	}

	public int getArtilleryCost() {
		return artilleryCost;
	}

	public int getMageCost() {
		return mageCost;
	}

	public double getArcherRange() {
		return archerRange;
	}

	public double getArtilleryRange() {
		return artilleryRange;
	}

	public double getMageRange() {
		return mageRange;
	}

	public double getAoeRange() {
		return aoeRange;
	}

	// Below is for controller use
	public void setNumberOfWaves(Number numberOfWaves) {
		this.numberOfWaves = numberOfWaves.intValue();
	}

	public void setMaxGroupPerWave(Number maxGroupPerWave) {
		this.maxGroupPerWave = maxGroupPerWave.intValue();
	}

	public void setMinGroupPerWave(Number minGroupPerWave) {
		this.minGroupPerWave = minGroupPerWave.intValue();
	}

	public void setMaxEnemiesPerGroup(Number maxEnemiesPerGroup) {
		this.maxEnemiesPerGroup = maxEnemiesPerGroup.intValue();
	}

	public void setMinEnemiesPerGroup(Number minEnemiesPerGroup) {
		this.minEnemiesPerGroup = minEnemiesPerGroup.intValue();
	}

	public void setWaveDelay(Number waveDelay) {
		this.waveDelay = waveDelay.doubleValue();
	}

	public void setGroupDelay(Number groupDelay) {
		this.groupDelay = groupDelay.doubleValue();
	}

	public void setEnemyDelay(Number enemyDelay) {
		this.enemyDelay = enemyDelay.doubleValue();
	}

	public void setKnightPercentage(Number knightPercentage) {
		this.knightPercentage = knightPercentage.doubleValue();
	}

	public void setGoblinPercentage(Number goblinPercentage) {
		this.goblinPercentage = goblinPercentage.doubleValue();
	}

	public void setStartingPlayerGold(Number startingPlayerGold) {
		this.startingPlayerGold = startingPlayerGold.intValue();
	}

	public void setStartingPlayerLives(Number startingPlayerLives) {
		this.startingPlayerLives = startingPlayerLives.intValue();
	}

	public void setGoblinHealth(Number goblinHealth) {
		this.goblinHealth = goblinHealth.doubleValue();
	}

	public void setGoblinSpeed(Number goblinSpeed) {
		this.goblinSpeed = goblinSpeed.doubleValue();
	}

	public void setGolbinReward(Number golbinReward) {
		this.golbinReward = golbinReward.intValue();
	}

	public void setKnightHealth(Number knightHealth) {
		this.knightHealth = knightHealth.doubleValue();
	}

	public void setKnightSpeed(Number knightSpeed) {
		this.knightSpeed = knightSpeed.doubleValue();
	}

	public void setKnightReward(Number knightReward) {
		this.knightReward = knightReward.intValue();
	}

	public void setArrowDamage(Number arrowDamage) {
		this.arrowDamage = arrowDamage.doubleValue();
	}

	public void setArtilleryDamage(Number artilleryDamage) {
		this.artilleryDamage = artilleryDamage.doubleValue();
	}

	public void setSpellDamage(Number spellDamage) {
		this.spellDamage = spellDamage.doubleValue();
	}

	public void setArcherCost(Number archerCost) {
		this.archerCost = archerCost.intValue();
	}

	public void setArtilleryCost(Number artilleryCost) {
		this.artilleryCost = artilleryCost.intValue();
	}

	public void setMageCost(Number mageCost) {
		this.mageCost = mageCost.intValue();
	}

	public void setArcherRange(Number archerRange) {
		this.archerRange = archerRange.doubleValue();
	}

	public void setArtilleryRange(Number artilleryRange) {
		this.artilleryRange = artilleryRange.doubleValue();
	}

	public void setMageRange(Number mageRange) {
		this.mageRange = mageRange.doubleValue();
	}

	public void setAoeRange(Number aoeRange) {
		this.aoeRange = aoeRange.doubleValue();
	}

	public static void nullifyOptions() {
		instance = null;
	}
}
