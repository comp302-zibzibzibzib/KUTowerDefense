package domain.map;

public enum TileType {
	GRASS(" G "), PATH(" P "), OBSTACLES(" O "), STRAIGHT(" S "),
	UPWARDS(" U "), RIGHT(" R "), LEFT(" L "), TOP(" T "), BOTTOM(" B "), 
	BOTTOMRIGHT("BR "),BOTTOMLEFT("BL "), TOPLEFT("TL "), TOPRIGHT("TR "), INVALID(" ? ");
	
	public String str;
	
	@Override
	public String toString() { return this.str; }
	
	private TileType(String str) {
		this.str = str;
	}
}
