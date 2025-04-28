package domain.entities;

public class EnemyFactory {
	// idk about these values
	private static final double GOBLIN_HIT_POINTS = 30;
	private static final double KNIGHT_HIT_POINTS = 50;
	private static final double GOBLIN_SPEED = 4;
	private static final double KNIGHT_SPEED = 4;
	private static final double GOBLIN_SPELL_REDUCTION = 10;
	private static final double KNIGHT_ARROW_REDUCTION = 10;
	
	public static Goblin createGoblin() {
		return new Goblin(GOBLIN_HIT_POINTS, GOBLIN_SPEED, null, GOBLIN_SPELL_REDUCTION);
	}
	
	public static Knight createKnight() {
		return new Knight(KNIGHT_HIT_POINTS, KNIGHT_SPEED, null, KNIGHT_ARROW_REDUCTION);
	}
}
