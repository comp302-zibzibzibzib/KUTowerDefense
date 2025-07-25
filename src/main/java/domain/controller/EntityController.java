package domain.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import domain.entities.*;
import domain.kutowerdefense.PlayModeManager;
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
        // Handle method that sequentially spawns enemies
        // Gets inserted into the JavaFX main application loop
    	if (PlayModeManager.getInstance().getGameSpeed() == 0.0) {
			lastUpdate = now;
			return;
		}
    	
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        double deltaTime = (double) (now - lastUpdate) / 1_000_000_000.0;
        lastUpdate = now;
        
        totalElapsed += deltaTime * PlayModeManager.getInstance().getGameSpeed();

        // Update elapsed time for all spawner objects
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
        // Updates manager state which in turn updates wave states
        if (totalElapsed > PlayModeManager.GRACE_PERIOD_SECONDS) {
            PlayModeManager.getInstance().initializeWaves(deltaTime);
        }
    }

    private void updateWaves(double deltaTime) {
        // Update wave states to spawn groups
        List<Wave> waves = PlayModeManager.getInstance().getWaves();
        for (Wave wave : waves) {
            if (wave.isSpawning()) {
                wave.spawnGroups(deltaTime);
            }
        }
    }

    private void updateGroups(double deltaTime) {
        // Update elapsed time in active group objects
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
}

class MovementTimer extends AnimationTimer {
    // Tasked with calling every active enemy's update method with a fixed delta time
    // Also updates the projectile motion for gold bag entities
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
		
		double deltaTime = (double) (now - lastUpdate) / 1_000_000_000;
		lastUpdate = now;
		
		List<Enemy> enemyList = new ArrayList<Enemy>(Enemy.getAllEnemies());
		for (Enemy enemy : enemyList) {
			if (!enemy.isInitialized()) continue;

			enemy.updateEnemy(deltaTime);
		}

        GoldBag.bagUpdate(deltaTime);

		if (Enemy.getAllEnemies().isEmpty()) {
			stop();
        }
	}
}

public class EntityController {
    // Controller class responsible for ui and entities package communication
    private static SpawnerLoopTimer gameLoop;
    private static MovementTimer moveTimer;

    private EntityController() {} // private constructor

    public static void startEntityLogic() {
        // Start the spawner and movement animation timers at the start of the game
        if (gameLoop == null) {
            gameLoop = new SpawnerLoopTimer();
        }
        gameLoop.start();
        if (moveTimer == null) moveTimer = new MovementTimer();
        moveTimer.start();
    }

    public static void addRemovedEnemyListener(Consumer<Boolean> consumer) {
        // Add a listener for every enemy death, to check if the player won or not
        Enemy.addEnemyRemovedListener(consumer);
    }

    public static void removeEnemyRemovedListener(Consumer<Boolean> consumer) {
        Enemy.removeEnemyRemovedListener(consumer);
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
    	List<Integer> enemyIDs = Enemy.getActiveEnemies().stream().map(c -> c.getEnemyID()).toList();
        return enemyIDs.contains(id);
    }
    
    public static int getNumberOfEnemies() {
    	return Enemy.getActiveEnemies().size();
    }

    public static List<Integer> getEnemyIDsRenderSort() {
        // This sorts the enemies from the furthest up to down so that we can change their
        // ordering on the ui so they don't overlap each other
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

        List<Integer> ids = enemyList.stream().map(Enemy::getEnemyID).collect(Collectors.toList());

        return ids;
    }
    
    public static int getEnemyID(int i) {
    	return Enemy.getActiveEnemies().get(i).getEnemyID();
    }
    
    public static boolean isGoblin(int i) {
    	return Enemy.getActiveEnemies().get(i) instanceof Goblin;
    }
    
    public static boolean isKnight(int i) {
    	return Enemy.getActiveEnemies().get(i) instanceof Knight;
    }
    
    public static int getXScale(int i) {
        // Helps turn the enemy sprite to the direction the enemy is moving
    	double[] direction = Enemy.getActiveEnemies().get(i).getDirection();
    	return (int) Math.signum(direction[0]);
    }
    
    public static int getYScale(int i) {
    	double[] direction = Enemy.getActiveEnemies().get(i).getDirection();
    	return (int) Math.signum(direction[1]);
    }
    
    public static void stop() {
    	if (gameLoop != null) gameLoop.stop();
    	if (moveTimer != null) moveTimer.stop();
        resetEnemies();
        GoldBag.resetBags();
    }

    public static void resetEnemies() {
        // Clear all static enemy lists
        Enemy.enemies = new ArrayList<Enemy>();
        Enemy.activeEnemies = new ArrayList<Enemy>();
    }

    public static boolean isKnightFast(int i) {
        // Check if an active knight is fast
        Enemy enemy = Enemy.getActiveEnemies().get(i);
        if (!(enemy instanceof Knight)) return false;
        return ((Knight) enemy).isFast();
    }

    public static boolean isEnemySlowedDown(int i) {
        // Check if an active enemy is slowed down
        Enemy enemy = Enemy.getActiveEnemies().get(i);
        return enemy.isSlowedDown();
    }

    public static boolean isGoldBagDead(int id) {
        return !GoldBag.getGoldBags().containsKey(id);
    }

    public static int getNumberOfGoldBags() {
        return GoldBag.getGoldBags().size();
    }

    public static ArrayList<Integer> getGoldBagIDs() {
        return new ArrayList<Integer>(GoldBag.getGoldBags().keySet());
    }

    public static double getGoldBagX(int id) {
        return GoldBag.getGoldBag(id).getLocation().xCoord;
    }

    public static double getGoldBagY(int id) {
        return GoldBag.getGoldBag(id).getLocation().yCoord;
    }

    public static double getGoldBagTime(int id) {
        return GoldBag.getGoldBag(id).getTimeSinceCreation();
    }

    public static boolean isGoldBagFlashing(int id) {
        return GoldBag.getGoldBag(id).getTimeSinceCreation() >= 7.0;
    }

    public static void pickUpBag(int id) {
        GoldBag.getGoldBag(id).pickUpBag();
    }

    public static List<Integer> getGoldBagsIDsRenderSort() {
        // Same reason with getting the render sort of enemies
        Comparator<GoldBag> comparator = new Comparator<GoldBag>() {
            @Override
            public int compare(GoldBag o1, GoldBag o2) {
                if (o1.getLocation().yCoord < o2.getLocation().yCoord) return -1;
                else if (o1.getLocation().yCoord > o2.getLocation().yCoord) return 1;
                return 0;
            }
        };

        List<GoldBag> goldBags = new ArrayList<>(GoldBag.getGoldBags().values());
        goldBags.sort(comparator);

        List<Integer> goldBagIDs = goldBags.stream().map(GoldBag::getId).collect(Collectors.toList());

        return goldBagIDs;
    }

    public static List<Integer> getEffectIDs() {
        return new ArrayList<>(Effect.getEffects().keySet());
    }

    public static void killEffect(int id) {
        Effect.killEffect(id);
    }

    public static String getEffectName(int id) {
        // Get the name (effectively the type) of an active effect with its ID
        Effect effect = Effect.getEffects().get(id);
        return switch (effect.getAttackType()) {
            case ARTILLERY -> "explosion";
            case SPELL -> "fireRed";
            case SLOW_SPELL -> "fireBlue";
            default -> null;
        };
    }

    public static double getEffectX(int id) {
        Effect effect = Effect.getEffects().get(id);
        return effect.getLocation().xCoord;
    }

    public static double getEffectY(int id) {
        Effect effect = Effect.getEffects().get(id);
        return effect.getLocation().yCoord;
    }
}