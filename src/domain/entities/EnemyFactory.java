package domain.entities;

import domain.kutowerdefense.GameOptions;

public class EnemyFactory {
	private static GameOptions options = GameOptions.getInstance();
	// idk about these values
	private static double GOBLIN_HIT_POINTS = 30;
	private static double KNIGHT_HIT_POINTS = 50;
	private static double GOBLIN_SPELL_REDUCTION = 10;
	private static double KNIGHT_ARROW_REDUCTION = 10;
	
	public static Goblin createGoblin() {
		return new Goblin(GOBLIN_HIT_POINTS, options.getEnemySpeed(), null, GOBLIN_SPELL_REDUCTION);
	}
	
	public static Knight createKnight() {
		return new Knight(KNIGHT_HIT_POINTS, options.getEnemySpeed(), null, KNIGHT_ARROW_REDUCTION);
	}
}
