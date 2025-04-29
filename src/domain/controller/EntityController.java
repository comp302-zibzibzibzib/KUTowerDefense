package domain.controller;

import java.util.ArrayList;
import java.util.List;

import domain.entities.Enemy;
import domain.entities.Group;
import domain.entities.Wave;
import domain.kutowerdefense.PlayModeManager;
import javafx.animation.AnimationTimer;

class SpawnerLoopTimer extends AnimationTimer {
	private boolean running = false;
	
    private long lastUpdate = 0;
    private double totalElapsed = 0;
    private PlayModeManager manager = PlayModeManager.getInstance();
    
    @Override
    public void start() {
    	super.start();
    	running = true;
    }
    
    @Override
    public void stop() {
    	super.stop();
    	testFunctionality();
    	running = false;
    }

    @Override
    public void handle(long now) {
    	if (PlayModeManager.getInstance().getGameSpeed() == 0.0) return;
    	
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
        lastUpdate = now;
        
        totalElapsed += deltaTime * manager.getGameSpeed();

        updateManager(deltaTime);
        updateWaves(deltaTime);
        updateGroups(deltaTime);

        if (manager.spawnedAllWaves()) {
            System.out.println("All waves spawned. Stopping spawner loop.");
            stop();
        }
    }
    
    public boolean isRunning() {
    	return running;
    }

    private void updateManager(double deltaTime) {
        if (totalElapsed > PlayModeManager.GRACE_PERIOD_SECONDS) {
            manager.initializeWaves(deltaTime);
        }
    }

    private void updateWaves(double deltaTime) {
        List<Wave> waves = manager.getWaves();
        for (Wave wave : waves) {
            if (wave.isSpawning()) {
                wave.spawnGroups(deltaTime);
            }
        }
    }

    private void updateGroups(double deltaTime) {
        List<Wave> waves = manager.getWaves();
        for (Wave wave : waves) {
            List<Group> groups = wave.getGroups();
            for (Group group : groups) {
                if (group.isSpawning()) {
                    group.initializeEnemies(deltaTime);
                }
            }
        }
    }
    
    private void testFunctionality() {
    	PlayModeManager man = PlayModeManager.getInstance();
    	
    	boolean passed = true;
    	for (Enemy enemy : Enemy.getAllEnemies()) {
			if (!enemy.getLocation().equals(man.getCurrentMap().getStartingTile().getLocation())
					&& enemy.isInitalized()) {
				passed = false;
				break;
			}
		}
    	if (passed) System.out.println("Initialization Test - PASSED!");
		else System.out.println("Initialization Test - PASSED!");
    }
}

class MovementTimer extends AnimationTimer {
	private long lastUpdate = 0;
	private int s = 0;
	private double totalTimeElapsed = 0;
	
	@Override
	public void handle(long now) {
		if (PlayModeManager.getInstance().getGameSpeed() == 0.0) return;
		
		if (lastUpdate == 0) {
			lastUpdate = now;
			return;
		}
		
		long deltaTime = (now - lastUpdate);
		lastUpdate = now;
		
		List<Enemy> enemyList = new ArrayList<Enemy>(Enemy.getAllEnemies());
		for (Enemy enemy : enemyList) {
			if (!enemy.isInitalized()) continue;
			
			enemy.moveEnemy(deltaTime);
			
			if(enemy.getPathIndex() == Enemy.path.size()-1) {
            	System.out.printf("Enemy %d: Reached End%n", enemy.getEnemyID());
            }
		}
		
		if (Enemy.getAllEnemies().isEmpty()) {
			stop();
			return;
		}
	}
}

public class EntityController {
    private static SpawnerLoopTimer gameLoop;
    private static MovementTimer moveTimer;

    private EntityController() {} // private constructor

    public static void startEntityLogic() {
        if (gameLoop == null) {
            gameLoop = new SpawnerLoopTimer();
        }
        gameLoop.start();
        if (moveTimer == null) moveTimer = new MovementTimer();
        moveTimer.start();
    }
    
    public static boolean spawningEnemies() {
    	return gameLoop.isRunning();
    }
    
    public static double getEnemyXCoord(int i) {
    	return Enemy.getAllEnemies().get(i).getLocation().getXCoord();
    }
    
    public static double getEnemyYCoord(int i) {
    	return Enemy.getAllEnemies().get(i).getLocation().getYCoord();
    }
    
    public static boolean isEnemyInitialized(int i) {
    	return Enemy.getAllEnemies().get(i).isInitalized();
    }
}