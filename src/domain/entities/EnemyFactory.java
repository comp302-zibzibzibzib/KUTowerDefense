package domain.entities;

import domain.kutowerdefense.GameOptions;

public interface EnemyFactory {
	// idk about these values
	static double GOBLIN_HIT_POINTS = 50;
	static double KNIGHT_HIT_POINTS = 50;
	static double GOBLIN_SPELL_REDUCTION = 0.3;
	static double KNIGHT_ARROW_REDUCTION = 0.3;
	static double KNIGHT_SPEED = 3.0;
	static double GOBLIN_SPEED = 6.0;
	//To hide the existence of Boss values should not be able to changed/seen in options
	static double BOSS_HIT_POINTS = 5000; //might be too much
	static double BOSS_SPEED = 1.0;
	static double BOSS_SPELL_REDUCTION = 0.8; //To penalise spamming mage towers to slowdown or reset
	static double BOSS_ARROW_REDUCTION = 0.5;
	static double BOSS_ARTILLERY_INCREASE = 1.4; // new(!) addition
	
	Enemy createEnemy();
}
