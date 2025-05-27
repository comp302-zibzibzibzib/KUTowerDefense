 package domain.map;

// Enumeration to differentiate between different shapes of path tiles
public enum PathType {
	HORIZONTAL_MIDDLE(" HM", new boolean[] {false, false, true, true}, "horizontalmiddle" ,new double[] {0, -0.15}),
	HORIZONTAL_END_LEFT("HEL", new boolean[] {false, false, false, true}, "horizontalendleft"),
	HORIZONTAL_END_RIGHT("HER", new boolean[] {false, false, true, false},"horizontalendright"),
	VERTICAL_MIDDLE(" VM", new boolean[] {true, true, false, false}, "verticalmiddle"), 
	VERTICAL_END_TOP("VET", new boolean[] {false, true, false, false}, "verticalendtop"),
	VERTICAL_END_BOTTOM("VEB", new boolean[] {true, false, false, false}, "verticalendbottom"),
	RIGHT(" RT ", new boolean[] {true, true, false, false}, "right"),
	LEFT(" LT ", new boolean[] {true, true, false, false},"left"),
	TOP(" TP ", new boolean[] {false, false, true, true},"top"),
	BOTTOM(" BT ", new boolean[] {false, false, true, true},"bottom"),
	BOTTOMRIGHT("BTR", new boolean[] {true, false, true, false},"bottomright", new double[] {-0.20, -0.20}),
	BOTTOMLEFT("BTL", new boolean[] {true, false, false, true},"bottomleft", new double[] {0.20, -0.20}),
	TOPLEFT("TPL", new boolean[] {false, true, false, true},"topleft", new double[] {0.20, 0.20}),
	TOPRIGHT("TPR", new boolean[] {false, true, true, false},"topright", new double[] {-0.20, 0.20});
	
	private String str;
	private boolean[] neighbourBool; // {up, down, left, right}
	private String assetName;
	private double[] pathOffsetPercentage;

	
	@Override
	public String toString() { return this.str; }
	
	public boolean neighbourExists(int index) {
		if (index > 3) return false;
		return neighbourBool[index];
	}
	
	public double[] getPathOffsetPercentage() {
		return pathOffsetPercentage;
	}
	
	private PathType(String str, boolean[] neighbourBool, String assetName, double[] pathOffsetPercentage) {
		this.str = str;
		this.neighbourBool = neighbourBool;
		this.assetName = assetName;
		this.pathOffsetPercentage = pathOffsetPercentage;
	}
	
	private PathType(String str, boolean[] neighbourBool, String assetName) {
		this.str = str;
		this.neighbourBool = neighbourBool;
		this.assetName = assetName;
		this.pathOffsetPercentage = new double[] {0.0, 0.0};
	}
	
	public String getAssetName() {
		return assetName;
	}
}
