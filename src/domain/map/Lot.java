package domain.map;

import java.io.Serializable;

import domain.tower.Tower;

public class Lot extends Tile {	
	public boolean isEmpty = true;
	public Tower tower;

	public Lot(Location location) {
		super(TileType.LOT, location);
	}

	public void placeTower(Tower tower) {
        if (!isEmpty) {
            System.out.println("Lot is not empty!");
            return;
        }
        this.setType(TileType.TOWER);
        this.tower = tower;
        isEmpty = false;
    }

    public void removeTower() {
        if (this.getType() != TileType.TOWER) {
            System.out.println("No tower to remove!");
            return;
        }
        this.setType(TileType.LOT);
        this.tower = null;
        isEmpty = true;
    }
}

	
