package domain.entities;

import java.util.*;
import java.util.function.Consumer;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.PathTile;
import domain.map.Tile;
import domain.map.Map;
import domain.services.Utilities;
import domain.tower.AttackType;
import domain.tower.Projectile;

public abstract class Enemy {
	private static final double GAUSSIAN_MEAN = 0.0;
	private static final double GAUSSIAN_STANDARD_DEVIATION = 3.0;

	public static List<Consumer<Boolean>> removedEnemyListener = new ArrayList<>();
	public static ArrayList<Enemy> enemies = new ArrayList<>();
	public static ArrayList<Enemy> activeEnemies = new ArrayList<>();
	public List<PathTile> path;
	public List<double[]> pathOffsets = new ArrayList<>();
	private static int numberOfEnemies = 0; // Not active enemy number just the total amount created during runtime
	protected double hitPoints;
	protected final double totalHitPoints;
	protected double speed;
	protected double defaultSpeed;
	protected Location location;
	protected int pathIndex;
	protected int enemyID;
	private final EnemyHitPointsListener hitPointsListener = new EnemyHitPointsListener();
	protected boolean slowedDown;
	private boolean initialized = false;
	
	private double[] direction = new double[] {0.0, 0.0};
	private double[] gaussianNoise = new double[] {0.0, 0.0};

	double timeSinceSlowedDown = 0.0;

	public Enemy(double hitPoints, double speed, Location location) {
		this.hitPoints = hitPoints;
		this.totalHitPoints = hitPoints;
		this.speed = speed;
		defaultSpeed = speed;
		this.location = new Location();
        slowedDown = false;
		if (location != null) {
			this.location.xCoord = location.xCoord;
			this.location.yCoord = location.yCoord;
		}
		enemyID = getID();
		enemies.add(this);
	}

	public void initialize() {
		initialized = true;
		activeEnemies.add(this);
		setPath();
		setPathOffsets();
		updateGaussianNoise();
		double[] offset = Map.getEdgeTargetOffset(path.getFirst());

		Location startLocation = getPath().getFirst().getLocation();
		double startX = startLocation.xCoord + offset[0] * Tile.tileLength;	// Offset x
		double startY = startLocation.yCoord + offset[1] * Tile.tileLength; // Offset y
		Location actualStartLocation = new Location(startX, startY);

		setLocation(actualStartLocation);
	}
	
	protected void hitPlayer() {
		Player.getInstance().takeDamage();
		initialized = false;
		cleanupEnemy();
		publishEnemyRemoved();
	}

	public void hitEnemy(double damage, AttackType attackType) {
		if (attackType == AttackType.SLOW_SPELL) slowDown();
		if ((attackType == AttackType.SPELL || attackType == AttackType.SLOW_SPELL) && Utilities.globalRNG.nextDouble() <= 0.03) {
			resetPosition();
			return;
		}
		updateHitPoints(damage);
		if(hitPoints <= 0 && initialized) {
			this.killEnemy();
		}
	}
	
	public void killEnemy() {
		if (!initialized) return;
		// Minus 0.001 so 0.0 is not counted | When chance is zero prevents from spawning
		if (Utilities.globalRNG.nextDouble() - 0.001 <= GameOptions.getInstance().getGoldBagChance()) {
			GoldBagFactory.createGoldBag(new Location(location));
		}
		// Adding gold reward is overriden in Knight and Goblin
		cleanupEnemy();
		publishEnemyRemoved();
	}

	public void slowDown() { // Slow down by 20% when hit by lvl2 mage tower
		timeSinceSlowedDown = 0;
		if(!slowedDown) {
			speed *= 0.8;
			slowedDown = true;
		}
	}
	
	public void endSlowDown() { // Return to normal speed
		timeSinceSlowedDown = 0;
		speed = defaultSpeed;
		slowedDown = false;
	}
	
	// 3% chance to reset back to start when hit by mage tower, can be put somewhere else
	public void resetPosition() {
		double[] offset = Map.getEdgeTargetOffset(path.getFirst());

		Location startLocation = getPath().getFirst().getLocation();
		double startX = startLocation.xCoord + offset[0] * Tile.tileLength;	// Offset x
		double startY = startLocation.yCoord + offset[1] * Tile.tileLength; // Offset y
		Location actualStartLocation = new Location(startX, startY);
		setLocation(actualStartLocation);

		this.pathIndex = 0;

		Projectile.killProjectiles(this);
	}

	// Tests written by Bedirhan Sakaoğlu
	public void updateEnemy(double deltaTime) {
		// REQUIRES: deltaTime is in units of one nanosecond i.e. 10^-9 of a second
		// MODIFIES: Location of this enemy, the current path index of this enemy, the direction this enemy is facing,
		//     		 slowdown cooldown of this enemy
		// EFFECTS: Moves this enemy towards the path tile in the path array on the current path index, updates path
		// 			index if the path is already reached, ends slow down effects if the timer is over
		if(PlayModeManager.getInstance().getGameSpeed() == 0) return;

		// Handle slowed down logic
		deltaTime = deltaTime * PlayModeManager.getInstance().getGameSpeed();

		if (slowedDown) timeSinceSlowedDown += deltaTime;
		if (timeSinceSlowedDown >= 4.0) {
			endSlowDown();
		}

		if (!initialized) return;
		double displacement = this.speed * deltaTime; //get displacement
		
		PathTile nextTile = path.get(pathIndex+1); //get the location of next tile's centre
		
		double[] tileOffset = pathOffsets.get(pathIndex+1);
		double[] offset = new double[] {tileOffset[0] + gaussianNoise[0], tileOffset[1] + gaussianNoise[1]};
		
		double nextX = nextTile.getLocation().xCoord + offset[0] * Tile.tileLength;
		double nextY = nextTile.getLocation().yCoord + offset[1] * Tile.tileLength;
		
		double distance = Utilities.euclideanDistance(this.location, new Location(nextX, nextY)); //get distance
		
		//get difference
		double xDelta = nextX - this.location.xCoord;
		double yDelta = nextY - this.location.yCoord;
		//get Unit
		double xUnit, yUnit;
		if (distance <= 0.005) {
			xUnit = 0.0;
			yUnit = 0.0;
		} else {
			xUnit = xDelta / distance;
			yUnit = yDelta / distance;
		}
		
		direction = new double[] {xUnit, yUnit};
		
		//move the enemy
		this.location.xCoord += xUnit * displacement;
		this.location.yCoord += yUnit * displacement;
		
		//updates pathIndex if the current location of enemy is near the next tile centre (limit is arbitrary)
		//can be put somewhere else

		if(Utilities.euclideanDistance(this.location, new Location(nextX, nextY)) < 0.15) {

			this.pathIndex++;
			if(this.pathIndex == path.size()-1) { //if arrived at ending tile hit the player
				this.hitPlayer();
			}
			return;
		}
		
		if (pathIndex+2 >= path.size()) return;
		
		PathTile tile2 = path.get(pathIndex+2);
		double[] offset2 = pathOffsets.get(pathIndex+2);
		
		double nextX2 = tile2.getLocation().xCoord + offset2[0] * Tile.tileLength;
		double nextY2 = tile2.getLocation().yCoord + offset2[1] * Tile.tileLength;
		
		double distance2 = Utilities.euclideanDistance(location, new Location(nextX2, nextY2));
		
		if (distance2 < distance) {
			this.pathIndex++;
        }
	}

	private void updateGaussianNoise() {
		gaussianNoise = new double[] {0.0, 0.0};
		int signX = Utilities.globalRNG.nextBoolean() ? 1 : -1;
		int signY = Utilities.globalRNG.nextBoolean() ? 1 : -1;
		gaussianNoise[0] = signX * boundedGaussian();
		gaussianNoise[1] = signY * boundedGaussian();
	}

	private static double boundedGaussian() {
		double noise = Utilities.globalRNG.nextGaussian(GAUSSIAN_MEAN, GAUSSIAN_STANDARD_DEVIATION);
		noise = Math.max(-1, Math.min(1, noise));

		return noise / 10.0;
	}
	
	public double getHitPoints() {
		return hitPoints;
	}
	public void setHitPoint(double hp) {
		this.hitPoints = hp;
	}

	public void updateHitPoints(double damage) {
		this.hitPoints -= damage;
		hitPointsListener.invoke(this.hitPoints / this.totalHitPoints);
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location.xCoord = location.xCoord;
		this.location.yCoord = location.yCoord;
	}
	
	public static ArrayList<Enemy> getAllEnemies(){
		return enemies;
	}
	public static ArrayList<Enemy> getActiveEnemies() { return activeEnemies; }

	public void setPathIndex(int pathIndex) {
		this.pathIndex = pathIndex;
	}
	
	public int getPathIndex() {
		return pathIndex;
	}
	
	public void setPath() {
		Map map = PlayModeManager.getInstance().getCurrentMap();
		HashMap<PathTile, List<PathTile>> pathMap = map.getPathMap();
		List<java.util.Map.Entry<PathTile, List<PathTile>>> nonNullPaths = pathMap.entrySet().stream().filter(
				c -> c.getValue() != null).toList();
		int index = Utilities.globalRNG.nextInt(nonNullPaths.size());
		PathTile start = nonNullPaths.get(index).getKey();
		path = pathMap.get(start);
	}

	public void setPathOffsets() {
		pathOffsets = PlayModeManager.getInstance().getCurrentMap().getOffsetMap().get(path.getFirst());
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean value) {
		initialized = value;
		if (value) activeEnemies.add(this);
		else activeEnemies.remove(this);
	}
	
	protected static int getID() {
		return numberOfEnemies++;
	}

	public int getEnemyID() {
		return enemyID;
	}
	
	public double[] getDirection() {
		return direction;
	}

	public EnemyHitPointsListener getHitPointsListener() {
		return hitPointsListener;
	}
	
	public static void resetID() {
		numberOfEnemies = 0;
	}

	private void cleanupEnemy() {
		initialized = false;
		enemies.remove(this);
		activeEnemies.remove(this);
	}

	public static void cleanupAllEnemies() {
		for (Enemy enemy : enemies) enemy.setInitialized(false);
		activeEnemies.clear();
		enemies.clear();
	}

  	public boolean isSlowedDown() {
		return slowedDown;
	}

	public List<PathTile> getPath() {
		return path;
	}

	private static void publishEnemyRemoved() {
		for (Consumer<Boolean> consumer : removedEnemyListener) {
			consumer.accept(enemies.isEmpty());
		}
	}

	public static void addEnemyRemovedListener(Consumer<Boolean> consumer) {
		removedEnemyListener.add(consumer);
	}

	public static void removeEnemyRemovedListener(Consumer<Boolean> consumer) {
		removedEnemyListener.remove(consumer);
	}
}
