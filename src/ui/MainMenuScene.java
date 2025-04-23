package ui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenuScene {
	private KuTowerDefenseA app;
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
        
        Button button = new Button();
		
		root.getChildren().addAll(mv);
		Scene scene = new Scene(root);


		
		
		return scene;
		
	}

}
