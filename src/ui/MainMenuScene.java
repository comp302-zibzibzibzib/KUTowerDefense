package ui;

import domain.controller.GameOptionsController;
import domain.controller.MainMenuController;
import domain.map.Map;
import domain.services.Utilities;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenuScene {
	
	private class MenuButton extends Button {
		public MenuButton(String text, MainMenuScene scene) {
			this.setText(text);
			this.setBackground(null);
			this.setOnAction(scene::processButtonEvents);
		}
	}
	
	private KuTowerDefenseA app;
    private Button newGameButton;
    private Button optionsButton;
    private Scene scene;
	
    
    public MainMenuScene(KuTowerDefenseA app, StackPane root) {
		this.app = app;
		newGameButton = new MenuButton("New Game", this);
		optionsButton = new MenuButton("Options", this);
		optionsButton.setTranslateY(40);
		this.scene = initScene(root);
	}
    
    public Scene getScene() { return scene; }
    
	private Scene initScene(StackPane root) {
		Image image = new Image(getClass().getResourceAsStream("/Images/MainMenuImage.png"));
		ImageView mv = new ImageView(image);
		
		mv.setPreserveRatio(true);
		mv.fitWidthProperty().bind(root.widthProperty());
        mv.fitHeightProperty().bind(root.heightProperty());
        
		root.getChildren().addAll(mv);
		root.getChildren().addAll(newGameButton, optionsButton);
		Scene scene = new Scene(root);

		return scene;
	}
	
	private void processButtonEvents(ActionEvent event) {
		if(event.getSource() == newGameButton) {
			MainMenuController.startNewGame("Pre-Built Map");
			app.startGame();
		} else if (event.getSource() == optionsButton) {
			GameOptionsController.initializeGameOptions();
			app.showOptionsMenu(new StackPane());
		}
		
	}

}
