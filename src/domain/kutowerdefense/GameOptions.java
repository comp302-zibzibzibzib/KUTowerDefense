package domain.kutowerdefense;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import domain.services.Utilities;
import domain.entities.Group;


public class GameOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String OPTIONS_FILE = "game_options.dat";
    private static GameOptions instance;

    // Core game settings
    private int difficultyLevel;      // 0 = Easy, 1 = Medium, 2 = Hard
    private boolean fullscreen;
    private double gameSpeed;         // Speed multiplier (1.0 = normal)

    // Game mechanics and parameters
    private double goblinSpeed;
    private double knightSpeed;
    private int goblinReward;
    private int knightReward;
    private double archerTowerRange;
    private double mageTowerRange;
    private double artilleryTowerRange;
    private double artilleryAoeRange;
    private double goblinSpellDefenseMult;
    private double knightArrowDefenseMult;
    private double gracePeriod;
    private int numberOfWaves;
    private List<Group> groupCompositions; // List of enemy group configurations
    private int goblinHitPoints;
    private int knightHitPoints;
    private int playerHitPoints;
    private int archerCost;
    private int mageCost;
    private int artilleryCost;
    private double towerRefundPercentage;
    private double arrowDamage;
    private double artilleryDamage;
    private double spellDamage;
    private double gameDifficultyFactor;

    /** Private constructor initializes all fields to default values. */
    private GameOptions() {
        // Default core settings
        this.difficultyLevel = 1;
        this.fullscreen = false;
        this.gameSpeed = 1.0;

        // Default game parameters
        this.goblinSpeed = 1.0;
        this.knightSpeed = 1.0;
        this.goblinReward = 10;
        this.knightReward = 20;
        this.archerTowerRange = 5.0;
        this.mageTowerRange = 4.0;
        this.artilleryTowerRange = 6.0;
        this.artilleryAoeRange = 3.0;
        this.goblinSpellDefenseMult = 0.5;
        this.knightArrowDefenseMult = 0.5;
        this.gracePeriod = 5.0;
        this.numberOfWaves = 10;
        this.groupCompositions = new ArrayList<>();
        this.goblinHitPoints = 100;
        this.knightHitPoints = 150;
        this.playerHitPoints = 20;
        this.archerCost = 100;
        this.mageCost = 150;
        this.artilleryCost = 200;
        this.towerRefundPercentage = 0.75;
        this.arrowDamage = 10.0;
        this.artilleryDamage = 50.0;
        this.spellDamage = 30.0;
        this.gameDifficultyFactor = 1.0;
    }

    /** Returns the singleton instance, loading saved options if available. */
    public static synchronized GameOptions getInstance() {
        if (instance == null) {
            Object obj = Utilities.readObject(OPTIONS_FILE);
            if (obj instanceof GameOptions) {
                instance = (GameOptions) obj;
            } else {
                instance = new GameOptions();
            }
        }
        return instance;
    }

    /** Resets all options to defaults and saves immediately. */
    public void resetOptions() {
        instance = new GameOptions();
        writeOptions();
    }

    /** Saves current options to a serialized file via Utilities. */
    public void writeOptions() {
        Utilities.writeObject(OPTIONS_FILE, this);
    }

    /** Loads options from serialized file via Utilities. If unavailable, defaults remain. */
    public void readOptions() {
        Object obj = Utilities.readObject(OPTIONS_FILE);
        if (obj instanceof GameOptions) {
            GameOptions loaded = (GameOptions) obj;
            // Copy all fields from loaded
            this.difficultyLevel       = loaded.difficultyLevel;
            this.fullscreen            = loaded.fullscreen;
            this.gameSpeed             = loaded.gameSpeed;
            this.goblinSpeed           = loaded.goblinSpeed;
            this.knightSpeed           = loaded.knightSpeed;
            this.goblinReward          = loaded.goblinReward;
            this.knightReward          = loaded.knightReward;
            this.archerTowerRange      = loaded.archerTowerRange;
            this.mageTowerRange        = loaded.mageTowerRange;
            this.artilleryTowerRange   = loaded.artilleryTowerRange;
            this.artilleryAoeRange     = loaded.artilleryAoeRange;
            this.goblinSpellDefenseMult = loaded.goblinSpellDefenseMult;
            this.knightArrowDefenseMult = loaded.knightArrowDefenseMult;
            this.gracePeriod           = loaded.gracePeriod;
            this.numberOfWaves         = loaded.numberOfWaves;
            this.groupCompositions.clear();
            this.groupCompositions.addAll(loaded.groupCompositions);
            this.goblinHitPoints       = loaded.goblinHitPoints;
            this.knightHitPoints       = loaded.knightHitPoints;
            this.playerHitPoints       = loaded.playerHitPoints;
            this.archerCost            = loaded.archerCost;
            this.mageCost              = loaded.mageCost;
            this.artilleryCost         = loaded.artilleryCost;
            this.towerRefundPercentage = loaded.towerRefundPercentage;
            this.arrowDamage           = loaded.arrowDamage;
            this.artilleryDamage       = loaded.artilleryDamage;
            this.spellDamage           = loaded.spellDamage;
            this.gameDifficultyFactor  = loaded.gameDifficultyFactor;
        }
    }

    // --- Getters and Setters for all fields ---

    public int getDifficultyLevel()                         { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel)     { if (difficultyLevel >= 0 && difficultyLevel <= 2) this.difficultyLevel = difficultyLevel; }

    public boolean isFullscreen()                          { return fullscreen; }
    public void setFullscreen(boolean fullscreen)          { this.fullscreen = fullscreen; }

    public double getGameSpeed()                           { return gameSpeed; }
    public void setGameSpeed(double gameSpeed)             { this.gameSpeed = Math.max(0.5, Math.min(3.0, gameSpeed)); }

    public double getGoblinSpeed()                         { return goblinSpeed; }
    public void setGoblinSpeed(double goblinSpeed)         { this.goblinSpeed = goblinSpeed; }

    public double getKnightSpeed()                         { return knightSpeed; }
    public void setKnightSpeed(double knightSpeed)         { this.knightSpeed = knightSpeed; }

    public int getGoblinReward()                           { return goblinReward; }
    public void setGoblinReward(int goblinReward)          { this.goblinReward = goblinReward; }

    public int getKnightReward()                           { return knightReward; }
    public void setKnightReward(int knightReward)          { this.knightReward = knightReward; }

    public double getArcherTowerRange()                    { return archerTowerRange; }
    public void setArcherTowerRange(double archerTowerRange) { this.archerTowerRange = archerTowerRange; }

    public double getMageTowerRange()                      { return mageTowerRange; }
    public void setMageTowerRange(double mageTowerRange)   { this.mageTowerRange = mageTowerRange; }

    public double getArtilleryTowerRange()                 { return artilleryTowerRange; }
    public void setArtilleryTowerRange(double artilleryTowerRange) { this.artilleryTowerRange = artilleryTowerRange; }

    public double getArtilleryAoeRange()                   { return artilleryAoeRange; }
    public void setArtilleryAoeRange(double artilleryAoeRange) { this.artilleryAoeRange = artilleryAoeRange; }

    public double getGoblinSpellDefenseMult()              { return goblinSpellDefenseMult; }
    public void setGoblinSpellDefenseMult(double goblinSpellDefenseMult) { this.goblinSpellDefenseMult = goblinSpellDefenseMult; }

    public double getKnightArrowDefenseMult()              { return knightArrowDefenseMult; }
    public void setKnightArrowDefenseMult(double knightArrowDefenseMult) { this.knightArrowDefenseMult = knightArrowDefenseMult; }

    public double getGracePeriod()                         { return gracePeriod; }
    public void setGracePeriod(double gracePeriod)         { this.gracePeriod = gracePeriod; }

    public int getNumberOfWaves()                          { return numberOfWaves; }
    public void setNumberOfWaves(int numberOfWaves)        { this.numberOfWaves = numberOfWaves; }

    public List<Group> getGroupCompositions()              { return new ArrayList<>(groupCompositions); }
    public void setGroupCompositions(List<Group> groupCompositions) { this.groupCompositions = new ArrayList<>(groupCompositions); }

    public int getGoblinHitPoints()                        { return goblinHitPoints; }
    public void setGoblinHitPoints(int goblinHitPoints)    { this.goblinHitPoints = goblinHitPoints; }

    public int getKnightHitPoints()                        { return knightHitPoints; }
    public void setKnightHitPoints(int knightHitPoints)    { this.knightHitPoints = knightHitPoints; }

    public int getPlayerHitPoints()                        { return playerHitPoints; }
    public void setPlayerHitPoints(int playerHitPoints)    { this.playerHitPoints = playerHitPoints; }

    public int getArcherCost()                             { return archerCost; }
    public void setArcherCost(int archerCost)              { this.archerCost = archerCost; }

    public int getMageCost()                               { return mageCost; }
    public void setMageCost(int mageCost)                  { this.mageCost = mageCost; }

    public int getArtilleryCost()                          { return artilleryCost; }
    public void setArtilleryCost(int artilleryCost)        { this.artilleryCost = artilleryCost; }

    public double getTowerRefundPercentage()               { return towerRefundPercentage; }
    public void setTowerRefundPercentage(double towerRefundPercentage) { this.towerRefundPercentage = towerRefundPercentage; }

    public double getArrowDamage()                         { return arrowDamage; }
    public void setArrowDamage(double arrowDamage)         { this.arrowDamage = arrowDamage; }

    public double getArtilleryDamage()                     { return artilleryDamage; }
    public void setArtilleryDamage(double artilleryDamage) { this.artilleryDamage = artilleryDamage; }

    public double getSpellDamage()                         { return spellDamage; }
    public void setSpellDamage(double spellDamage)         { this.spellDamage = spellDamage; }

    public double getGameDifficultyFactor()                { return gameDifficultyFactor; }
    public void setGameDifficultyFactor(double gameDifficultyFactor) { this.gameDifficultyFactor = gameDifficultyFactor; }
}
