package domain.map;

public enum DecorativeType {
	HOUSE1("house"),HOUSE2("house2"), WELL("well"), ROCK1("rock1"), ROCK2("rock2"),TREE1("tree1"),TREE2("tree2"), TREE3("tree3"), WOOD("wood");
	
	private String assetName;
	
	private DecorativeType(String assetName) {
		this.assetName = assetName;
	}
	
	public String getAssetName() {
		return assetName;
	}
}
