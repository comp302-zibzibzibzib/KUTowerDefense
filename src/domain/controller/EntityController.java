package domain.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import domain.entities.Enemy;
import domain.entities.Goblin;
import domain.entities.Group;
import domain.entities.Knight;
import domain.entities.Wave;
import domain.kutowerdefense.PlayModeManager;
import domain.map.PathTile;
import javafx.animation.AnimationTimer;

class SpawnerLoopTimer extends AnimationTimer {
	private boolean running = false;
	
    private long lastUpdate = 0;
    private double totalElapsed = 0;
    
    @Override
    public void start() {
    	super.start();
    	lastUpdate = 0;
    	running = true;
    }
    
    @Override
    public void stop() {
    	super.stop();
    	lastUpdate = 0;
    	//testFunctionality();
    	running = false;
    }

    @Override
    public void handle(long now) {
    	if (PlayModeManager.getInstance().getGameSpeed() == 0.0) {
			lastUpdate = now;
			return;
		}
    	
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
        lastUpdate = now;
        
        totalElapsed += deltaTime * PlayModeManager.getInstance().getGameSpeed();

        updateManager(deltaTime);
        updateWaves(deltaTime);
        updateGroups(deltaTime);

        if (PlayModeManager.getInstance().spawnedAllWaves()) {
            System.out.println("All waves spawned. Stopping spawner loop.");
            stop();
        }
    }
    
    public boolean isRunning() {
    	return running;
    }

    private void updateManager(double deltaTime) {
        if (totalElapsed > PlayModeManager.GRACE_PERIOD_SECONDS) {
            PlayModeManager.getInstance().initializeWaves(deltaTime);
        }
    }

    private void updateWaves(double deltaTime) {
        List<Wave> waves = PlayModeManager.getInstance().getWaves();
        for (Wave wave : waves) {
            if (wave.isSpawning()) {
                wave.spawnGroups(deltaTime);
            }
        }
    }

    private void updateGroups(double deltaTime) {
        List<Wave> waves = PlayModeManager.getInstance().getWaves();
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
					&& enemy.isInitialized()) {
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
		if (PlayModeManager.getInstance().getGameSpeed() == 0.0) {
			lastUpdate = now;
			return;
		}
		
		if (lastUpdate == 0) {
			lastUpdate = now;
			return;
		}
		
		long deltaTime = (now - lastUpdate);
		lastUpdate = now;
		
		List<Enemy> enemyList = new ArrayList<Enemy>(Enemy.getAllEnemies());
		for (Enemy enemy : enemyList) {
			if (!enemy.isInitialized()) continue;

			enemy.updateEnemy(deltaTime);
			
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
    	return Enemy.getActiveEnemies().get(i).getLocation().getXCoord();
    }
    
    public static double getEnemyYCoord(int i) {
    	return Enemy.getActiveEnemies().get(i).getLocation().getYCoord();
    }

    public static void addEnemyHPListener(int i, Consumer<Double> consumer) {
        Enemy enemy = Enemy.getActiveEnemies().get(i);
        enemy.getHitPointsListener().addListener(consumer);
    }
    
    public static boolean isEnemyInitialized(int i) {
    	return Enemy.getAllEnemies().get(i).isInitialized();
    }
    
    public static boolean isEnemyIDInitialized(int id) {
    	for (Enemy enemy : Enemy.getAllEnemies()) {
    		if (enemy.getEnemyID() == id) return enemy.isInitialized();
    	} return false;
    }
    
    public static int getNumberOfEnemies() {
    	return Enemy.getActiveEnemies().size();
    }

    public static ArrayList<Integer> getEnemyIDsRenderSort() {
        Comparator<Enemy> comparator = new Comparator<Enemy>() {
            @Override
            public int compare(Enemy o1, Enemy o2) {
                if (o1.getLocation().yCoord < o2.getLocation().yCoord) return -1;
                else if (o1.getLocation().yCoord > o2.getLocation().yCoord) return 1;
                return 0;
            }
        };

        ArrayList<Enemy> enemyList = new ArrayList<>(Enemy.getActiveEnemies());
        enemyList.sort(comparator);

        ArrayList<Integer> ids = new ArrayList<>();
        for (Enemy enemy : enemyList) {
            ids.add(enemy.getEnemyID());
        }

        return ids;
    }
    
    public static int getEnemyID(int i) {
    	return Enemy.getAllEnemies().get(i).getEnemyID();
    }
    
    public static boolean isGoblin(int i) {
    	return Enemy.getAllEnemies().get(i) instanceof Goblin;
    }
    
    public static boolean isKnight(int i) {
    	return Enemy.getAllEnemies().get(i) instanceof Knight;
    }
    
    public static int getXScale(int i) {
    	double[] direction = Enemy.getAllEnemies().get(i).getDirection();
    	return (int) Math.signum(direction[0]);
    }
    
    public static int getYScale(int i) {
    	double[] direction = Enemy.getAllEnemies().get(i).getDirection();
    	return (int) Math.signum(direction[1]);
    }
    
    public static void stop() {
    	if (gameLoop != null) gameLoop.stop();
    	if (moveTimer != null) moveTimer.stop();
        resetEnemies();
    }

    public static void resetEnemies() {
        Enemy.enemies = new ArrayList<Enemy>();
        Enemy.activeEnemies = new ArrayList<Enemy>();
        Enemy.path = new ArrayList<PathTile>();
    }

    public static boolean isKnightFast(int i) {
        Enemy enemy = Enemy.getActiveEnemies().get(i);
        if (!(enemy instanceof Knight)) return false;
        return ((Knight) enemy).isFast();
    }

    public static boolean isEnemySlowedDown(int i) {
        Enemy enemy = Enemy.getActiveEnemies().get(i);
        return enemy.isSlowedDown();
    }
}