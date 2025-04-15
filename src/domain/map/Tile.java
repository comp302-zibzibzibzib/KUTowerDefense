package domain.map;

public class Tile {
	public TileType type;
	public Boolean isWalkable;
	public Location location;
	
	public Tile() {
		this.type = TileType.GRASS;
		this.isWalkable = false;
	}
	
	public Tile(TileType type) {
		this.type = TileType.INVALID;
	}
	
	public Tile(TileType type, Boolean isWalkable, Location location) {
		this.type = type;
		this.isWalkable = isWalkable;
		this.location = location;
		
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
	public Boolean getIsWalkable() {
		return isWalkable;
	}
	public void setIsWalkable(Boolean isWalkable) {
		this.isWalkable = isWalkable;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
