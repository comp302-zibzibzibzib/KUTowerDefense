package ui;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import domain.controller.GameOptionsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// If player exits menu without saving the options still persist during runtime Need Fix
// However if they don't save the options won't persist for other runs, as expected
public class OptionScene {
	private static final String SPRITE_PATH = "/Images/HUD/";
	
	private KuTowerDefenseA app;
	private VBox vbox;
	private Scene scene;
	
	private Image buttonBlue = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb.png"));
	private Image buttonPressed = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb_pressed.png"));
	private Image buttonDisabled = new Image(getClass().getResourceAsStream(SPRITE_PATH + "disableb.png"));
	private Image buttonDisabled3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "disableb3.png"));
	private Image buttonHover = new Image(getClass().getResourceAsStream(SPRITE_PATH + "hoverb.png"));
	private Image buttonHover3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "hoverb3.png"));
	private Image buttonBlue3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb3.png"));
	
	private class OptionHBoxInt extends HBox {
		Label label;
		Label value;
		Button increment;
		Button decrement;
		
		public Supplier<Integer> getter;
		public Consumer<Integer> setter;
		
		public OptionHBoxInt(String text, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int incrementAmount) {
			this.getter = getter;
			this.setter = setter;
			createOption(text, getter, setter, min, max, incrementAmount);
		}
		
		private void createOption(String text, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int incrementAmount) {
			label = new Label(text);
			value = new Label(String.valueOf(getter.get()));
			StackPane labelPane = createLabelStackPane(buttonBlue3, label, 20);
			StackPane valuePane = createLabelStackPane(buttonBlue3, value, 30);
			
			increment = new Button();
			increment.setBackground(null);
			StackPane incrementPane = createButtonStackPane(buttonBlue, buttonHover, buttonPressed, new Label(">"), 30, 50);
			increment.setGraphic(incrementPane);
			decrement = new Button();
			decrement.setBackground(null);
			StackPane decrementPane = createButtonStackPane(buttonBlue, buttonHover, buttonPressed, new Label("<"), 30, 50);
			decrement.setGraphic(decrementPane);
			
			increment.setOnAction(e -> {
				int current = getter.get();
				if (current + incrementAmount <= max) {
					setter.accept(current + incrementAmount);
					value.setText(String.valueOf(getter.get()));
				}
			});
			
			decrement.setOnAction(e -> {
				int current = getter.get();
				if (current - incrementAmount >= min) {
					setter.accept(current - incrementAmount);
					value.setText(String.valueOf(getter.get()));
				}
			});
			
			this.getChildren().addAll(labelPane, decrement, valuePane, increment);
			
			this.setAlignment(Pos.CENTER);
		}
		
		public Label getValueLabel() {
			return value;
		}
	}
	
	private class OptionHBoxDouble extends HBox {
		Label label;
		Label value;
		Button increment;
		Button decrement;
		
		public Supplier<Double> getter;
		public Consumer<Double> setter;
		
		public OptionHBoxDouble(String text, Supplier<Double> getter, Consumer<Double> setter, double min, double max, double incrementAmount) {
			this.getter = getter;
			this.setter = setter;
			createOption(text, getter, setter, min, max, incrementAmount);
		}
		
		private void createOption(String text, Supplier<Double> getter, Consumer<Double> setter, double min, double max, double incrementAmount) {
			label = new Label(text);
			value = new Label(String.valueOf(getter.get()));
			StackPane labelPane = createLabelStackPane(buttonBlue3, label, 20);
			StackPane valuePane = createLabelStackPane(buttonBlue3, value, 30);
			
			increment = new Button();
			increment.setBackground(null);
			StackPane incrementPane = createButtonStackPane(buttonBlue, buttonHover, buttonPressed, new Label(">"), 30, 50);
			increment.setGraphic(incrementPane);
			decrement = new Button();
			decrement.setBackground(null);
			StackPane decrementPane = createButtonStackPane(buttonBlue, buttonHover, buttonPressed, new Label("<"), 30, 50);
			decrement.setGraphic(decrementPane);
			
			increment.setOnAction(e -> {
				double current = getter.get();
				if (current + incrementAmount <= max) {
					setter.accept(current + incrementAmount);
					value.setText(String.valueOf(getter.get()));
				}
			});
			
			decrement.setOnAction(e -> {
				double current = getter.get();
				if (current - incrementAmount >= min) {
					setter.accept(current - incrementAmount);
					value.setText(String.valueOf(getter.get()));
				}
			});
			
			this.getChildren().addAll(labelPane, decrement, valuePane, increment);
			this.setAlignment(Pos.CENTER);
		}
		
		public Label getValueLabel() {
			return value;
		}
	}
	
	public OptionScene(KuTowerDefenseA app, StackPane root) {
		this.app = app;
		initializeScene(root);
	}

	public Scene getScene() {
		return scene;
	}
	
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
	
	private StackPane createLabelStackPane(Image image, Label label, int fontSize) {
		ImageView view = new ImageView(image);
		view.setFitHeight(50);
	    view.setFitWidth(150);
	    
	    Font font = Font.font("Calibri", fontSize);
	    label.setFont(font);
	    
	    StackPane pane = new StackPane();
	    pane.getChildren().addAll(view, label);
	    
	    label.setTranslateY(-5);
	    
	    return pane;
	}
	
	private void initializeScene(StackPane root) {
		Image backgroundImage = new Image(getClass().getResourceAsStream("/Images/options_background.png"));
        ImageView backgroundView = new ImageView(backgroundImage);
        
        backgroundView.setPreserveRatio(false);
        backgroundView.fitWidthProperty().bind(root.widthProperty());
        backgroundView.fitHeightProperty().bind(root.heightProperty());
        
		
		vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));
		
		GameOptionsController.initializeGameOptions();
		vbox.getChildren().add(new OptionHBoxInt("Player Lives", () -> GameOptionsController.getStartingLives(),
				val -> GameOptionsController.setStartingLives(val), 10, 50, 5));
		vbox.getChildren().add(new OptionHBoxInt("Starting Gold", () -> GameOptionsController.getStartingGold(),
				val -> GameOptionsController.setStartingGold(val), 300, 600, 100));
		vbox.getChildren().add(new OptionHBoxDouble("Enemy Speed", () -> GameOptionsController.getEnemySpeed(),
				val -> GameOptionsController.setEnemySpeed(val), 2.0, 8.0, 1.0));
		vbox.getChildren().add(new OptionHBoxInt("Wave Number", () -> GameOptionsController.getNumberOfWaves(),
				val -> GameOptionsController.setNumberOfWaves(val), 5, 20, 5));
		
		Button saveButton = new Button();
		saveButton.setBackground(null);
		saveButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Save"), 20, 100));
		saveButton.setOnAction(e -> {
			GameOptionsController.saveOptions();
		});
		
		Button resetButton = new Button();
		resetButton.setBackground(null);
		resetButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Reset"), 20, 100));
		resetButton.setOnAction(e -> {
			GameOptionsController.resetOptions();
			List<Node> vboxNodes = vbox.getChildren();
			for(Node node : vboxNodes) {
				if (node instanceof OptionHBoxInt) {
					OptionHBoxInt hbox = (OptionHBoxInt) node;
					Label value = hbox.getValueLabel();
					value.setText(String.valueOf(hbox.getter.get()));
				}
			}
		});
		
		Button mainMenuButton = new Button();
		mainMenuButton.setBackground(null);
		mainMenuButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Main Menu"), 20, 150));
		mainMenuButton.setOnAction(e -> {
			app.showMainMenu(new StackPane());
		});
		
		vbox.getChildren().addAll(saveButton, resetButton, mainMenuButton);
		
		root.getChildren().addAll(backgroundView, vbox);
		
		scene = new Scene(root, app.getPrimaryStage().getWidth(), app.getPrimaryStage().getHeight() - 27);
	}
}
