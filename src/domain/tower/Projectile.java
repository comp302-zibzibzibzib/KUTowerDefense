package domain.tower;

import java.io.Serializable;	

import domain.entities.Enemy;
import domain.map.Location;
import domain.services.Utilities;

public class Projectile implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected double damage;
	protected AttackType attacktype;
	protected Enemy target;
	protected Location location;
	
	
	public Projectile(AttackType attacktype, Enemy target, Location location) {
		super();
		this.damage = attacktype.getDamage();
		this.attacktype = attacktype;
		this.target = target;
		this.location = location;
	}
	

	public void hitTarget() {
		target.hitEnemy(damage);
	}
	
	public boolean followTarget() {
		//Boolean return: If true is returned, remove projectile else do nothing.
	    if (target == null || target.getHitPoints() <= 0) return false; //No target in range or enemy is dead, does nothing.
	    
	    //Gets the difference from target to projectile and calculates the euclidian distance between them 
	    double dx = target.getLocation().xCoord - location.xCoord; 
	    double dy = target.getLocation().yCoord - location.yCoord;
	    double distance = Utilities.euclideanDistance(location, target.getLocation());
	    
	    //Hits the enemy if the distance is below a certain threshold
	    if (distance < 0.25) { //Arbitrary threshold value 
	        hitTarget();
	        //For removal
	        return true;
	    }
	    	
	    //Calculates unit direction vectors and multiplies them with projectile speed. 
	    double speed = 1; //Arbitrary speed value (Changing values may result unwanted behaviours needs fixing)
	    double nextX = (dx / distance) * speed;
	    double nextY = (dy / distance) * speed;
	    //Updates the projectile's coodrinates in the direction of enemy
	    location.xCoord += nextX;
	    location.yCoord += nextY;
	    //Projectile is still active, does nothing
	    return false;
	}


	public Location getLocation() {
		return location;
	}
	
	public Enemy getTarget() {
		return target;
	}
	public AttackType getAttackType() {
		return attacktype;
	}
}
