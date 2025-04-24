package domain.map;

import java.io.Serializable;

import domain.tower.Tower;

public class Lot extends Tile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public boolean isEmpty = true;
	public Tower tower;
	//IDKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
	public Lot(Location location) {
		super(TileType.LOT, location);
	}
	//Might need additional interactions with map or tile class
	public void placeTower(Tower tower) {
        if (!isEmpty) {
            System.out.println("Lot is not empty!");
            return;
        }
        this.setType(TileType.TOWER);
        isEmpty = false;
    }

    public void removeTower() {
        if (this.getType() != TileType.TOWER) {
            System.out.println("No tower to remove!");
            return;
        }
        this.setType(TileType.LOT);
        isEmpty = true;
    }
}

	
