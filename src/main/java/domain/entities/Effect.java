package domain.entities;

import java.util.ArrayList;

import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;

public class Effect {
	private Location location;
	private double timeSinceCreation = 0.0;
	public static ArrayList<Effect> effects = new ArrayList<>();
	public Effect(Location location) {
		if(location != null) {
			this.location.xCoord = location.xCoord;
			this.location.yCoord = location.yCoord;
		}
		effects.add(this);
	
	}
	
	public void updateEffect(double dt) {
		if(PlayModeManager.getInstance().getGameSpeed() == 0) return;
		timeSinceCreation += dt;
		if (timeSinceCreation >= 1.0) {
			effects.remove(this);
		}
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public static ArrayList<Effect> getEffects() {
		return effects;
	}
	public static void setEffects(ArrayList<Effect> effects) {
		Effect.effects = effects;
	}
}