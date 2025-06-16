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
	private int goblinReward; 
	private double knightHealth; 
	private double knightSpeed; 
	private int knightReward; 
	private double goldBagChance; //ok

	// Projectiles
	private double arrowDamage; 
	private double artilleryDamage; 
	private double spellDamage; 
	private double aoeRange;

	// Towers
	private int archerCost;
	private int archerUpgradeCost;
	private double archerRange;
	private double archerFireRate;
	private int artilleryCost;
	private int artilleryUpgradeCost;
	private double artilleryRange;
	private double artilleryFireRate;
	private int mageCost;
	private int mageUpgradeCost;
	private double mageRange;
	private double mageFireRate;
	
	private double musicVolume;
    
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
		goblinReward = 10;

		knightHealth = 80.0;
		knightSpeed = 3.0;
		knightReward = 20;

		goldBagChance = 0.2;

		arrowDamage = 5.0;
		artilleryDamage = 20.0;
		spellDamage = 15.0;

		archerCost = 150;
		artilleryCost = 350;
		mageCost = 250;

		archerRange = 12.0;
		artilleryRange = 7.0;
		mageRange = 10.0;
		aoeRange = 3.0;

		archerUpgradeCost = 200;
		artilleryUpgradeCost = 400;
		mageUpgradeCost = 300;

		archerFireRate = 5.0;
		artilleryFireRate = 1.0;
		mageFireRate = 2.0;

		musicVolume = 0.5;
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

	public int getGoblinReward() {
		return goblinReward;
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

	public double getGoldBagChance() {
		return goldBagChance;
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

	public double getMusicVolume() { return musicVolume; }

	public int getArcherUpgradeCost() {
		return archerUpgradeCost;
	}
	
	public void setArcherUpgradeCost(int archerUpgradeCost) {
		this.archerUpgradeCost = archerUpgradeCost;
	}
	
	public double getArcherFireRate() {
		return archerFireRate;
	}
	
	public void setArcherFireRate(double archerFireRate) {
		this.archerFireRate = archerFireRate;
	}
	
	public int getArtilleryUpgradeCost() {
		return artilleryUpgradeCost;
	}
	
	public void setArtilleryUpgradeCost(int artilleryUpgradeCost) {
		this.artilleryUpgradeCost = artilleryUpgradeCost;
	}
	
	public double getArtilleryFireRate() {
		return artilleryFireRate;
	}
	
	public void setArtilleryFireRate(double artilleryFireRate) {
		this.artilleryFireRate = artilleryFireRate;
	}
	
	public int getMageUpgradeCost() {
		return mageUpgradeCost;
	}
	
	public void setMageUpgradeCost(int mageUpgradeCost) {
		this.mageUpgradeCost = mageUpgradeCost;
	}
	
	public double getMageFireRate() {
		return mageFireRate;
	}
	
	public void setMageFireRate(double mageFireRate) {
		this.mageFireRate = mageFireRate;
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
		this.goblinPercentage = 1 - this.knightPercentage;
	}

	public void setGoblinPercentage(Number goblinPercentage) {
		this.goblinPercentage = goblinPercentage.doubleValue();
		this.knightPercentage = 1 - this.goblinPercentage;
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

	public void setGoblinReward(Number goblinReward) {
		this.goblinReward = goblinReward.intValue();
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

	public void setGoldBagChance(Number goldBagChance) {
		this.goldBagChance = goldBagChance.doubleValue();
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

	public void setMusicVolume(Number musicVolume) {
		this.musicVolume = musicVolume.doubleValue();
	}

	public void setMaxGroupPerWave(int maxGroupPerWave) {
		this.maxGroupPerWave = maxGroupPerWave;
	}
	
	public void setMinGroupPerWave(int minGroupPerWave) {
		this.minGroupPerWave = minGroupPerWave;
	}
	
	public void setMaxEnemiesPerGroup(int maxEnemiesPerGroup) {
		this.maxEnemiesPerGroup = maxEnemiesPerGroup;
	}
	
	public void setMinEnemiesPerGroup(int minEnemiesPerGroup) {
		this.minEnemiesPerGroup = minEnemiesPerGroup;
	}
	
	public void setWaveDelay(double waveDelay) {
		this.waveDelay = waveDelay;
	}
	
	public void setGroupDelay(double groupDelay) {
		this.groupDelay = groupDelay;
	}
	
	public void setEnemyDelay(double enemyDelay) {
		this.enemyDelay = enemyDelay;
	}
	
	public void setKnightPercentage(double knightPercentage) {
		this.knightPercentage = knightPercentage;
		this.goblinPercentage = 1 - this.knightPercentage;
	}
	
	public void setGoblinPercentage(double goblinPercentage) {
		this.goblinPercentage = goblinPercentage;
		this.knightPercentage = 1 - this.goblinPercentage;
	}
	
	public void setStartingPlayerGold(int startingPlayerGold) {
		this.startingPlayerGold = startingPlayerGold;
	}
	
	public void setStartingPlayerLives(int startingPlayerLives) {
		this.startingPlayerLives = startingPlayerLives;
	}
	
	public void setGoblinHealth(double goblinHealth) {
		this.goblinHealth = goblinHealth;
	}
	
	public void setGoblinSpeed(double goblinSpeed) {
		this.goblinSpeed = goblinSpeed;
	}
	
	public void setGoblinReward(int goblinReward) {
		this.goblinReward = goblinReward;
	}
	
	public void setKnightHealth(double knightHealth) {
		this.knightHealth = knightHealth;
	}
	
	public void setKnightSpeed(double knightSpeed) {
		this.knightSpeed = knightSpeed;
	}
	
	public void setKnightReward(int knightReward) {
		this.knightReward = knightReward;
	}
	
	public void setGoldBagChance(double goldBagChance) {
		this.goldBagChance = goldBagChance;
	}
	
	public void setArrowDamage(double arrowDamage) {
		this.arrowDamage = arrowDamage;
	}
	
	public void setArtilleryDamage(double artilleryDamage) {
		this.artilleryDamage = artilleryDamage;
	}
	
	public void setSpellDamage(double spellDamage) {
		this.spellDamage = spellDamage;
	}
	
	public void setArcherCost(int archerCost) {
		this.archerCost = archerCost;
	}
	
	public void setArtilleryCost(int artilleryCost) {
		this.artilleryCost = artilleryCost;
	}
	
	public void setMageCost(int mageCost) {
		this.mageCost = mageCost;
	}
	
	public void setArcherRange(double archerRange) {
		this.archerRange = archerRange;
	}
	
	public void setArtilleryRange(double artilleryRange) {
		this.artilleryRange = artilleryRange;
	}
	
	public void setMageRange(double mageRange) {
		this.mageRange = mageRange;
	}
	
	public void setAoeRange(double aoeRange) {
		this.aoeRange = aoeRange;
	}
	
	public void setArcherUpgradeCost(Number archerUpgradeCost) {
		this.archerUpgradeCost = archerUpgradeCost.intValue();
	}
	
	public void setArcherFireRate(Number archerFireRate) {
		this.archerFireRate = archerFireRate.doubleValue();
	}
	
	public void setArtilleryUpgradeCost(Number artilleryUpgradeCost) {
		this.artilleryUpgradeCost = artilleryUpgradeCost.intValue();
	}
	
	public void setArtilleryFireRate(Number artilleryFireRate) {
		this.artilleryFireRate = artilleryFireRate.doubleValue();
	}
	
	public void setMageUpgradeCost(Number mageUpgradeCost) {
		this.mageUpgradeCost = mageUpgradeCost.intValue();
	}
	
	public void setMageFireRate(Number mageFireRate) {
		this.mageFireRate = mageFireRate.doubleValue();
	}
	
	public static void nullifyOptions() {
		instance = null;
	}
}
