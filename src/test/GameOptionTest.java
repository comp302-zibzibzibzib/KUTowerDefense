package domain.services;

import java.io.File;
import domain.kutowerdefense.GameOptions;

/**
 * GameOptionsTest:
 *
 * This class checks that GameOptions can save (serialize) and reload (deserialize)
 * user settings correctly.
 *
 * Steps:
 *   1) Delete Data/Options/user_options.ser if it exists.
 *   2) Call GameOptions.initializeGameOptions() – since no file exists, defaults load.
 *      Verify default values: lives=20, gold=200, waves=10, speed=4.0.
 *   3) Change those values in memory and call Utilities.writeOptions() to save.
 *   4) Overwrite the in-memory values with wrong numbers.
 *   5) Call GameOptions.initializeGameOptions() again – now it should read
 *      the saved file. Verify that lives=50, gold=500, waves=20, speed=7.5.
 *   6) If all checks pass, print “GameOptionsTest PASSED”; otherwise print “FAILED”.
 *
 * To run: Right-click this file → Run As → Java Application
 * Check the Console for “GameOptionsTest PASSED”.
 */
public class GameOptionsTest {

    private static final String USER_OPTIONS_PATH = "Data/Options/user_options.ser";

    public static void main(String[] args) {
        boolean ok = true;

        // 1) Delete existing file
        File f = new File(USER_OPTIONS_PATH);
        if (f.exists() && !f.delete()) {
            System.err.println("WARNING: Could not delete existing user_options.ser");
        }

        try {
            // 2) Load defaults (no file on disk)
            GameOptions.initializeGameOptions();
            GameOptions opts = GameOptions.getInstance();

            // Verify default values
            if (opts.getStartingPlayerLives() != 20) {
                System.out.println("FAIL (default lives): expected=20, actual=" + opts.getStartingPlayerLives());
                ok = false;
            }
            if (opts.getStartingPlayerGold() != 200) {
                System.out.println("FAIL (default gold): expected=200, actual=" + opts.getStartingPlayerGold());
                ok = false;
            }
            if (opts.getNumberOfWaves() != 10) {
                System.out.println("FAIL (default waves): expected=10, actual=" + opts.getNumberOfWaves());
                ok = false;
            }
            if (Math.abs(opts.getEnemySpeed() - 4.0) > 1e-6) {
                System.out.println("FAIL (default speed): expected=4.0, actual=" + opts.getEnemySpeed());
                ok = false;
            }

            // 3) Change values and save to disk
            opts.setStartingPlayerLives(50);
            opts.setStartingPlayerGold(500);
            opts.setNumberOfWaves(20);
            opts.setEnemySpeed(7.5);
            Utilities.writeOptions();

            // 4) Overwrite in-memory values
            opts.setStartingPlayerLives(1);
            opts.setStartingPlayerGold(1);
            opts.setNumberOfWaves(1);
            opts.setEnemySpeed(1.1);

            // 5) Reload from disk
            GameOptions.initializeGameOptions();
            GameOptions reloaded = GameOptions.getInstance();

            // Verify saved values
            if (reloaded.getStartingPlayerLives() != 50) {
                System.out.println("FAIL (loaded lives): expected=50, actual=" + reloaded.getStartingPlayerLives());
                ok = false;
            }
            if (reloaded.getStartingPlayerGold() != 500) {
                System.out.println("FAIL (loaded gold): expected=500, actual=" + reloaded.getStartingPlayerGold());
                ok = false;
            }
            if (reloaded.getNumberOfWaves() != 20) {
                System.out.println("FAIL (loaded waves): expected=20, actual=" + reloaded.getNumberOfWaves());
                ok = false;
            }
            if (Math.abs(reloaded.getEnemySpeed() - 7.5) > 1e-6) {
                System.out.println("FAIL (loaded speed): expected=7.5, actual=" + reloaded.getEnemySpeed());
                ok = false;
            }

        } catch (Exception e) {
            System.out.println("TEST ERROR: " + e.getClass().getSimpleName() + " → " + e.getMessage());
            ok = false;
        }

        // 6) Print final result
        if (ok) {
            System.out.println("GameOptionsTest PASSED");
        } else {
            System.out.println("GameOptionsTest FAILED");
        }
    }
}
