package domain.map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class MapTest {

    static Map map = new Map("Pre-Built Map", 9, 16);
    static MapEditor me = new MapEditor(map);

    @BeforeEach
    void mapCreate(){ //creates a valid prebuilt map copied from services/Test.java file
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0,4);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1,4);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 2,4);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 3,4);
        me.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 4,4);
        me.placeTile(TileType.PATH, PathType.TOPRIGHT, 4,5);
        me.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,5,5);
        me.placeTile(TileType.PATH, PathType.TOPLEFT, 5,4);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,6,4);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,7,4);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 8,4);
        me.placeTile(TileType.CASTLE, 0, 2);
        me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 0, 0);
        me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 0, 15);
        me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 8, 0);
        me.placeTile(TileType.DECORATIVES,DecorativeType.TREE1, 8, 15);
        me.placeTile(TileType.LOT, 3, 2);
        me.placeTile(TileType.LOT, 5, 2);
        me.placeTile(TileType.LOT, 3, 6);
        me.placeTile(TileType.LOT, 8, 5);
        me.placeTile(TileType.TOWER, TowerType.ARCHER, 0,5);
        me.placeTile(TileType.TOWER, TowerType.MAGE, 8,3);
        me.placeTile(TileType.DECORATIVES,DecorativeType.HOUSE1, 5, 14);
        me.placeTile(TileType.DECORATIVES,DecorativeType.HOUSE2, 7, 13);
        me.placeTile(TileType.DECORATIVES,DecorativeType.WELL, 7, 10);
    }

    @Test
    void repOK() { //checks whether repOK correctly validates map

        boolean truth = me.isValidMap(); //checks whether path is correct and empty lot count is more than or equal to 4
        if (map.mapName == null || map.height < 1 || map.width < 1){ //checks whether height and width is more than 1 and mapName exits
            truth = false;
        }
        assert (truth == map.repOK());
    }
}