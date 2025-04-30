package domain.kutowerdefense;

import java.io.FileNotFoundException;
import java.io.Serializable;

import domain.services.Utilities;


public class GameOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static GameOptions instance;
    
    private int startingPlayerLives = 20;
    private int startingPlayerGold = 300;
    private int numberOfWaves = 10;
    private double enemySpeed = 4;
    
    public static GameOptions getDefaultOptions() {
    	return new GameOptions();
    }
    
    public void resetOptions() {
    	GameOptions defaultOptions = Utilities.readDefaultOptions();
    	
    	this.startingPlayerLives = defaultOptions.startingPlayerLives;
    	this.startingPlayerGold = defaultOptions.startingPlayerGold;
    	this.numberOfWaves = defaultOptions.numberOfWaves;
    	this.enemySpeed = defaultOptions.enemySpeed;
    	
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
    	startingPlayerLives = 20;
    	startingPlayerGold = 300;
    	numberOfWaves = 10;
    	enemySpeed = 4;
    }

	public double getEnemySpeed() {
		return enemySpeed;
	}

	public void setEnemySpeed(double enemySpeed) {
		this.enemySpeed = enemySpeed;
	}

	public int getStartingPlayerLives() {
		return startingPlayerLives;
	}

	public void setStartingPlayerLives(int startingPlayerLives) {
		this.startingPlayerLives = startingPlayerLives;
	}

	public int getStartingPlayerGold() {
		return startingPlayerGold;
	}

	public void setStartingPlayerGold(int startingPlayerGold) {
		this.startingPlayerGold = startingPlayerGold;
	}

	public int getNumberOfWaves() {
		return numberOfWaves;
	}

	public void setNumberOfWaves(int numberOfWaves) {
		this.numberOfWaves = numberOfWaves;
	}
}
