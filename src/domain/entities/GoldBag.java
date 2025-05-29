package domain.entities;

import java.util.HashMap;

import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.services.Utilities;
import domain.tower.TowerFactory;

public class GoldBag {
	private int gold;
	private Location location;
	private double timeSinceCreation;
	private int id;
	
	private static int maxGold = (int) ((0.5*TowerFactory.costArcher) - 1); //temp
	private static double duration = 10.0;
	private static int numOfBags = 0;
	public static HashMap<Integer, GoldBag> goldBags = new HashMap<Integer, GoldBag>();
	
	public GoldBag(Location location) {
		this.location = new Location(location.xCoord, location.yCoord);
		this.gold = Utilities.globalRNG.nextInt(maxGold) + 2; //max gold is from tower factory temporarily
		this.timeSinceCreation = 0.0;
		this.id = getBagID();
		goldBags.put(id, this);
	}
	
	public void pickUpBag() { //removes bag from map adds gold to player
		Player.getInstance().updateGold(gold);
		this.removeBag();
	}
	
	public void removeBag() { //bag removal after timeout must be handled somewhere else
		this.location = null;
		goldBags.remove(id, this);
	}
	
	public static void bagUpdate(double DeltaTime) {
		for(GoldBag bag : goldBags.values()) {
			bag.timeSinceCreation += DeltaTime;
			if(bag.timeSinceCreation >= duration) {
				bag.removeBag();
			}
		}
	}
	
	
	private int getBagID() {
		return numOfBags++;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getTimeSinceCreation() {
		return timeSinceCreation;
	}

	public void setTimeSinceCreation(double timeSinceCreation) {
		this.timeSinceCreation = timeSinceCreation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
