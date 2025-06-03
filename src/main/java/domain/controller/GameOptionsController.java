package domain.controller;

import domain.kutowerdefense.GameOptions;
import domain.services.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GameOptionsController {

	public static final List<String> optionNames = Arrays.asList("Wave Number", "Max Group per Wave",
			"Min Group per Wave", "Max Enemies per Group", "Min Enemies per Group", "Wave Delay", "Group Delay", "Enemy Delay",
			"Knight Percentage", "Goblin Percentage", "Player Gold", "Player Lives", "Goblin Health", "Goblin Speed", "Goblin Reward",
			"Knight Health", "Knight Speed", "Knight Reward", "Arrow Damage", "Artillery Damage", "Spell Damage", "AOE Range",
			"Archer Cost", "Artillery Cost", "Mage Cost", "Archer Range", "Artillery Range", "Mage Range");

	public static HashMap<String, Number[]> optionRanges;

	public static HashMap<String, Supplier<Number>> supplierMap;

	public static HashMap<String, Consumer<Number>> consumerMap;

	private static GameOptions options;

	public static void initializeGameOptions() {
		GameOptions.initializeGameOptions();
		options = GameOptions.getInstance();

		// I am really sorry for this but this is the safest way
		// I tried making it iterate over the declared fields of GameOptions.class
		// but as it turns out it is quite unreliable
		// Please don't waste your time refactoring this code
		consumerMap = new HashMap<>();
		consumerMap.put("Wave Number", options::setNumberOfWaves);
		consumerMap.put("Max Group per Wave", options::setMaxGroupPerWave);
		consumerMap.put("Min Group per Wave", options::setMinGroupPerWave);
		consumerMap.put("Max Enemies per Group", options::setMaxEnemiesPerGroup);
		consumerMap.put("Min Enemies per Group", options::setMinEnemiesPerGroup);
		consumerMap.put("Wave Delay", options::setWaveDelay);
		consumerMap.put("Group Delay", options::setGroupDelay);
		consumerMap.put("Enemy Delay", options::setEnemyDelay);
		consumerMap.put("Knight Percentage", options::setKnightPercentage);
		consumerMap.put("Goblin Percentage", options::setGoblinPercentage);
		consumerMap.put("Player Gold", options::setStartingPlayerGold);
		consumerMap.put("Player Lives", options::setStartingPlayerLives);
		consumerMap.put("Goblin Health", options::setGoblinHealth);
		consumerMap.put("Goblin Speed", options::setGoblinSpeed);
		consumerMap.put("Goblin Reward", options::setGolbinReward);
		consumerMap.put("Knight Health", options::setKnightHealth);
		consumerMap.put("Knight Speed", options::setKnightSpeed);
		consumerMap.put("Knight Reward", options::setKnightReward);
		consumerMap.put("Arrow Damage", options::setArrowDamage);
		consumerMap.put("Artillery Damage", options::setArtilleryDamage);
		consumerMap.put("Spell Damage", options::setSpellDamage);
		consumerMap.put("AOE Range", options::setAoeRange);
		consumerMap.put("Archer Cost", options::setArcherCost);
		consumerMap.put("Artillery Cost", options::setArtilleryCost);
		consumerMap.put("Mage Cost", options::setMageCost);
		consumerMap.put("Archer Range", options::setArcherRange);
		consumerMap.put("Artillery Range", options::setArtilleryRange);
		consumerMap.put("Mage Range", options::setMageRange);

		supplierMap = new HashMap<>();
		supplierMap.put("Wave Number", options::getNumberOfWaves);
		supplierMap.put("Max Group per Wave", options::getMaxGroupPerWave);
		supplierMap.put("Min Group per Wave", options::getMinGroupPerWave);
		supplierMap.put("Max Enemies per Group", options::getMaxEnemiesPerGroup);
		supplierMap.put("Min Enemies per Group", options::getMinEnemiesPerGroup);
		supplierMap.put("Wave Delay", options::getWaveDelay);
		supplierMap.put("Group Delay", options::getGroupDelay);
		supplierMap.put("Enemy Delay", options::getEnemyDelay);
		supplierMap.put("Knight Percentage", options::getKnightPercentage);
		supplierMap.put("Goblin Percentage", options::getGoblinPercentage);
		supplierMap.put("Player Gold", options::getStartingPlayerGold);
		supplierMap.put("Player Lives", options::getStartingPlayerLives);
		supplierMap.put("Goblin Health", options::getGoblinHealth);
		supplierMap.put("Goblin Speed", options::getGoblinSpeed);
		supplierMap.put("Goblin Reward", options::getGolbinReward);
		supplierMap.put("Knight Health", options::getKnightHealth);
		supplierMap.put("Knight Speed", options::getKnightSpeed);
		supplierMap.put("Knight Reward", options::getKnightReward);
		supplierMap.put("Arrow Damage", options::getArrowDamage);
		supplierMap.put("Artillery Damage", options::getArtilleryDamage);
		supplierMap.put("Spell Damage", options::getSpellDamage);
		supplierMap.put("AOE Range", options::getAoeRange);
		supplierMap.put("Archer Cost", options::getArcherCost);
		supplierMap.put("Artillery Cost", options::getArtilleryCost);
		supplierMap.put("Mage Cost", options::getMageCost);
		supplierMap.put("Archer Range", options::getArcherRange);
		supplierMap.put("Artillery Range", options::getArtilleryRange);
		supplierMap.put("Mage Range", options::getMageRange);

		optionRanges = new HashMap<>();
		optionRanges.put("Wave Number", new Number[]{1, 20, 1});
		optionRanges.put("Max Group per Wave", new Number[]{5, 10, 1});
		optionRanges.put("Min Group per Wave", new Number[]{1, 5, 1});
		optionRanges.put("Max Enemies per Group", new Number[]{5, 10, 1});
		optionRanges.put("Min Enemies per Group", new Number[]{2, 5, 1});
		optionRanges.put("Wave Delay", new Number[]{2.0, 10.0, 0.1});
		optionRanges.put("Group Delay", new Number[]{1.0, 4.0, 0.1});
		optionRanges.put("Enemy Delay", new Number[]{0.5, 2.0, 0.1});
		optionRanges.put("Knight Percentage", new Number[]{0.0, 1.0, 0.01});
		optionRanges.put("Goblin Percentage", new Number[]{0.0, 1.0, 0.01});
		optionRanges.put("Player Gold", new Number[]{200, 1000, 10});
		optionRanges.put("Player Lives", new Number[]{10, 50, 1});
		optionRanges.put("Goblin Health", new Number[]{40.0, 100.0, 5.0});
		optionRanges.put("Goblin Speed", new Number[]{6.0, 10.0, 0.1});
		optionRanges.put("Goblin Reward", new Number[]{10, 100, 1});
		optionRanges.put("Knight Health", new Number[]{40.0, 100.0, 5.0});
		optionRanges.put("Knight Speed", new Number[]{2.0, 4.0, 0.1});
		optionRanges.put("Knight Reward", new Number[]{10, 100, 1});
		optionRanges.put("Arrow Damage", new Number[]{5.0, 15.0, 1.0});
		optionRanges.put("Artillery Damage", new Number[]{25.0, 40.0, 1.0});
		optionRanges.put("Spell Damage", new Number[]{15.0, 25.0, 1.0});
		optionRanges.put("AOE Range", new Number[]{0.5, 3.0, 0.5});
		optionRanges.put("Archer Cost", new Number[]{10, 200, 5});
		optionRanges.put("Artillery Cost", new Number[]{10, 300, 5});
		optionRanges.put("Mage Cost", new Number[]{10, 400, 5});
		optionRanges.put("Archer Range", new Number[]{1.0, 10.0, 0.1});
		optionRanges.put("Artillery Range", new Number[]{1.0, 10.0, 0.1});
		optionRanges.put("Mage Range", new Number[]{1.0, 10.0, 0.1});
 	}

	public static Consumer<Number> getConsumer(String name) {
		return consumerMap.get(name);
	}

	public static Supplier<Number> getSupplier(String name) {
		return supplierMap.get(name);
	}

	public static Number[] getOptionConstraints(String name) {
		return optionRanges.get(name);
	}

	public static List<String> getOptionNames() {
		return optionNames;
	}
	
	public static int getStartingLives() {
		return options.getStartingPlayerLives();
	}
	
	public static int getStartingGold() {
		return options.getStartingPlayerGold();
	}
	
	public static int getNumberOfWaves() {
		return options.getNumberOfWaves();
	}
	
	public static void saveOptions() {
		Utilities.writeOptions();
	}
	
	public static void resetOptions() {
		options.resetOptions();
	}

	public static GameOptions options() {
		return options;
	}

	public static void nullifyOptions() {
		GameOptions.nullifyOptions();
	}
}
