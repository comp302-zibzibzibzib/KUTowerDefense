package domain.entities;

import java.util.Random;

import domain.kutowerdefense.Player;
import domain.map.Location;
import domain.tower.TowerFactory;

public class GoldBag {
	
	private int gold;
	private Location location;
	public static Random gen = new Random();
	private static int maxGold = (int) ((0.5*TowerFactory.costArcher) - 1); //temp
	//need way to store when it was created to make it despawn when not picked up
	
	public GoldBag(Location location) {
		this.location = location;
		this.gold = gen.nextInt(maxGold) + 2; //max gold is from tower factory temporarily
	}
	
	public void pickUpBag() { //removes bag from map adds gold to player
		this.location = null;
		Player.getInstance().updateGold(gold);
	}
	
	public void removeBag() { //bag removal after timeout can be handled somewhere else
		this.location = null;
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

}
