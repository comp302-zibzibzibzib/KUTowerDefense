package domain.entities;

import domain.kutowerdefense.GameOptions;
import domain.services.Utilities;

import java.util.ArrayList;
import java.util.List;

public class GroupFactory {
	// Hides complex group creation logic
	// Factory Pattern
	public static Group createGroup(int currentWave) {
		List<Enemy> enemyComposition = new ArrayList<Enemy>();
		double knightPercentage = GameOptions.getInstance().getKnightPercentage();

		int waveNum = GameOptions.getInstance().getNumberOfWaves();
		double wavePercentage = (double) currentWave / (double) waveNum;

		// The number of enemies in each group grows as the game gets closer to the end
		double enemyAmountMultiplier = 0.5 + 1.5 * wavePercentage;
		// The max and min enemy number specified in the game options get multiplied by the amountMultiplier
		int maxEnemy = (int) (GameOptions.getInstance().getMaxEnemiesPerGroup() * enemyAmountMultiplier);
		int minEnemy = (int) (GameOptions.getInstance().getMinEnemiesPerGroup() * enemyAmountMultiplier);

		// Get a random bounded enemy amount
		int enemyAmount = Utilities.globalRNG.nextInt(maxEnemy - minEnemy + 1) + minEnemy;

		for (int i = 0; i < enemyAmount; i++) {
			Enemy enemy;
			if (Utilities.globalRNG.nextDouble() < knightPercentage) {
				enemy = KnightFactory.getInstance().createEnemy();
			} else {
				enemy = GoblinFactory.getInstance().createEnemy();
			}

			enemyComposition.add(enemy);
		}

		return new Group(enemyComposition.size(), enemyComposition);
	}
}
