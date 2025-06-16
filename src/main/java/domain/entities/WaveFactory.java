package domain.entities;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.Player;
import domain.services.Utilities;

import java.util.List;
import java.util.ArrayList;

public class WaveFactory {
	// Hides complex wave creation logic
	// Factory Pattern
	static int previousGroupAmount = 0;

	public static Wave createWave(int currentWave) {
		List<Group> groupList = new ArrayList<Group>();
		int waveNum = GameOptions.getInstance().getNumberOfWaves();
		double wavePercentage = (double) currentWave / (double) waveNum;

		// Amount of groups grow as we slowly reach the end game
		// Similar randomized logic with group creation
		double groupAmountMultiplier = 0.5 + 1.5 * wavePercentage;
		int maxGroup = (int) (GameOptions.getInstance().getMaxGroupPerWave() * groupAmountMultiplier);
		int minGroup = (int) (GameOptions.getInstance().getMinGroupPerWave() * groupAmountMultiplier);

		int groupAmount = Utilities.globalRNG.nextInt(minGroup, maxGroup + 1);

		if (groupAmount < previousGroupAmount) {
			groupAmount += (int) ((previousGroupAmount - groupAmount) * 0.5);
		} else {
			previousGroupAmount = groupAmount;
		}

		for (int i = 0; i < groupAmount; i++) {
			Group group = GroupFactory.createGroup(currentWave);
			groupList.add(group);
		}

		return new Wave(groupList.size(), groupList);
	}
}
