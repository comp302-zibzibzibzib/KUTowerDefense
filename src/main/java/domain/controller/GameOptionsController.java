package domain.controller;

import domain.kutowerdefense.GameOptions;
import domain.services.Utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GameOptionsController {

	public static final List<String> optionNames = Arrays.asList("Wave Number", "Max Group per Wave",
			"Min Group per Wave", "Max Enemies per Group", "Min Enemies per Group", "Wave Delay", "Group Delay", "Enemy Delay",
			"Knight Percentage", "Goblin Percentage", "Player Gold", "Player Lives", "Goblin Health", "Goblin Speed", "Goblin Reward",
			"Knight Health", "Knight Speed", "Knight Reward", "Gold Bag Chance", "Arrow Damage", "Artillery Damage", "Spell Damage", "AOE Range",
			"Archer Cost", "Archer Upgrade Cost", "Archer Range", "Archer Fire Rate", "Artillery Cost", "Artillery Upgrade Cost", 
			"Artillery Range", "Artillery Fire Rate", "Mage Cost", "Mage Upgrade Cost", "Mage Range", "Mage Fire Rate");

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
		consumerMap.put("Goblin Reward", options::setGoblinReward);
		consumerMap.put("Knight Health", options::setKnightHealth);
		consumerMap.put("Knight Speed", options::setKnightSpeed);
		consumerMap.put("Knight Reward", options::setKnightReward);
		consumerMap.put("Gold Bag Chance", options::setGoldBagChance);
		consumerMap.put("Arrow Damage", options::setArrowDamage);
		consumerMap.put("Artillery Damage", options::setArtilleryDamage);
		consumerMap.put("Spell Damage", options::setSpellDamage);
		consumerMap.put("AOE Range", options::setAoeRange);
		consumerMap.put("Archer Cost", options::setArcherCost);
		consumerMap.put("Archer Upgrade Cost", options::setArcherUpgradeCost);
		consumerMap.put("Archer Range", options::setArcherRange);
		consumerMap.put("Archer Fire Rate", options::setArcherFireRate);
		consumerMap.put("Artillery Cost", options::setArtilleryCost);
		consumerMap.put("Artillery Upgrade Cost", options::setArtilleryUpgradeCost);
		consumerMap.put("Artillery Range", options::setArtilleryRange);
		consumerMap.put("Artillery Fire Rate", options::setArtilleryFireRate);
		consumerMap.put("Mage Cost", options::setMageCost);
		consumerMap.put("Mage Upgrade Cost", options::setMageUpgradeCost);
		consumerMap.put("Mage Range", options::setMageRange);
		consumerMap.put("Mage Fire Rate", options::setMageFireRate);

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
		supplierMap.put("Goblin Reward", options::getGoblinReward);
		supplierMap.put("Knight Health", options::getKnightHealth);
		supplierMap.put("Knight Speed", options::getKnightSpeed);
		supplierMap.put("Knight Reward", options::getKnightReward);
		supplierMap.put("Gold Bag Chance", options::getGoldBagChance);
		supplierMap.put("Arrow Damage", options::getArrowDamage);
		supplierMap.put("Artillery Damage", options::getArtilleryDamage);
		supplierMap.put("Spell Damage", options::getSpellDamage);
		supplierMap.put("AOE Range", options::getAoeRange);
		supplierMap.put("Archer Cost", options::getArcherCost);
		supplierMap.put("Archer Upgrade Cost", options::getArcherUpgradeCost);
		supplierMap.put("Archer Range", options::getArcherRange);
		supplierMap.put("Archer Fire Rate", options::getArcherFireRate);
		supplierMap.put("Artillery Cost", options::getArtilleryCost);
		supplierMap.put("Artillery Upgrade Cost", options::getArtilleryUpgradeCost);
		supplierMap.put("Artillery Range", options::getArtilleryRange);
		supplierMap.put("Artillery Fire Rate", options::getArtilleryFireRate);
		supplierMap.put("Mage Cost", options::getMageCost);
		supplierMap.put("Mage Upgrade Cost", options::getMageUpgradeCost);
		supplierMap.put("Mage Range", options::getMageRange);
		supplierMap.put("Mage Fire Rate", options::getMageFireRate);

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
		optionRanges.put("Gold Bag Chance", new Number[]{0.0, 1.0, 0.01});
		optionRanges.put("Arrow Damage", new Number[]{5.0, 15.0, 1.0});
		optionRanges.put("Artillery Damage", new Number[]{25.0, 40.0, 1.0});
		optionRanges.put("Spell Damage", new Number[]{15.0, 25.0, 1.0});
		optionRanges.put("AOE Range", new Number[]{2.0, 6.0, 0.1});
		optionRanges.put("Archer Cost", new Number[]{10, 400, 5});
		optionRanges.put("Archer Upgrade Cost", new Number[]{50, 250, 10});
		optionRanges.put("Archer Range", new Number[]{5.0, 20.0, 0.1});
		optionRanges.put("Archer Fire Rate", new Number[]{1.0, 15.0, 0.1});
		optionRanges.put("Artillery Cost", new Number[]{10, 600, 5});
		optionRanges.put("Artillery Upgrade Cost", new Number[]{150, 400, 10});
		optionRanges.put("Artillery Range", new Number[]{4.0, 10.0, 0.1});
		optionRanges.put("Artillery Fire Rate", new Number[]{0.5, 5.0, 0.1});
		optionRanges.put("Mage Cost", new Number[]{10, 500, 5});
		optionRanges.put("Mage Upgrade Cost", new Number[]{100, 600, 10});
		optionRanges.put("Mage Range", new Number[]{5.0, 15.0, 0.1});
		optionRanges.put("Mage Fire Rate", new Number[]{1.0, 10.0, 0.1});
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

	// LOOKUP THE NAME OF THE OPTION YOU WANT TO SET IT'S ABOVE
	public static void setOption(String name, Number value) {
		Consumer<Number> setter = consumerMap.get(name);
		if (setter != null) {
			setter.accept(value);
		}
	}

	// LOOKUP THE NAME OF THE OPTION YOU WANT TO GET IT'S ABOVE
	public static Number getOption(String name) {
		Supplier<Number> getter = supplierMap.get(name);
		if (getter != null) {
			return getter.get();
		} else return null;
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
