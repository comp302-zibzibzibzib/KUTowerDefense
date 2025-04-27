package domain.map;

public enum TowerType {
	ARCHER("archertower"), MAGE("magetower"), ARTILLERY("artillerytower");
	
	private String assetName;
	
	private TowerType(String assetName) {
		this.setAssetName(assetName);
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
}
