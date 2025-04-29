package domain.map;

public class DecorativeTile extends Tile {
	private static final long serialVersionUID = 1L;

	private DecorativeType decorativeType;

	public DecorativeTile(DecorativeType decorativeType, Location location) {
		super(TileType.DECORATIVES, location);
		this.decorativeType = decorativeType;
		
	}

	public DecorativeType getDecorativeType() {
		return decorativeType;
	}

	public void setDecorativeType(DecorativeType decorativeType) {
		this.decorativeType = decorativeType;
	}

}
