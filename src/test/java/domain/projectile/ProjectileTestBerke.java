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
