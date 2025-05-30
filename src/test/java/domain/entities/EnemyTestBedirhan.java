package domain.entities;

import domain.map.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTestBedirhan {

    @Test
    void knightSpeedsUpNearGoblin() {
        Enemy goblin = GoblinFactory.getInstance().createEnemy();
        Enemy knight = KnightFactory.getInstance().createEnemy();

        Location goblinLocation = new Location(0.0, 0.0);
        // Within one tile length range
        Location knightLocation = new Location(goblinLocation.xCoord + Tile.tileLength / 2, 0);

        goblin.setLocation(goblinLocation);
        knight.setLocation(knightLocation);

        goblin.setInitialized(true);
        knight.setInitialized(true);

        ((Knight) knight).knightSpeedUp();

        assertTrue(((Knight) knight).isFast());
        Enemy.cleanupAllEnemies();
    }

    @Test
    void knightDoesNotSpeedUpAwayFromGoblin() {
        Enemy goblin = GoblinFactory.getInstance().createEnemy();
        Enemy knight = KnightFactory.getInstance().createEnemy();

        Location goblinLocation = new Location(0.0, 0.0);
        // Not within one tile length range
        Location knightLocation = new Location(goblinLocation.xCoord + Tile.tileLength,
                                        goblinLocation.yCoord + Tile.tileLength);

        goblin.setLocation(goblinLocation);
        knight.setLocation(knightLocation);

        goblin.setInitialized(true);
        knight.setInitialized(true);

        ((Knight) knight).knightSpeedUp();

        assertFalse(((Knight) knight).isFast());
        Enemy.cleanupAllEnemies();
    }

    @Test
    void enemyMovesTowardsNextPath() {
        Enemy.path = Arrays.asList(new PathTile[]{new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 0.0)),
                                        new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 5.0)),
                                        new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 10.0))});
        Enemy knight = KnightFactory.getInstance().createEnemy();
        Location knightLocation = new Location(Enemy.path.get(0).getLocation().xCoord,
                                                Enemy.path.get(0).getLocation().yCoord - 0.1);

        knight.setLocation(knightLocation);
        knight.setInitialized(true);

        // Run for 3 frames to be sure of overall movement
        knight.updateEnemy(100000000);
        knight.updateEnemy(100000000);
        knight.updateEnemy(100000000);

        double deltaX = knight.location.xCoord - Enemy.path.get(1).getLocation().xCoord;
        double deltaY = knight.location.yCoord - Enemy.path.get(1).getLocation().yCoord;

        assertTrue(Math.sqrt(deltaX * deltaX + deltaY * deltaY) < 5);
        Enemy.cleanupAllEnemies();
    }

    @Test
    void slowDownEnemy() {
        Enemy goblin = GoblinFactory.getInstance().createEnemy();
        Enemy knight = KnightFactory.getInstance().createEnemy();

        double goblinSpeed = goblin.getSpeed();
        double knightSpeed = knight.getSpeed();

        goblin.slowDown();
        knight.slowDown();

        assertEquals(goblin.getSpeed(), goblinSpeed * 0.8);
        assertEquals(knight.getSpeed(), knightSpeed * 0.8);
        Enemy.cleanupAllEnemies();
    }

    @Test
    void slowDownSynergicKnight() {
        Enemy knight = KnightFactory.getInstance().createEnemy();
        Enemy goblin = GoblinFactory.getInstance().createEnemy();

        Location goblinLocation = new Location(0.0, 0.0);
        Location knightLocation = new Location(goblinLocation.xCoord + Tile.tileLength / 2, 0.0);

        goblin.setLocation(goblinLocation);
        knight.setLocation(knightLocation);

        goblin.setInitialized(true);
        knight.setInitialized(true);

        ((Knight)knight).knightSpeedUp();

        assertTrue(((Knight)knight).isFast());

        knight.slowDown();

        assertEquals(((knight.defaultSpeed + goblin.defaultSpeed) / 2) * 0.8, knight.getSpeed());
        Enemy.cleanupAllEnemies();
    }

    @Test
    void synergizeSlowedKnight() {
        Enemy knight = KnightFactory.getInstance().createEnemy();

        Location goblinLocation = new Location(0.0, 0.0);
        Location knightLocation = new Location(goblinLocation.xCoord + Tile.tileLength / 2, 0.0);


        knight.setLocation(knightLocation);

        knight.setInitialized(true);
        knight.slowDown();

        ((Knight)knight).knightSpeedUp();

        assertFalse(((Knight)knight).isFast());

        Enemy goblin = GoblinFactory.getInstance().createEnemy();
        goblin.setLocation(goblinLocation);
        goblin.setInitialized(true);
        ((Knight)knight).knightSpeedUp();

        assertTrue(((Knight)knight).isFast());
        assertEquals(((knight.defaultSpeed + goblin.defaultSpeed) / 2) * 0.8, knight.getSpeed());
        Enemy.cleanupAllEnemies();
    }
}