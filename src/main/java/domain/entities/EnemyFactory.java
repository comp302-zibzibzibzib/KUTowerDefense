package domain.entities;

public interface EnemyFactory {
	static double GOBLIN_SPELL_REDUCTION = 0.3;
	static double KNIGHT_ARROW_REDUCTION = 0.3;

	static double BOSS_HIT_POINTS = 5000; //might be too much
	static double BOSS_SPEED = 1.0;
	static double BOSS_SPELL_REDUCTION = 0.8; //To penalise spamming mage towers to slowdown or reset
	static double BOSS_ARROW_REDUCTION = 0.5;
	static double BOSS_ARTILLERY_INCREASE = 1.4; // new(!) addition
	
	Enemy createEnemy();
}
