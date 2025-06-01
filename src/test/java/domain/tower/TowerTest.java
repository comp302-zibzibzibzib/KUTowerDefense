package domain.tower;

import domain.entities.Enemy;
import domain.entities.Goblin;
import domain.map.*;
import domain.services.Utilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    static Map map;
    static MapEditor me;
    static ArcherTower tower;

    @BeforeEach
    void mapCreate(){ // 4x2 test map
        map = new Map("tower update test", 4, 2);
        me = new MapEditor(map);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0, 1);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1, 1);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 2, 1);
        me.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 3, 1);
        tower = new ArcherTower(0,0,1,30.0,10.0);
        tower.location = map.tileMap[0][0].getLocation();
        Enemy.path = Arrays.asList((PathTile) map.tileMap[3][1], (PathTile) map.tileMap[2][1], (PathTile) map.tileMap[1][1], (PathTile) map.tileMap[0][1]);

    }

    @Test
    void updateTest_CorrectProjectileTypeCreation() { //Haydar Arda Subasi
        //test whether update() function creates the correct projectile depending on the tower, also test whether targets single enemy
        Location loc = new Location(map.tileMap[3][1].getLocation().xCoord, map.tileMap[3][1].getLocation().yCoord - 0.1 );
        Goblin goblin = new Goblin(1.0,1.0,loc,0.1);
        goblin.setPathIndex(0);
        goblin.setInitialized(true);
        Projectile proj = tower.update(0.10000001);
        assertEquals(AttackType.ARROW, proj.attackType);

    }

    @Test
    void updateTest_ObeysCooldown(){ //Haydar Arda Subasi
        //test whether update() function correctly does not create new projectile during cooldown
        Location loc = new Location(map.tileMap[3][1].getLocation().xCoord, map.tileMap[3][1].getLocation().yCoord - 0.1 );
        Goblin goblin = new Goblin(1.0,1.0,loc,0.1);
        goblin.setPathIndex(0);
        goblin.setInitialized(true);
        Projectile proj = tower.update(1); // reset time since last shot
        Projectile proj2 = tower.update(0.001); //should be null ie. no shooting
        assertNull(proj2);
    }

    @Test
    void updateTest_TargetsFirst(){ //Haydar Arda Subasi
        //tests whether update() function correctly chooses the enemy farthest along the path and creates the projectile accordingly
        Location loc = new Location(map.tileMap[3][1].getLocation().xCoord, map.tileMap[3][1].getLocation().yCoord - 0.1 );
        Goblin goblin = new Goblin(1.0,1.0,loc,0.1);
        goblin.setPathIndex(0);
        goblin.setInitialized(true);

        //add second goblin ver small distance further
        Location loc1 = new Location(map.tileMap[3][1].getLocation().xCoord, map.tileMap[3][1].getLocation().yCoord - 0.100000001 );
        Goblin goblin1 = new Goblin(1.0,1.0,loc1,0.1);
        goblin1.setPathIndex(0);
        goblin1.setInitialized(true);

        Projectile proj = tower.update(1);
        assertEquals(goblin1, proj.target);

        //add third goblin on the next tile to test retargeting
        Location loc2 = new Location(map.tileMap[3][1].getLocation().xCoord, map.tileMap[1][1].getLocation().yCoord - 0.1001 );
        Goblin goblin2 = new Goblin(1.0,1.0,loc2,0.1);
        goblin2.setPathIndex(2);
        goblin2.setInitialized(true);

        proj = tower.update(1);
        assertEquals(goblin2, proj.target);

    }
}