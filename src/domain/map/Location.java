package domain.map;

import java.io.Serializable;

public class Location implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public double xCoord;
	public double yCoord;
	private static final double LOCATION_ERROR_MARGIN = 0.001;
	
	public Location(double xCoord, double yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	public double getXCoord() {
		return xCoord;
	}
	
	public double getYCoord() {
		return yCoord;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Location)) return false;
		
		Location other = (Location) o;
		if (Math.abs(this.xCoord - other.xCoord) < LOCATION_ERROR_MARGIN && Math.abs(this.yCoord - other.yCoord) < LOCATION_ERROR_MARGIN)
			return true;
		
		return false;
	}
}	
