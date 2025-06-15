package domain.entities;

import java.util.ArrayList;
import java.util.HashMap;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.PlayModeManager;
import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.map.Tile;
import domain.services.Utilities;

public class GoldBag {
	private static int maxGold = (int) ( (0.5 * GameOptions.getInstance().getArcherCost() ) - 1); //temp
	private static double duration = 10.0;
	private static double bagGravity = 9.81; // yes actually, no kidding, right?
	private static double bagSpeed = 3.0;
	private static int numOfBags = 0;
	public static HashMap<Integer, GoldBag> goldBags = new HashMap<Integer, GoldBag>();

	private int gold;
	private Location location;
	private double timeSinceCreation;
	private int id;

	private double initY;
	private double xVelocity;
	private double yVelocity;

	public GoldBag(Location location) {
		this.location = new Location(location.xCoord, location.yCoord);
		this.gold = Utilities.globalRNG.nextInt(maxGold) + 2; //max gold is from tower factory temporarily
		this.timeSinceCreation = 0.0;
		this.id = getBagID();
		goldBags.put(id, this);

		initY = location.yCoord;
		yVelocity = -bagSpeed;

		int direction = (Utilities.globalRNG.nextBoolean()) ? -1 : 1;
		if (location.xCoord < 20.0) direction = 1;
		
		int w = PlayModeManager.getInstance().getCurrentMap().getWidth();
		if (w * Tile.tileLength - location.xCoord < 20.0) direction = -1;

		xVelocity = 2 * direction * bagSpeed;
	}

	public void pickUpBag() { //removes bag from map adds gold to player
		Player.getInstance().updateGold(gold);
		this.removeBag();
	}

	public void removeBag() { //bag removal after timeout must be handled somewhere else
		this.location = null;
		goldBags.remove(id, this);
	}

	public static void bagUpdate(double deltaTime) {
		deltaTime = PlayModeManager.getInstance().getGameSpeed() * deltaTime;

		for(GoldBag bag : new ArrayList<>(goldBags.values())) {
			bag.timeSinceCreation += deltaTime;
			if(bag.timeSinceCreation >= duration) {
				bag.removeBag();
				continue;
			}

			// Run 2D projectile motion if above initial height
			if (bag.location.yCoord <= bag.initY) {
				bag.yVelocity += deltaTime * bagGravity;
				bag.location.yCoord += bag.yVelocity * deltaTime;
				bag.location.xCoord += bag.xVelocity * deltaTime;
			}
		}
	}

	public static void resetBags() {
		goldBags.clear();
	}

	public static HashMap<Integer, GoldBag> getGoldBags() {
		return goldBags;
	}

	public static GoldBag getGoldBag(int id) {
		return goldBags.get(id);
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