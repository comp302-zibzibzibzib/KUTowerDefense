package domain.entities;

import java.util.ArrayList;
import java.util.List;

public class GroupFactory {
	public static final String groupComp1 = "kgkgg";
	public static final String groupComp2 = "ggg";
	public static final String groupComp3 = "kkk";
	public static final String groupComp4 = "kgk";
	public static final String groupComp5 = "gkg";

	public static Group createGroup(String groupComposition) {
		List<Enemy> enemyComposition = new ArrayList<Enemy>();
		for (char c : groupComposition.toCharArray()) {
			Enemy enemy = switch(c) {
				case 'k' -> EnemyFactory.createKnight();
				case 'g' -> EnemyFactory.createGoblin();
				default -> null;
			};
			
			if (enemy == null) continue;
			enemyComposition.add(enemy);
		}
		
		return new Group(enemyComposition.size(), enemyComposition);
	}
}
