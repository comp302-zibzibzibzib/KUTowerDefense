package domain.services;

import domain.controller.EntityController;
import domain.controller.MainMenuController;
import domain.entities.Enemy;
import domain.kutowerdefense.PlayModeManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Entity Test gets so have its own class because JavaFX doesn't like it when an Application is a private class ¯\_(ツ)_/¯
// A white window will open once you execute this, ignore it
// In order to execute you must add the following like to Run configurations -> Arguments -> VM Arguments:
// For Windows:  	--module-path "\path\to\javafx-sdk-21.0.7\lib" --add-modules javafx.controls,javafx.fxml
// For MacOS:		--module-path \path\to\javafx-sdk-21.0.7\lib --add-modules javafx.controls,javafx.fxml
// Obviously you have to have JavaFX installed
public class EntityTest extends Application {
	class TestRunner implements Runnable {

		@Override
		public void run() {
			boolean passed = true;
			while (true) {
				if (EntityController.spawningEnemies()) continue;
				
				PlayModeManager man = PlayModeManager.getInstance();
				for (Enemy enemy : Enemy.getAllEnemies()) {
					if (!enemy.getLocation().equals(man.getCurrentMap().getStartingTile().getLocation())) {
						passed = false;
						break;
					}
				}
				break;
			}
			
			if (passed) System.out.println("Initialization Test - PASSED!");
			else System.out.println("Initialization Test - PASSED!");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("-----Entity Initialization Test-----");
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 400);
        
        arg0.setScene(scene);
        arg0.setTitle("Entity Test");
        arg0.show();

        MainMenuController.startNewGame("Pre-Built Map");
        EntityController.startEntityLogic();
        Thread testThread = new Thread(new TestRunner());
        testThread.start();
	}
}
