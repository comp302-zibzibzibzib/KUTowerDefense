package domain.entities;

import java.util.ArrayList;
import java.util.List;

import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.PathTile;
import domain.map.Tile;
import domain.services.Utilities;

public abstract class Enemy {
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static List<PathTile> path = new ArrayList<PathTile>();
	private static int numberOfEnemies = 0; // Not active enemy number just the total amount created during runtime
	protected double hitPoints;
	protected double speed;
	protected Location location;
	protected int pathIndex;
	protected int enemyID;
	
	private boolean initialized = false;
	private int previousXSign;
	private int previousYSign;
	private int previousPathIndex;
	
	/**
	 * 
	 * @param player
	 * decreases lives of player, currently hard coded to be 1
	 */
	
	protected void hitPlayer() {
		Player.getInstance().takeDamage();
		initialized = false;
		enemies.remove(this);
	}
	
	public void killEnemy() { //VALUE IS RANDOM FOR NOW, MUST BE ABLE TO CHANGE IN OPTIONS
		enemies.remove(this);
		Player.getInstance().setGold(Player.getInstance().getGold() + 25);
	}
	
	public void moveEnemy(long deltaTime) {
		if(PlayModeManager.getInstance().getGameSpeed() == 0) return;
		
		double deltaSecond = deltaTime/1_000_000_000.0; //if causing problems can be removed
		double displacement = (this.speed * PlayModeManager.getInstance().getGameSpeed()) * deltaSecond; //get displacement
		
		PathTile nextTile = path.get(pathIndex+1); //get the location of next tile's centre
		
		double[] tileOffset = null;
		if (pathIndex+1 == path.size()-1) 
			tileOffset = new double[] {0.0, -0.7}; // Enemy has to move further up if next is end tile
		else tileOffset = nextTile.getPathType().getPathOffsetPercentage();
		
		double nextX = nextTile.getLocation().xCoord + tileOffset[0] * Tile.tileLength;
		double nextY = nextTile.getLocation().yCoord + tileOffset[1] * Tile.tileLength;
		
		double distance = Utilities.euclideanDistance(this.location, new Location(nextX, nextY)); //get distance
		
		//get difference
		double xDelta = nextX - this.location.xCoord;
		double yDelta = nextY - this.location.yCoord;
		//get Unit
		double xUnit = xDelta / distance;
		double yUnit = yDelta / distance;
		
		//move the enemy
		this.location.xCoord += xUnit * displacement;
		this.location.yCoord += yUnit * displacement;
		
		//updates pathIndex if the current location of enemy is near the next tile centre (limit is arbitrary)
		//can be put somewhere else

		this.previousPathIndex = pathIndex;
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
			return;
		}
	}
	
	public double getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(double hitPoints) {
		this.hitPoints = hitPoints;
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
	
	public void hitEnemy(double damage) {
		hitPoints -= damage;
		if(hitPoints <= 0) {
			this.killEnemy();
		}
	}
	
	public void setPathIndex(int pathIndex) {
		this.pathIndex = pathIndex;
	}
	
	public int getPathIndex() {
		return pathIndex;
	}
	
	public static void setPath() {
		path = PlayModeManager.getInstance().getCurrentMap().getPath();
	}
	
	public void initialize(Location location) {
		setLocation(location);
		initialized = true;
		setPath();
	}
	
	public boolean isInitalized() {
		return initialized;
	}
	
	protected static int getID() {
		return numberOfEnemies++;
	}
	
	public int getEnemyID() {
		return enemyID;
	}
}
