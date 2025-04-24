package domain.map;

// Enumeration to differentiate between different shapes of path tiles
public enum PathType {
	HORIZONTAL_MIDDLE(" HM"), HORIZONTAL_END_LEFT("HEL"), HORIZONTAL_END_RIGHT("HER"), 
	VERTICAL_MIDDLE(" VM"), VERTICAL_END_TOP("VET"), VERTICAL_END_BOTTOM("VEB"),
	RIGHT(" R "), LEFT(" L "), TOP(" T "), BOTTOM(" B "), BOTTOMRIGHT("BTR"), BOTTOMLEFT("BTL"), TOPLEFT("TPL"), TOPRIGHT("TPR");
	
	public String str;
	
	@Override
	public String toString() { return this.str; }
	
	private PathType(String str) {
		this.str = str;
	}
}
