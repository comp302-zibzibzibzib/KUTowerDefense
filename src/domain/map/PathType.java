 package domain.map;

// Enumeration to differentiate between different shapes of path tiles
public enum PathType {
	HORIZONTAL_MIDDLE(" HM", new boolean[] {false, false, true, true}), HORIZONTAL_END_LEFT("HEL", new boolean[] {false, false, false, true}),
	HORIZONTAL_END_RIGHT("HER", new boolean[] {false, false, true, false}), VERTICAL_MIDDLE(" VM", new boolean[] {true, true, false, false}), 
	VERTICAL_END_TOP("VET", new boolean[] {false, true, false, false}), VERTICAL_END_BOTTOM("VEB", new boolean[] {true, false, false, false}),
	RIGHT(" RT", new boolean[] {true, true, false, false}), LEFT(" LT", new boolean[] {true, true, false, false}),
	TOP(" TP", new boolean[] {false, false, true, true}), BOTTOM(" BT", new boolean[] {false, false, true, true}),
	BOTTOMRIGHT("BTR", new boolean[] {true, false, true, false}), BOTTOMLEFT("BTL", new boolean[] {true, false, false, true}),
	TOPLEFT("TPL", new boolean[] {false, true, false, true}), TOPRIGHT("TPR", new boolean[] {false, true, true, false});
	
	private String str;
	private boolean[] neighbourBool; // {up, down, left, right}
	private String assetName;

	
	@Override
	public String toString() { return this.str; }
	
	public boolean neighbourExists(int index) {
		if (index > 3) return false;
		return neighbourBool[index];
	}
	
	private PathType(String str, boolean[] neighbourBool, String assetName) {
		this.str = str;
		this.neighbourBool = neighbourBool;
		this.assetName = assetName;
	}
	
	public String getAssetName() {
		return assetName;
	}
}
