package ui;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenuScene {
	private KuTowerDefenseA app;
    private Button newGameButton = new Button();

	public MainMenuScene(KuTowerDefenseA app) {
		// TODO Auto-generated constructor stub
		this.app = app;
	}
	public Scene getScene(StackPane root) {
		Image image = new Image(getClass().getResourceAsStream("/Images/MainMenuImage.png"));
		ImageView mv = new ImageView(image);
		
		mv.setPreserveRatio(true);
		mv.fitWidthProperty().bind(root.widthProperty());
        mv.fitHeightProperty().bind(root.heightProperty());
        
        newGameButton.setText("New Game");
        newGameButton.setBackground(null);
        newGameButton.setOnAction(this::processButtonEvents);
        
		
		root.getChildren().addAll(mv);
		root.getChildren().add(newGameButton);
		Scene scene = new Scene(root);

		return scene;
		
	}
	
	private void processButtonEvents(ActionEvent event) {
		if(event.getSource() == newGameButton) {
			System.out.println("damn");
			
		}
	}

}
