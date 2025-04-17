package domain.map;

// Node object which has a Location, used to implement path finding logic
public class PathTile extends Tile {
	private static final long serialVersionUID = 1L;
	
	private PathTile up;
	private PathTile down;
	private PathTile right;
	private PathTile left;
	
	private PathType pathType;
	
	public PathTile(Location location, PathTile up, PathTile down, PathTile right, PathTile left) {
		super(TileType.PATH, location);
		this.up = up; this.down = down; this.right = right; this.left = left;
	}
	
	public PathTile(Location location) {
		super(TileType.PATH, location);
	}

	public PathTile getUp() {
		return up;
	}

	public void setUp(PathTile up) {
		this.up = up;
	}

	public PathTile getDown() {
		return down;
	}

	public void setDown(PathTile down) {
		this.down = down;
	}

	public PathTile getRight() {
		return right;
	}

	public void setRight(PathTile right) {
		this.right = right;
	}

	public PathTile getLeft() {
		return left;
	}

	public void setLeft(PathTile left) {
		this.left = left;
	}
}
