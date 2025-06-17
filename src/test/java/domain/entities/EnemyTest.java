package domain.entities;

import domain.map.Location;
import domain.map.PathTile;
import domain.map.PathType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    @AfterEach
    void cleanUpEnemies() {
        Enemy.cleanupAllEnemies();
    }

    @Test // Bedirhan Sakaoğlu
    void moveEnemyTowardsPath() {
        // An enemy should close their distance with their target destination
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
    }

    @Test // Bedirhan Sakaoğlu
    void enemyReachesEndingPath() {
        // An enemy will eventually reach their destination
        Enemy.path = Arrays.asList(new PathTile[]{new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 0.0)),
                new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 5.0)),
                new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 10.0))});
        Enemy knight = KnightFactory.getInstance().createEnemy();
        Location knightLocation = new Location(Enemy.path.get(0).getLocation().xCoord,
                Enemy.path.get(0).getLocation().yCoord - 0.1);

        knight.setLocation(knightLocation);
        knight.setInitialized(true);

        int i = 0;
        // Run until enemy reaches ending
        while(knight.isInitialized()) {
            i++;
            knight.updateEnemy(100000000);

            // Fail if it takes too much time
            if (i > 1000000) {
                fail();
            }
        }

        double deltaX = knight.location.xCoord - Enemy.path.get(1).getLocation().xCoord;
        double deltaY = knight.location.yCoord - Enemy.path.get(1).getLocation().yCoord;

        assertFalse(knight.isInitialized());
    }

    @Test // Bedirhan Sakaoğlu
    void slowDownWearsOffAfterFourSeconds() {
        // A slowed enemy will have their slow effect wear off after 4 seconds
        Enemy.path = Arrays.asList(new PathTile[]{new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 0.0)),
                new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 5.0)),
                new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 10.0))});
        Enemy knight = KnightFactory.getInstance().createEnemy();
        Location knightLocation = new Location(Enemy.path.get(0).getLocation().xCoord,
                Enemy.path.get(0).getLocation().yCoord - 0.1);

        knight.setLocation(knightLocation);
        knight.setInitialized(true);

        knight.slowDown();

        int i = 0;
        // Run until slow down wears off
        while(knight.isSlowedDown()) {
            i++;
            knight.updateEnemy(100000000);

            // Fail if it takes too much time
            if (i > 1000000) {
                fail();
            }
        }

        assertFalse(knight.isSlowedDown());
    }
}