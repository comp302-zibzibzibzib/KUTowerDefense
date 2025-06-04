package domain.entities;

import java.util.ArrayList;

import domain.kutowerdefense.PlayModeManager;
import domain.map.Location;
import domain.services.Utilities;
import domain.tower.AttackType;
import domain.tower.Tower;

public class Boss extends Enemy{

    private double spellDamageReduction;
    private double arrowDamageReduction;
    private double artilleryDamageIncrease;
    private double timeSinceLastFreezeAttempt;
    private ArrayList<Tower> towersInRange;

    private static double freezeChance = 0.4;//can be put somewhere else
    private static double freezeAttemptInterval = 4.0;

    public Boss(double hitPoints, double speed, Location location, double spellDamageReduction, double arrowDamageReduction, double artilleryDamageIncrease) {
        super(hitPoints, speed, location);
        this.spellDamageReduction = spellDamageReduction;
        this.arrowDamageReduction = arrowDamageReduction;
        this.artilleryDamageIncrease = artilleryDamageIncrease;
        this.timeSinceLastFreezeAttempt = 0.0;
        this.towersInRange = new ArrayList<Tower>();

    }

    public void freezeTower() { //try to freeze if successful freeze one of the towers that can attack the boss
        if(Utilities.globalRNG.nextDouble() <= freezeChance) {
            for(Tower tower : PlayModeManager.getInstance().getCurrentMap().getTowerList()) {
                if(Utilities.euclideanDistance(this.location, tower.getLocation()) <= tower.getRange()) {
                    towersInRange.add(tower);
                }
            }
            towersInRange.get(Utilities.globalRNG.nextInt(towersInRange.size())).setIsFrozen(true);
            towersInRange.clear();
        }
    }

    @Override
    public void hitEnemy(double damage, AttackType attackType) {
        double modifiedDamage = damage;
        if (attackType == AttackType.ARROW) modifiedDamage = (1 - arrowDamageReduction) * modifiedDamage;
        if (attackType == AttackType.SPELL || attackType == AttackType.SLOW_SPELL) modifiedDamage = (1 - spellDamageReduction) * modifiedDamage;
        if (attackType == AttackType.ARTILLERY) modifiedDamage = artilleryDamageIncrease * modifiedDamage;
        super.hitEnemy(modifiedDamage, attackType);
    }

    @Override
    public void updateEnemy(double deltaTime) {
        super.updateEnemy(deltaTime);
        this.timeSinceLastFreezeAttempt += deltaTime;
        if(this.timeSinceLastFreezeAttempt >= freezeAttemptInterval) {
            this.freezeTower();
            this.timeSinceLastFreezeAttempt = 0.0;
        }
    }

    public double getSpellDamageReduction() {
        return spellDamageReduction;
    }

    public void setSpellDamageReduction(double spellDamageReduction) {
        this.spellDamageReduction = spellDamageReduction;
    }

    public double getArrowDamageReduction() {
        return arrowDamageReduction;
    }

    public void setArrowDamageReduction(double arrowDamageReduction) {
        this.arrowDamageReduction = arrowDamageReduction;
    }
}