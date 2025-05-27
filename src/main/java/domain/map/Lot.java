package domain.map;

import java.io.Serializable;
import domain.tower.Tower;

public class Lot extends Tile implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isEmpty = true;
    private Tower tower;
    private TowerType towerType;

    public Lot(Location location) {
        super(TileType.LOT, location);
    }

    public void placeTower(Tower tower,TowerType towerType) {
        if (!isEmpty) {
            System.out.println("Lot is not empty!");
            return;
        }
        this.setType(TileType.TOWER);
        this.setTowerType(towerType);
        this.tower = tower;
        this.isEmpty = false;
    }

    public void removeTower() {
        if (tower == null) {
            System.out.println("No tower to remove!");
            return;
        }
        this.setType(TileType.LOT);
        this.setTowerType(null);
        this.tower = null;
        this.isEmpty = true;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Tower getTower() {
        return tower;
    }

    public boolean hasTower() {
        return !isEmpty && tower != null;
    }

	public TowerType getTowerType() {
		return towerType;
	}

	public void setTowerType(TowerType towerType) {
		this.towerType = towerType;
	}
    
    
}
