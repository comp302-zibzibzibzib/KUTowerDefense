package domain.entities;

import java.util.List;

import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.Tile;

public abstract class Enemy {
	protected float hitPoints;
	protected float speed;
	protected Location location;
	
	/**
	 * 
	 * @param player
	 * decreases lives of player, currently hard coded to be 1
	 */
	
	protected void hitPlayer(Player player) {//ALL TEMP MIGHT CHANGE
		player.setHealth(player.getHealth()-1);
	}
	
	protected void findPath(List<List<Tile>> tileMap) { //stub from UML Diagram will be implemented/changed or removed
	}

	public float getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(float hitPoints) {
		this.hitPoints = hitPoints;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	

}
