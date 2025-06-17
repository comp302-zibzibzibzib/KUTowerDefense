package domain.map;

public enum TileType {
	GRASS(" G "), PATH(" P "), TOWER(" T "), DECORATIVES(" D "), CASTLE(" C "), LOT(" L "), INVALID(" ? ");
	
	public String str;
	
	@Override
	public String toString() { return this.str; }
	
	private TileType(String str) {
		this.str = str;
	}
}
