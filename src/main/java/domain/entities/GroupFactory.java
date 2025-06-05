package domain.entities;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.Player;
import domain.services.Utilities;

import java.util.ArrayList;
import java.util.List;

public class GroupFactory {
	public static Group createGroup(int currentWave) {
		List<Enemy> enemyComposition = new ArrayList<Enemy>();
		double knightPercentage = GameOptions.getInstance().getKnightPercentage();

		int waveNum = GameOptions.getInstance().getNumberOfWaves();
		double wavePercentage = (double) currentWave / (double) waveNum;

		double enemyAmountMultiplier = 1.0 + 1.0 * wavePercentage;
		int maxEnemy = (int) (GameOptions.getInstance().getMaxEnemiesPerGroup() * enemyAmountMultiplier);
		int minEnemy = (int) (GameOptions.getInstance().getMinEnemiesPerGroup() * enemyAmountMultiplier);

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
