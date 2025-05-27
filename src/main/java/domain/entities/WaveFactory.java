package domain.entities;

import java.util.Arrays;
import java.util.List;

public class WaveFactory {
	
	public static Wave createWave() { // Currently always generates a wave with the same properties
		Group group1 = GroupFactory.createGroup(GroupFactory.groupComp1);
		Group group2 = GroupFactory.createGroup(GroupFactory.groupComp2);
		Group group3 = GroupFactory.createGroup(GroupFactory.groupComp3);
		Group group4 = GroupFactory.createGroup(GroupFactory.groupComp4);
		Group group5 = GroupFactory.createGroup(GroupFactory.groupComp5);
		
		List<Group> groupList = Arrays.asList(new Group[]{group1, group2, group3, group4, group5});
		List<Double> delayList = Arrays.asList(new Double[]{group1.getSpawnDelay(), group2.getSpawnDelay(),
				group3.getSpawnDelay(), group4.getSpawnDelay(), group5.getSpawnDelay()});
		
		return new Wave(groupList.size(), groupList, delayList);
	}
}
