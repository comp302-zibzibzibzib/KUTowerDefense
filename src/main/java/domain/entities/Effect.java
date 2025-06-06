package domain.entities;

import java.util.ArrayList;
import java.util.HashMap;

import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;
import domain.tower.AttackType;

public class Effect {
	private Location location;
	private final AttackType attackType;
	private final int id;
	public static HashMap<Integer, Effect> effects = new HashMap<>();
	private static int effectNum = 0;
	public Effect(Location location, AttackType attackType) {
		this.location = new Location();
		if(location != null) {
			this.location.xCoord = location.xCoord;
			this.location.yCoord = location.yCoord;
		}

		id = getInitID();
		this.attackType = attackType;
		effects.put(id, this);
	}

	public void killEffect() {
		effects.remove(this.id);
	}

	public static void killEffect(int id) {
		effects.remove(id);
	}

	public int getID() {
		return id;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public AttackType getAttackType() {
		return attackType;
	}

	private static int getInitID() {
		return effectNum++;
	}

	public static void resetEffects() {
		effects.clear();
	}

	public static HashMap<Integer, Effect> getEffects() {
		return effects;
	}
}