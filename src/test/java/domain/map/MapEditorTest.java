package domain.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapEditorTest {
    //Haydar Arda Subasi
    //Tests placeTile(TileType type, int height, int width) which is used to place Lot Tiles and Castle Tiles.

    static Map map;
    static MapEditor me;

    @BeforeEach
    void createTestMap(){ //Creates a test map and the editor for the map
        map = new Map("placeTileTestMap",6 ,6);
        me = new MapEditor(map);
    }

    @Test
    void placeCastleTileTest_InvalidLocation() { //Haydar Arda Subasi
        //Try place castle on invalid position
        Tile beforeTile1 = map.tileMap[0][5];
        Tile beforeTile2 = map.tileMap[1][5];
        //top tiles of the castle are outside the map
        me.placeTile(TileType.CASTLE, 0, 5);


        Tile afterTile1 = map.tileMap[0][5];
        Tile afterTile2 = map.tileMap[1][5];

        //Tiles should not be replaced/ should be equal
        assertEquals(beforeTile1, afterTile1);
        assertEquals(beforeTile2, afterTile2);


    }

    @Test
    void placeCastleTileTest_OccupiedSpace(){ //Haydar Arda Subasi
        //try to place Castle on occupied space
        me.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 0 , 0);

        Tile beforeTile1 = map.tileMap[0][0];
        Tile beforeTile2 = map.tileMap[0][1];
        Tile beforeTile3 = map.tileMap[1][0];
        Tile beforeTile4 = map.tileMap[1][1];

        me.placeTile(TileType.CASTLE, 0, 0);


        Tile afterTile1 = map.tileMap[0][0];
        Tile afterTile2 = map.tileMap[0][1];
        Tile afterTile3 = map.tileMap[1][0];
        Tile afterTile4 = map.tileMap[1][1];

        //Tiles should not be replaced/ should be equal
        assertEquals(beforeTile1, afterTile1);
        assertEquals(beforeTile2, afterTile2);
        assertEquals(beforeTile3, afterTile3);
        assertEquals(beforeTile4, afterTile4);

        //castle should not be placed if one of tiles in 2x2 area is occupied
        me.removeTile(0,0);
        me.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 1 , 0);
        me.placeTile(TileType.DECORATIVES, DecorativeType.TREE1, 0, 1);

        beforeTile1 = map.tileMap[0][0];
        beforeTile2 = map.tileMap[0][1];
        beforeTile3 = map.tileMap[1][0];
        beforeTile4 = map.tileMap[1][1];

        me.placeTile(TileType.CASTLE, 0, 0);


        afterTile1 = map.tileMap[0][0];
        afterTile2 = map.tileMap[0][1];
        afterTile3 = map.tileMap[1][0];
        afterTile4 = map.tileMap[1][1];

        //Tiles should not be replaced/ should be equal
        assertEquals(beforeTile1, afterTile1);
        assertEquals(beforeTile2, afterTile2);
        assertEquals(beforeTile3, afterTile3);
        assertEquals(beforeTile4, afterTile4);


    }

    @Test
    void placeCastleTileTest_valid() { //Haydar Arda Subasi
        //Try place caste on valid position
        Tile beforeTile1 = map.tileMap[0][0];
        Tile beforeTile2 = map.tileMap[0][1];
        Tile beforeTile3 = map.tileMap[1][0];
        Tile beforeTile4 = map.tileMap[1][1];

        me.placeTile(TileType.CASTLE, 0, 0);


        Tile afterTile1 = map.tileMap[0][0];
        Tile afterTile2 = map.tileMap[0][1];
        Tile afterTile3 = map.tileMap[1][0];
        Tile afterTile4 = map.tileMap[1][1];

        //Tiles should be replaced/ should not be equal
        assertNotEquals(beforeTile1, afterTile1);
        assertNotEquals(beforeTile2, afterTile2);
        assertNotEquals(beforeTile3, afterTile3);
        assertNotEquals(beforeTile4, afterTile4);
        //extra check if tile type is CASTLE
        assertEquals(TileType.CASTLE, afterTile1.getType());
        assertEquals(TileType.CASTLE, afterTile2.getType());
        assertEquals(TileType.CASTLE, afterTile2.getType());
        assertEquals(TileType.CASTLE, afterTile2.getType());
    }

    @Test
    void placeLotTileTest(){ //Haydar Arda Subasi
        Tile beforeTile1 = map.tileMap[0][0];
        me.placeTile(TileType.LOT,0,0);
        Tile afterTile1 = map.tileMap[0][0];

        assertNotEquals(beforeTile1, afterTile1);
        assertEquals(1, me.map.getLotCount());
        assertEquals(1, map.getLotCount());
    }

    @Test
    void placeInvalidTileTypeTest(){ //Haydar Arda Subasi
        //tries to place invalid tileTypes (other placeTiles functions must be called)

        //try to place tower type
        Tile beforeTile1 = map.tileMap[0][0];
        me.placeTile(TileType.TOWER,0,0);
        Tile afterTile1 = map.tileMap[0][0];

        assertEquals(beforeTile1, afterTile1);

        //try to place decorative type
        beforeTile1 = map.tileMap[0][0];
        me.placeTile(TileType.DECORATIVES,0,0);
        afterTile1 = map.tileMap[0][0];

        assertEquals(beforeTile1, afterTile1);

        //try to place path type
        beforeTile1 = map.tileMap[0][0];
        me.placeTile(TileType.PATH,0,0);
        afterTile1 = map.tileMap[0][0];

        assertEquals(beforeTile1, afterTile1);

    }


}