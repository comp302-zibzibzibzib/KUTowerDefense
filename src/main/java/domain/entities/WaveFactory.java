package domain.entities;

import domain.kutowerdefense.GameOptions;
import domain.kutowerdefense.Player;
import domain.services.Utilities;

import java.util.List;
import java.util.ArrayList;

public class WaveFactory {
	static int previousGroupAmount = 0;


	public static Wave createWave(int currentWave) {
		List<Group> groupList = new ArrayList<Group>();
		int waveNum = GameOptions.getInstance().getNumberOfWaves();
		double wavePercentage = (double) currentWave / (double) waveNum;

		double groupAmountMultiplier = 1.0 + 1.0 * wavePercentage;
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
