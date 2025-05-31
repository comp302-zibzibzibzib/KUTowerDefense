package domain.projectile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import domain.entities.Enemy;
import domain.entities.GoblinFactory;
import domain.entities.KnightFactory;
import domain.map.*;
import domain.tower.ArrowFactory;
import domain.tower.ArtilleryFactory;
import domain.tower.Projectile;
import domain.tower.SpellFactory;

public class ProjectileTestBerke {
	@AfterEach
    void cleanUpEnemies() {
        Enemy.cleanupAllEnemies();
    }
	/**
     * @requires
     *   - Enemy.path has already been set to a non-empty list of PathTile objects
     *   - enemy != null and enemy.isInitialized() == true
     *   - a non-null Projectile of any kind
     *   - Projectile.location has been set to
     *   - deltaTime (the argument to update) >= 0.0
     *
     * @modifies
     *   - Projectile.location (via repeated calls to update(...))
     *   - enemy location (via updateEnemy(...))
     *   - Internally, Projectile may remove itself from the global projectile list once it hits
     *
     * @effects
     *   - After each call to Projectile.update(...), the projectile’s location must change
     *     (i.e. travel from its previous coordinates). In other words:
     *       • travel1 != travel0
     *       • travel2 != travel1
     *       • travel3 != travel2
     *
     *   - This verifies that a spell‐type projectile “travels” toward its target over time
     */
	@Test 
	void travel() { // Berke Boylu
		Enemy.path = Arrays.asList(new PathTile[] { new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 0.0)),
				new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 5.0)),
				new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 10.0)) });
		Enemy goblin = GoblinFactory.getInstance().createEnemy();
		Location goblinLocation = new Location(Enemy.path.get(0).getLocation().xCoord,
				Enemy.path.get(0).getLocation().yCoord);

		goblin.setLocation(goblinLocation);
		goblin.setInitialized(true);
		
		Projectile spellProjectile = SpellFactory.getInstance().createProjectile(goblin, goblinLocation);
		Location initialLocation = new Location(5.0,5.0);
		spellProjectile.setLocation(initialLocation);
		
		Location travel0 = new Location(spellProjectile.getLocation().xCoord,spellProjectile.getLocation().yCoord);
		goblin.updateEnemy(100000000);
		spellProjectile.update(0.1);
		Location travel1 = new Location(spellProjectile.getLocation().xCoord,spellProjectile.getLocation().yCoord);
		assertNotEquals(travel1, travel0);
		
		goblin.updateEnemy(100000000);
		spellProjectile.update(0.1);
		Location travel2 = new Location(spellProjectile.getLocation().xCoord,spellProjectile.getLocation().yCoord);
		assertNotEquals(travel2, travel1);
		
		goblin.updateEnemy(100000000);
		spellProjectile.update(0.1);
		Location travel3 = new Location(spellProjectile.getLocation().xCoord,spellProjectile.getLocation().yCoord);
		assertNotEquals(travel3, travel2);
	}

    /**
     * @requires
     *   - Enemy.path has already been set to a non-empty list of PathTile objects.
     *   - enemy != null, goblin.isInitialized() == true, and enemy.getHitpoints <= projectile.getDamage
     *   - ProjectileFactory.getInstance().createProjectile(enemy, enemyLocation) returns a non-null Projectile
     *   - projectile.location has been set.
     *   - deltaTime (the argument to update) >= 0.0
     *
     * @modifies
     *   - enemy.hitPoints (via repeated calls to Projectile.update(...))
     *   - Internally, Projectile may remove itself from the global projectiles list once it hits
     *
     * @effects
     *   - After projectile hits enemy, enemy.getHitPoints() must be ≤ 0. In other words the projectile should deal enough damage
     *     to kill the goblin and goblin is cleaned up afterwards from activeEnemies
     *     
     *   - This verifies that a projectile correctly follows its target and, upon collision, kills the target
     */
	@Test
	void killEnemy() { // Berke Boylu
		Enemy.path = Arrays.asList(new PathTile[] { new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 0.0)),
				new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 5.0)),
				new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 10.0)) });
		Enemy goblin = GoblinFactory.getInstance().createEnemy();
		Location goblinLocation = new Location(Enemy.path.get(0).getLocation().xCoord,
				Enemy.path.get(0).getLocation().yCoord);

		goblin.setLocation(goblinLocation);
		goblin.setInitialized(true);
		goblin.setHitPoint(5.0);
		
		Projectile arrowProjectile = ArrowFactory.getInstance().createProjectile(goblin, goblinLocation);
		Location projectileLocation = new Location(5.0,5.0);
		arrowProjectile.setLocation(projectileLocation);

		goblin.updateEnemy(100000000);
		arrowProjectile.update(0.1);
		
		goblin.updateEnemy(100000000);
		arrowProjectile.update(0.1);
		
		goblin.updateEnemy(100000000);
		arrowProjectile.update(0.1);

		assertTrue(goblin.getHitPoints() <= 0);
		assertTrue(Enemy.activeEnemies.isEmpty());
	}

    /**
     * @requires
     *   - Enemy.path has already been set to a non-empty list of PathTile objects
     *   - At least two enemies are created, and they are within the splash radius
     *   - ArtilleryFactory.getInstance().createProjectile(enemy, target) returns a non-null AOE Projectile
     *   - AOEProjectile.location has been set
     *   - deltaTime (argument to update) ≥ 0.0
     *
     * @modifies
     *   - enemy1.hitPoints and enemy2.hitPoints, ... , enemyN.hitpoints (once the projectile’s distance to target < 1)
     *   - Internally, AOEProjectile may remove itself from the global projectile list when it hits
     *
     * @effects
     *   - After projectile hits the targeted enemy within the splash radius range, enemy1, enemy2, ... , enemyN must have taken damage. Concretely:
     *       • enemy1Health2 != enemy1Health1
     *       • enemy2Health2 != enemy2Health1
     *       ...
     *		 • enemyNHealth2 != enemyNHealth1
     *
     *   - This verifies that an artillery‐type projectile (AOE) deals splash damage to all targets within the splash radius
     *     once it reaches its target
     */
	@Test
	void AOEDamage() { // Berke Boylu
		Enemy.path = Arrays.asList(new PathTile[] { new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 0.0)),
				new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 5.0)),
				new PathTile(PathType.VERTICAL_MIDDLE, new Location(0.0, 10.0)) });
		Enemy knight = KnightFactory.getInstance().createEnemy();
		Enemy goblin = GoblinFactory.getInstance().createEnemy();
		Location knightLocation = new Location(Enemy.path.get(0).getLocation().xCoord,
				Enemy.path.get(0).getLocation().yCoord - 0.1);
		Location goblinLocation = new Location(Enemy.path.get(0).getLocation().xCoord,
				Enemy.path.get(0).getLocation().yCoord);

		knight.setLocation(knightLocation);
		knight.setInitialized(true);
		double knightHealth1 = knight.getHitPoints();

		goblin.setLocation(goblinLocation);
		goblin.setInitialized(true);
		double goblinHealth1 = goblin.getHitPoints();
		
		Projectile AOEProjectile = ArtilleryFactory.getInstance().createProjectile(goblin, goblinLocation);
		Location projectileLocation = new Location(5.0,5.0);
		AOEProjectile.setLocation(projectileLocation);
	
		knight.updateEnemy(100000000);
		goblin.updateEnemy(100000000);
		AOEProjectile.update(0.1);
		
		knight.updateEnemy(100000000);
		goblin.updateEnemy(100000000);
		AOEProjectile.update(0.1);
		
		knight.updateEnemy(100000000);
		goblin.updateEnemy(100000000);
		AOEProjectile.update(0.1);

		double knightHealth2 = knight.getHitPoints();
		double goblinHealth2 = goblin.getHitPoints();
		assertNotEquals(knightHealth1, knightHealth2);
		assertNotEquals(goblinHealth1, goblinHealth2);
	}
}
