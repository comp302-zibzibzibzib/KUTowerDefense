package domain.map;

import domain.tower.ArcherTower;
import domain.tower.Tower;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class MapTest {

    static Map map;
    static MapEditor me;

    @BeforeEach
    void mapCreate(){
        // creates a valid test map
        map = new Map("Test Map", 9, 16);
        me = new MapEditor(map);
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
    void repOK() { // checks whether repOK correctly validates map
        boolean truth = me.isValidMap(); // checks whether path is correct and empty lot count is more than or equal to 4
        if (map.mapName == null || map.height < 1 || map.width < 1){ //checks whether height and width is more than 1 and mapName exits
            truth = false;
        }
        assert (truth == map.repOK());
    }

    @Test
    void placeATreeTileOnGrass() {
        Tile firstTile = map.tileMap[0][6];

        // Try place tree
        me.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 0, 6);

        Tile secondTile = map.tileMap[0][6];

        // Check if tree is there
        assertNotEquals(firstTile, secondTile);
        assertEquals(TileType.DECORATIVES, secondTile.type);
        assertInstanceOf(DecorativeTile.class, secondTile);
        assertEquals(DecorativeType.TREE1, ((DecorativeTile) secondTile).getDecorativeType());
    }

    @Test
    void tryPlaceTileOnNonEmptyTile() {
        Tile firstTile = map.tileMap[0][3];

        // Try placing different tiles on non-empty tile
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0, 3);
        me.placeTile(TileType.LOT, 0, 3);
        me.placeTile(TileType.TOWER, 0, 3);

        Tile secondTile = map.tileMap[0][3];

        // First tile should be the same
        assertEquals(firstTile, secondTile);
    }

    @Test
    void placeTowerOnLot() {
        Tile firstTile = map.tileMap[3][2];

        // Try placing an archer tower on lot
        me.placeTile(TileType.TOWER, TowerType.ARCHER, 3, 2);

        Tile secondTile = map.tileMap[3][2];

        // Check if the lot is the same
        assertEquals(firstTile, secondTile);
        // Check if the tower is there
        assertInstanceOf(Lot.class, secondTile);
        assertInstanceOf(ArcherTower.class, ((Lot)secondTile).getTower());
    }

    @Test
    void removeTower() {
        Tile firstTile = map.tileMap[8][3];

        // Demolish house
        me.removeTile(8, 3);

        Tile secondTile = map.tileMap[8][3];

        // Check if house is still there
        assertEquals(firstTile, secondTile);
        assertNull(((Lot)secondTile).getTower());
    }

    @Test
    void removeCastle() {
        // Get all times that have the castle
        Tile castleLeftUp = map.tileMap[0][2];
        Tile castleRightUp = map.tileMap[0][3];
        Tile castleLeftDown = map.tileMap[1][2];
        Tile castleRightDown = map.tileMap[1][3];

        // Try removing castle from any of the tiles
        me.removeTile(1, 2);

        Tile castleLeftUp2 = map.tileMap[0][2];
        Tile castleRightUp2 = map.tileMap[0][3];
        Tile castleLeftDown2 = map.tileMap[1][2];
        Tile castleRightDown2 = map.tileMap[1][3];

        // Castle should be gone?
        assertNotEquals(castleLeftUp, castleLeftUp2);
        assertNotEquals(castleLeftDown, castleLeftDown2);
        assertNotEquals(castleRightDown, castleRightDown2);
        assertNotEquals(castleRightUp, castleRightUp2);
    }

    @Test
    void assertPathNeighbor() {
        Tile pathDown = map.tileMap[7][4];
        Tile pathUp = map.tileMap[6][4];

        assertEquals(pathDown, ((PathTile) pathUp).getDown());
        assertEquals(pathUp, ((PathTile) pathDown).getUp());

        me.removeTile(6, 4);

        assertFalse(me.isValidMap());
        assertNull(((PathTile)pathDown).getUp());
    }
}