package domain.entities;

import domain.kutowerdefense.GameOptions;

public interface EnemyFactory {
	// idk about these values
	static double GOBLIN_HIT_POINTS = 50;
	static double KNIGHT_HIT_POINTS = 50;
	static double GOBLIN_SPELL_REDUCTION = 0.3;
	static double KNIGHT_ARROW_REDUCTION = 0.3;
	
	Enemy createEnemy();
}
