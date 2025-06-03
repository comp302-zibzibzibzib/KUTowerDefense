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
		MainMenuScene menuScene = new MainMenuScene(this, root);
        Scene mainMenuScene = menuScene.getScene();
		primaryStage.setResizable(true);
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("KU Tower Defense");
		primaryStage.setMinWidth(700);
		primaryStage.setMinHeight(600);
		primaryStage.setWidth(700);
		primaryStage.setHeight(600);
        primaryStage.show();
    }
    public void showOptionsMenu(StackPane root) {
    	OptionScene options = new OptionScene(this, root);
		primaryStage.setResizable(true);
        primaryStage.setScene(options.getScene());
		primaryStage.setMinWidth(700);
		primaryStage.setMinHeight(700);
		primaryStage.setWidth(700);
		primaryStage.setHeight(700);
        primaryStage.show();
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
		primaryStage.setMinWidth(0);
		primaryStage.setMinHeight(0);
		primaryStage.sizeToScene();
    	primaryStage.setResizable(false);
    }
    
	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
