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
	
	public void updateEffect(long dt) {
		double deltaSecond = dt/1_000_000_000.0; 
		if(PlayModeManager.getInstance().getGameSpeed() == 0) return;
		timeSinceCreation += deltaSecond;
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