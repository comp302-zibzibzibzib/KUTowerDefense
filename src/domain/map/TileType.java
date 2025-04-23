package domain.map;

public enum TileType {
	GRASS(" G "), PATH(" P "), OBSTACLES(" O "), TOWER(" T ");
	
	public String str;
	
	@Override
	public String toString() { return this.str; }
	
	private TileType(String str) {
		this.str = str;
	}
}
