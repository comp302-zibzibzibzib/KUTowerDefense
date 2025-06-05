package domain.services;

import domain.map.*;
import jdk.jshell.execution.Util;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilitiesTest {
    @Test
    public void findingPathForvalidMapOne(){
        Map map1 = new Map("map1", 9, 16);
        MapEditor mapeditor1 = new MapEditor(map1);
        mapeditor1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,8,1);
        mapeditor1.placeTile(TileType.PATH, PathType.TOPLEFT, 7,1);
        mapeditor1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,7,2);
        mapeditor1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,7,3);
        mapeditor1.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,7,4);
        mapeditor1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,6,4);
        mapeditor1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,5,4);
        mapeditor1.placeTile(TileType.PATH, PathType.TOPLEFT,4,4);
        mapeditor1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,4,5);
        mapeditor1.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,4,6);
        mapeditor1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,3,6);
        mapeditor1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,2,6);
        mapeditor1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,1,6);
        mapeditor1.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,0,6);

        mapeditor1.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 8,2);
        mapeditor1.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 5 , 9);

        List<PathType> validPath = Arrays.asList(PathType.VERTICAL_MIDDLE,PathType.TOPLEFT
                ,PathType.HORIZONTAL_MIDDLE,PathType.HORIZONTAL_MIDDLE,PathType.BOTTOMRIGHT,PathType.VERTICAL_MIDDLE,PathType.VERTICAL_MIDDLE,PathType.TOPLEFT
                ,PathType.HORIZONTAL_MIDDLE,PathType.BOTTOMRIGHT,PathType.VERTICAL_MIDDLE,PathType.VERTICAL_MIDDLE,PathType.VERTICAL_MIDDLE,PathType.VERTICAL_MIDDLE);

        List<PathTile> isValid = Utilities.findPath(map1);

        for(int i = 0; i< validPath.size(); i++){
            assertEquals(validPath.get(i),isValid.get(i).getPathType());
        }

    }

    @Test
    public void findingPathForInvalidMap(){
        Map map2 = new Map("map2", 9, 16);
        MapEditor mapeditor2 = new MapEditor(map2);
        mapeditor2.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,8,1);
        mapeditor2.placeTile(TileType.PATH, PathType.TOPLEFT, 7,1);
        mapeditor2.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,7,2);
        mapeditor2.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE,7,3);
        mapeditor2.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,7,4);
        mapeditor2.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,6,4);
        mapeditor2.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,5,4);
        mapeditor2.placeTile(TileType.PATH, PathType.TOPLEFT,4,4);
        mapeditor2.placeTile(TileType.TOWER, TowerType.ARCHER,4,5);
        mapeditor2.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,4,6);
        mapeditor2.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,3,6);
        mapeditor2.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,2,6);
        mapeditor2.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,1,6);
        mapeditor2.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,0,6);

        List<PathTile> isValid = Utilities.findPath(map2);

        assertNull(isValid);
    }

    @Test
    public void findingPathForValidMapTwo(){
        Map map3 = new Map("map3", 9, 16);
        MapEditor mapeditor3 = new MapEditor(map3);
        mapeditor3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,8,5);
        mapeditor3.placeTile(TileType.PATH, PathType.LEFT,7,5);
        mapeditor3.placeTile(TileType.PATH, PathType.TOPLEFT, 6,5);
        mapeditor3.placeTile(TileType.PATH, PathType.TOP, 6,6);
        mapeditor3.placeTile(TileType.PATH, PathType.BOTTOMRIGHT,6,7);
        mapeditor3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,5,7);
        mapeditor3.placeTile(TileType.PATH, PathType.LEFT, 4,7);
        mapeditor3.placeTile(TileType.PATH, PathType.TOPRIGHT,3,7);
        mapeditor3.placeTile(TileType.PATH, PathType.BOTTOMLEFT,3,6);
        mapeditor3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,2,6);
        mapeditor3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,1,6);
        mapeditor3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,0,6);

        mapeditor3.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 8,0);
        mapeditor3.placeTile(TileType.PATH, PathType.HORIZONTAL_MIDDLE, 8,1);

        List<PathTile> isValid = Utilities.findPath(map3);

        assertNotNull(isValid);
        assertEquals(isValid.getLast(), map3.getEndingTile());
        assertEquals(isValid.getFirst(),map3.getStartingTile());

    }
}