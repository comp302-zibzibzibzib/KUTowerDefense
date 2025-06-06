package domain.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import domain.entities.*;
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

        double deltaTime = (double) (now - lastUpdate) / 1_000_000_000.0;
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

    public static List<Integer> getEnemyIDsRenderSort() {
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
        GoldBag.resetBags();
    }

    public static void resetEnemies() {
        Enemy.enemies = new ArrayList<Enemy>();
        Enemy.activeEnemies = new ArrayList<Enemy>();
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