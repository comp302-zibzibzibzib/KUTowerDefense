package ui;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class KuTowerDefenseA  extends Application{
	private Stage primaryStage = new Stage();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		showMainMenu(root);
		
	}
	public void showMainMenu(StackPane root) {
        Scene mainMenuScene = new MainMenuScene(this).getScene(root);
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("KU Tower Defense");
        primaryStage.show();
    }
    public void showOptionsMenu() {
    	
    }
    public void showMapEditor() {
    	
    }
    public void startGame() {
    	
    }
	
	

}
