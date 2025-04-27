package ui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class KuTowerDefenseA  extends Application{
	private Stage primaryStage = new Stage();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
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
    	MapEditorScene mapEditor = new MapEditorScene(this);
    	Scene editor = mapEditor.getScene();
    	primaryStage.setScene(editor);
    	primaryStage.setResizable(false);
    	
    }
    public void startGame() {
    	PlayModeScene playableGame = new PlayModeScene(this);
    	Scene game = playableGame.getScene();
    	primaryStage.setScene(game);
    	primaryStage.setResizable(false);
    	
    	
    }
	
	

}
