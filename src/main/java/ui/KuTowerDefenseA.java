package ui;
import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class KuTowerDefenseA  extends Application{
	private Image cursorImage;
	private ImageCursor cursor;
	private Stage primaryStage = new Stage();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		cursorImage = new Image(getClass().getResourceAsStream("/Images/HUD/cursor.png"));
		cursor = new ImageCursor(cursorImage, cursorImage.getWidth()/2, cursorImage.getHeight()/2);
		this.primaryStage = primaryStage;
		StackPane root = new StackPane();
		showMainMenu(root);
		
	}
	public void showMainMenu(StackPane root) {
		MainMenuScene menuScene = new MainMenuScene(this, root);
        Scene mainMenuScene = menuScene.getScene();
		mainMenuScene.setCursor(cursor);
		primaryStage.setResizable(true);
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("KU Tower Defense");
        primaryStage.show();
    }
    public void showOptionsMenu(StackPane root) {
    	OptionScene options = new OptionScene(this, root);
		options.getScene().setCursor(cursor);
		primaryStage.setResizable(true);
        primaryStage.setScene(options.getScene());
        primaryStage.show();
    }
    public void showMapEditor() {
    	MapEditorScene mapEditor = new MapEditorScene(this);
    	Scene editor = mapEditor.getScene();
		editor.setCursor(cursor);
    	primaryStage.setScene(editor);
    	primaryStage.setResizable(false);
    	
    }
    public void startGame() {
    	PlayModeScene playableGame = new PlayModeScene(this);
    	Scene game = playableGame.getScene();
		game.setCursor(cursor);
    	primaryStage.setScene(game);
    	primaryStage.setResizable(false);
    }
    
	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
