package ui;

import java.util.function.Consumer;
import java.util.function.Supplier;

import domain.controller.GameOptionsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

// If player exits menu without saving the options still persist during runtime Need Fix
// However if they don't save the options won't persist for other runs, as expected
public class OptionScene {
	private KuTowerDefenseA app;
	private VBox vbox;
	private Scene scene;
	
	public OptionScene(KuTowerDefenseA app, StackPane root) {
		this.app = app;
		initializeScene(root);
	}

	public Scene getScene() {
		return scene;
	}
	
	private HBox createOption(String text, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int incrementAmount) {
		Label label = new Label(text);
		Label value = new Label(String.valueOf(getter.get()));
		
		Button increment = new Button("+");
		Button decrement = new Button("-");
		
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
		
		HBox option = new HBox(10, label, decrement, value, increment);
		option.setAlignment(Pos.CENTER);
		return option;
	}
	
	private HBox createOption(String text, Supplier<Double> getter, Consumer<Double> setter, double min, double max, double incrementAmount) {
		Label label = new Label(text);
		Label value = new Label(String.valueOf(getter.get()));
		
		Button increment = new Button("+");
		Button decrement = new Button("-");
		
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
		
		HBox option = new HBox(10, label, decrement, value, increment);
		option.setAlignment(Pos.CENTER);
		return option;
	}
	
	private void initializeScene(StackPane root) {
		Image backgroundImage = new Image(getClass().getResourceAsStream("/Images/MainMenuImage.png"));
        ImageView backgroundView = new ImageView(backgroundImage);
        
        backgroundView.setPreserveRatio(true);
        backgroundView.fitWidthProperty().bind(root.widthProperty());
        backgroundView.fitHeightProperty().bind(root.heightProperty());
        
		
		vbox = new VBox(15);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));
		
		vbox.getChildren().add(createOption("Player Lives", () -> GameOptionsController.getStartingLives(),
				val -> GameOptionsController.setStartingLives(val), 10, 50, 5));
		vbox.getChildren().add(createOption("Starting Gold", () -> GameOptionsController.getStartingGold(),
				val -> GameOptionsController.setStartingGold(val), 300, 600, 100));
		vbox.getChildren().add(createOption("Enemy Speed", () -> GameOptionsController.getEnemySpeed(),
				val -> GameOptionsController.setEnemySpeed(val), 2.0, 8.0, 1.0));
		vbox.getChildren().add(createOption("Wave Number", () -> GameOptionsController.getNumberOfWaves(),
				val -> GameOptionsController.setNumberOfWaves(val), 5, 20, 5));
		
		Button saveButton = new Button("Save");
		saveButton.setOnAction(e -> {
			GameOptionsController.saveOptions();
		});
		
		Button mainMenuButton = new Button("Main Menu");
		mainMenuButton.setOnAction(e -> {
			app.showMainMenu(new StackPane());
		});
		
		vbox.getChildren().addAll(saveButton, mainMenuButton);
		
		root.getChildren().addAll(backgroundView, vbox);
		
		scene = new Scene(root, app.getPrimaryStage().getWidth(), app.getPrimaryStage().getHeight() - 27);
	}
}
