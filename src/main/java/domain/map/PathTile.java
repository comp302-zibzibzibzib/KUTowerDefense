package domain.map;

// Node/tile object which has a Location, used to implement path finding logic
public class PathTile extends Tile {
	private static final long serialVersionUID = 1L;
	
	// All neighbours of pathtile
	private PathTile up;
	private PathTile down;
	private PathTile right;
	private PathTile left;
	
	private PathType pathType;		// The type of path tile
	
	public PathType getPathType() {
		return pathType;
	}

	public void setPathType(PathType pathType) {
		this.pathType = pathType;
	}

	public PathTile(PathType pathType, Location location) {
		super(TileType.PATH, location);
		this.pathType = pathType;
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
