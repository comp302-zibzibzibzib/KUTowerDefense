package domain.map;

import domain.tower.Tower;

public class Lot {
	private Tower tower;
	private Tile tile;
	private boolean isEmpty;
	
	public Lot(Tile tile) {
		this.tile = tile;
		this.isEmpty = true;
	}
	
	public Lot(Tile tile, Tower tower) {
		this.tile = tile;
		this.tower = tower;
		this.isEmpty = false;
	}
	
	public void placeTower(Tower tower) {
		if (this.tower != null) return;
		this.tower = tower;
		this.isEmpty = false;
		tower.setLocation(tile.getLocation());
	}
	
	public void removeTower() {
		this.tower.setLocation(null);
		this.tower = null;
		this.isEmpty = true;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public void setTile(Tile tile) {
		this.tile = tile;
	}
}
