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
	protected double hitPoints;
	protected double speed;
	protected Location location;
	protected int pathIndex;
	
	/**
	 * 
	 * @param player
	 * decreases lives of player, currently hard coded to be 1
	 */
	
	protected void hitPlayer() {
		Player.getInstance().takeDamage();
		enemies.remove(this);
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
		this.location = location;
	}
	
	public static ArrayList<Enemy> getAllEnemies(){
		return enemies;
	}
	
	public void hitEnemy(double damage) {
		hitPoints -= damage;
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
	
	
}
