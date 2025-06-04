package ui;

import domain.controller.GameOptionsController;
import domain.controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainMenuScene {
	private static final String SPRITE_PATH = "/Images/HUD/";
	
	private Image buttonHover3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "hoverb3.png"));
	private Image buttonBlue3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb3.png"));
	
	private class MenuButton extends Button {
		public MenuButton(MainMenuScene scene) {
			this.setBackground(null);
			this.setOnAction(scene::processButtonEvents);
		}
	}
	
	private KuTowerDefenseA app;
    private Button newGameButton;
	private Button mapEditorButton;
	private Button optionsButton;
    private Button quitGameButton;
    private Scene scene;
    
    private StackPane createButtonStackPane(Image image, Image hoverImage, Image clickedImage, Label label, int fontSize, int width) {
		ImageView view = new ImageView(image);
		view.setFitHeight(50);
	    view.setFitWidth(width);
	    
	    Font font = Font.font("Calibri", FontWeight.BOLD, fontSize);
	    label.setFont(font);
	    
	    StackPane pane = new StackPane();
	    pane.getChildren().addAll(view, label);
	    
	    label.setTranslateY(-5);
	    
	    pane.setOnMouseEntered(e -> { view.setImage(hoverImage); });
	    pane.setOnMouseExited(e -> { view.setImage(image); });
	    
	    pane.setOnMousePressed(e -> { view.setImage(clickedImage); });
	    pane.setOnMouseReleased(e -> { 
	    	if (pane.isHover()) {
	    		view.setImage(hoverImage); 
	    	} else {
	    		view.setImage(image); 
	    	}
	    });
	    
	    return pane;
	}
	
    
    public MainMenuScene(KuTowerDefenseA app, StackPane root) {
		this.app = app;
		newGameButton = new MenuButton(this);
		newGameButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("New Game"), 20, 150));
		mapEditorButton = new MenuButton(this);
		mapEditorButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Map Editor"), 20, 150));
		mapEditorButton.setTranslateY(50);
		optionsButton = new MenuButton(this);
		optionsButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Options"), 20, 150));
		optionsButton.setTranslateY(100);
		quitGameButton = new MenuButton(this);
		quitGameButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Quit Game"), 20, 150));
		quitGameButton.setTranslateY(150);
		this.scene = initScene(root);
	}
    
    public Scene getScene() { return scene; }
    
	private Scene initScene(StackPane root) {
		Image image = new Image(getClass().getResourceAsStream("/Images/MainMenuImage.png"));
		ImageView mv = new ImageView(image);
		
		mv.setPreserveRatio(false);
		mv.fitWidthProperty().bind(root.widthProperty());
        mv.fitHeightProperty().bind(root.heightProperty());
        
		root.getChildren().addAll(mv);
		root.getChildren().addAll(newGameButton, mapEditorButton, optionsButton, quitGameButton);
		Scene scene = new Scene(root);

		return scene;
	}
	
	private void processButtonEvents(ActionEvent event) {
		if(event.getSource() == newGameButton) {
			MainMenuController.startNewGame("Multiple Way Map");
			app.startGame();
		} else if (event.getSource() == mapEditorButton) {
			// Begüm buraya bir şeyler yazar mısın pls
		} else if (event.getSource() == optionsButton) {
			GameOptionsController.initializeGameOptions();
			app.showOptionsMenu(new StackPane());
		} else if (event.getSource() == quitGameButton) {
			MainMenuController.quitGame();
		}
	}
}
