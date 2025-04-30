package domain.entities;

import java.util.ArrayList;
import java.util.List;

import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.PathTile;
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
	
	/**
	 * 
	 * @param player
	 * decreases lives of player, currently hard coded to be 1
	 */
	
	protected void hitPlayer() {
		Player.getInstance().takeDamage();
		enemies.remove(this);
	}
	
	public void killEnemy() { //VALUE IS RANDOM FOR NOW, MUST BE ABLE TO CHANGE IN OPTIONS
		enemies.remove(this);
		Player.getInstance().setGold(Player.getInstance().getGold() + 25);
	}
	
	public void moveEnemy(long deltaTime) {
		double deltaSecond = deltaTime/1_000_000_000.0; //if causing problems can be removed
		double displacement = (this.speed * PlayModeManager.getInstance().getGameSpeed())*deltaSecond; //get displacement
		
		Location nextTileLoc = path.get(pathIndex+1).getLocation(); //get the location of next tile's centre
		double distance = Utilities.euclideanDistance(this.location, nextTileLoc); //get distance
		
		//get difference
		double xDelta = nextTileLoc.xCoord - this.location.xCoord;
		double yDelta = nextTileLoc.yCoord - this.location.yCoord;
		//get Unit
		double xUnit = xDelta / distance;
		double yUnit = yDelta / distance;
		//move the enemy
		this.location.xCoord += xUnit * displacement;
		this.location.yCoord += yUnit * displacement;
		
		double distY = nextTileLoc.xCoord - location.xCoord;
		double distX = nextTileLoc.yCoord - location.yCoord;
		
		boolean overshoot = Math.signum(distX) * previousXSign < 0 || Math.signum(distY) * previousYSign < 0;
		
		previousXSign = (int) Math.signum(distX);
		previousYSign = (int) Math.signum(distY);
		
		//updates pathIndex if the current location of enemy is near the next tile centre (limit is arbitrary)
		//can be put somewhere else
		if(Utilities.euclideanDistance(this.location, nextTileLoc) < 0.025 ) {
			this.pathIndex++;
			if(this.pathIndex == path.size()-1) { //if arrived at ending tile hit the player
				this.hitPlayer();
			}
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
