package domain.map;

import java.io.Serializable;

public class Tile implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final double tileLength = 5;
	
	public TileType type;
	public Location location;
	
	public Tile() {
		this.type = TileType.GRASS;
	}
	
	public Tile(Location location) {
		this.type = TileType.GRASS;
		this.location = location;
	}
	
	public Tile(TileType tileType) {
		this.type = tileType;
	}
	
	public Tile(TileType type, Location location) {
		this.type = type;
		this.location = location;
		
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
