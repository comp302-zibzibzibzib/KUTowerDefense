package domain.entities;

import java.util.ArrayList;
import java.util.List;

import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.PathTile;
import domain.services.Utilities;

public abstract class Enemy {
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	protected double hitPoints;
	protected double speed;
	protected Location location;
	protected int pathIndex;
	
	/**
	 * 
	 * @param player
	 * decreases lives of player, currently hard coded to be 1
	 */
	
	protected void hitPlayer(Player player) {
		//player.setHealth(player.getHealth()-1);
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
	
	public void hitEnemy(double  damage) {
		hitPoints -= damage;
	}
	public int getPathIndex() {
		return pathIndex;
	}
	
}
