package domain.entities;

import java.util.*;

import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.PathTile;
import domain.map.Tile;
import domain.services.Utilities;
import domain.tower.AttackType;
import domain.tower.Projectile;

public abstract class Enemy {
	private static final Random random = new Random();

	public static ArrayList<Enemy> enemies = new ArrayList<>();
	public static ArrayList<Enemy> activeEnemies = new ArrayList<>();
	public static List<PathTile> path = new ArrayList<PathTile>();
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

	public void initialize(Location location) {
		setLocation(location);
		initialized = true;
		activeEnemies.add(this);
		setPath();
	}
	
	protected void hitPlayer() {
		Player.getInstance().takeDamage();
		initialized = false;
		cleanupEnemy();
	}

	public void hitEnemy(double damage, AttackType attackType) {
		if (attackType == AttackType.SLOW_SPELL) slowDown();
		if ((attackType == AttackType.SPELL || attackType == AttackType.SLOW_SPELL) && random.nextDouble() <= 0.03) {
			resetPosition();
			return;
		}
		updateHitPoints(damage);
		if(hitPoints <= 0 && initialized) {
			this.killEnemy();
		}
	}
	
	public void killEnemy() { //VALUE IS RANDOM FOR NOW, MUST BE ABLE TO CHANGE IN OPTIONS
		if (!initialized) return;
		cleanupEnemy();
		Player.getInstance().setGold(Player.getInstance().getGold() + 25);
	}
	
	//need some way to store when the enemy got slowed down
	public void slowDown() { //slow down by 20% when hit by lvl2 mage tower
		timeSinceSlowedDown = 0;
		if(!slowedDown) {
			speed *= 0.8;
			slowedDown = true;
		}
	}
	
	public void endSlowDown() { //return to normal speed
		timeSinceSlowedDown = 0;
		speed = defaultSpeed;
		slowedDown = false;
	}
	
	//3% chance to reset back to start when hit by mage tower, can be put somewhere else
	public void resetPosition() { 
		Location startLocation = PlayModeManager.getInstance().getCurrentMap().getStartingTile().getLocation();
		double startX = startLocation.xCoord;
		double startY = startLocation.yCoord + 1.0 * Tile.tileLength;
        this.location = new Location(startX, startY);
		this.pathIndex = 0;

		Projectile.killProjectiles(this);
	}
	
	public void updateEnemy(long deltaTime) {
		if(PlayModeManager.getInstance().getGameSpeed() == 0) return;
		
		double deltaSecond = deltaTime/1_000_000_000.0; //if causing problems can be removed

		// Handle slowed down logic
		if (slowedDown) timeSinceSlowedDown += deltaSecond;
		if (timeSinceSlowedDown >= 4.0) {
			endSlowDown();
		}

		double displacement = (this.speed * PlayModeManager.getInstance().getGameSpeed()) * deltaSecond; //get displacement
		
		PathTile nextTile = path.get(pathIndex+1); //get the location of next tile's centre
		
		double[] tileOffset = null;
		if (pathIndex+1 == path.size()-1) 
			tileOffset = new double[] {0.0, -1}; // Enemy has to move further up if next is end tile
		else tileOffset = nextTile.getPathType().getPathOffsetPercentage();
		
		double nextX = nextTile.getLocation().xCoord + tileOffset[0] * Tile.tileLength;
		double nextY = nextTile.getLocation().yCoord + tileOffset[1] * Tile.tileLength;
		
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
		double[] offset2 = tile2.getPathType().getPathOffsetPercentage();
		
		double nextX2 = tile2.getLocation().xCoord + offset2[0] * Tile.tileLength;
		double nextY2 = tile2.getLocation().yCoord + offset2[1] * Tile.tileLength;
		
		double distance2 = Utilities.euclideanDistance(location, new Location(nextX2, nextY2));
		
		if (distance2 < distance) {
			this.pathIndex++;
        }
	}
	
	public double getHitPoints() {
		return hitPoints;
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
	
	public static void setPath() {
		path = PlayModeManager.getInstance().getCurrentMap().getPath();
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
}
