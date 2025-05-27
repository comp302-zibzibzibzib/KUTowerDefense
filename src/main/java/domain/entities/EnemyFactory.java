package domain.entities;

public interface EnemyFactory {
	// idk about these values
	static double GOBLIN_HIT_POINTS = 50;
	static double KNIGHT_HIT_POINTS = 50;
	static double GOBLIN_SPELL_REDUCTION = 0.3;
	static double KNIGHT_ARROW_REDUCTION = 0.3;
	static double KNIGHT_SPEED = 3.0;
	static double GOBLIN_SPEED = 6.0;
	
	Enemy createEnemy();
}
