package domain.map;

import java.io.Serializable;

public class Location implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public double xCoord;
	public double yCoord;
	public Location(double xCoord, double yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
}	
