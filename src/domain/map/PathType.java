package domain.map;

// Enumeration to differentiate between different shapes of path tiles
public enum PathType {
	HORIZONTAL_MIDDLE, HORIZONTAL_END_LEFT, HORIZONTAL_END_RIGHT, 
	VERTICAL_MIDDLE, VERTICAL_END_LEFT, VERTICAL_END_RIGHT,
	RIGHT, LEFT, TOP, BOTTOM, BOTTOMRIGHT, BOTTOMLEFT, TOPLEFT, TOPRIGHT
}
