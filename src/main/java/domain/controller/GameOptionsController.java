package domain.controller;

import domain.kutowerdefense.GameOptions;
import domain.services.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameOptionsController {
	private static final List<String> optionNames = Arrays.asList("Wave Number", "Max Group per Wave",
			"Min Group per Wave", "Max Enemies per Group", "Min Enemies per Group", "Wave Delay", "Group Delay", "Enemy Delay",
			"Knight Percentage", "Goblin Percentage", "Player Gold", "Player Lives", "Goblin Health", "Goblin Speed", "Goblin Reward",
			"Knight Health", "Knight Speed", "Knight Reward", "Arrow Damage", "Artillery Damage", "Spell Damage", "Archer Cost",
			"Artillery Cost", "Mage Cost", "Archer Range", "Artillery Range", "Mage Range", "AOE Range");

	private static final HashMap<String, Number[]> optionRanges = new HashMap<>();

	static {
		optionRanges.put("Wave Number", new Number[]{1, 20, 1});
		optionRanges.put("Max Group per Wave", new Number[]{5, 10, 1});
		optionRanges.put("Min Group per Wave", new Number[]{1, 5, 1});
		optionRanges.put("Max Enemies per Group", new Number[]{5, 10, 1});
		optionRanges.put("Min Enemies per Group", new Number[]{2, 5, 1});
		optionRanges.put("Wave Delay", new Number[]{5.0, 10.0, 0.1});
		optionRanges.put("Group Delay", new Number[]{2.0, 4.0, 0.1});
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
		optionRanges.put("Spell Damage", new Number[]{15.0, 25.0, 5.0});
		optionRanges.put("Archer Cost", new Number[]{10, 200, 5});
		optionRanges.put("Artillery Cost", new Number[]{10, 300, 5});
		optionRanges.put("Mage Cost", new Number[]{10, 400, 5});
		optionRanges.put("Archer Range", new Number[]{1.0, 10.0, 0.1});
		optionRanges.put("Artillery Range", new Number[]{1.0, 10.0, 0.1});
		optionRanges.put("Mage Range", new Number[]{1.0, 10.0, 0.1});
		optionRanges.put("AOE Range", new Number[]{0.5, 3.0, 0.5});
	}

	private static final HashMap<String, Field> fieldMap = new HashMap<>();
	private static GameOptions options = GameOptions.getInstance();

	public static void initializeGameOptions() {
		GameOptions.initializeGameOptions();
		options = GameOptions.getInstance();
		int i = 0;
		for (Field field : GameOptions.class.getDeclaredFields()) {
			if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			fieldMap.put(optionNames.get(i++), field);
		}
 	}

	public static Field getGameOptionField(String name) {
		return fieldMap.get(name);
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

	public static void setGameOptionsField(Field field, Object value) {
		try {
			field.set(GameOptions.getInstance(), value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static HashMap<String, Field> getOptionFieldMap() {
		return fieldMap;
	}
}
