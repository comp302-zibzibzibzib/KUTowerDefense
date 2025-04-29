package domain.kutowerdefense;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import domain.services.Utilities;
import domain.entities.Group;


public class GameOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static GameOptions instance;
    
    private int startingPlayerLives = 20;
    private int startingPlayerGold = 1000;
    private int numberOfWaves = 10;
    private double enemySpeed = 4;
    
    public static GameOptions getDefaultOptions() {
    	return new GameOptions();
    }
    
    public static void initializeGameOptions() {
    	instance = getInstance();
    }
    
    public static GameOptions getInstance() {
    	if (instance == null) {
    		try {
    			instance = Utilities.readOptions();
    		} catch (Exception e) {
    			Utilities.writeDefaultOptions();
    			instance = Utilities.readDefaultOptions();
    		}
    	}
    	return instance;
    }
    
    public GameOptions() {
    	startingPlayerLives = 20;
    	startingPlayerGold = 1000;
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
