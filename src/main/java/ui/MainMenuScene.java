package ui;

import domain.controller.GameOptionsController;
import domain.controller.MainMenuController;
import domain.controller.MapEditorController;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainMenuScene {
	private static final String SPRITE_PATH = "/Images/HUD/";
	
	private Image buttonHover3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "hoverb3.png"));
	private Image buttonBlue3 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb3.png"));
	private Pane mapSelectionOverlay;
	StackPane root = new StackPane();
	private Image buttonBlue = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb5.png"));
	private Image rightButton = new Image(getClass().getResourceAsStream(SPRITE_PATH + "rightbutton.png"));
	private Image leftButton = new Image(getClass().getResourceAsStream(SPRITE_PATH + "leftbutton.png"));
	private List<Image> snapshotImages = new ArrayList<>();
	private List<String> snapshotNames = new ArrayList<>();
	private int currentSnapshotIndex = 0;
	private ImageView mapPreview;
	private Image buttonBlue3v2 = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb3.png"));
	private Label mapNameLabel;

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
		this.root = root;
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

	private void showMapSelectionOverlay() {
		loadMapSnapshotButtons();

		if (mapSelectionOverlay == null) {
			mapSelectionOverlay = new Pane();
			mapSelectionOverlay.setPrefSize(1120, 630);
			mapSelectionOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

			Pane selectionBox = new Pane();
			selectionBox.setPrefSize(500, 300);
			selectionBox.layoutXProperty().bind(root.widthProperty().subtract(500).divide(2));
			selectionBox.layoutYProperty().bind(root.heightProperty().subtract(300).divide(2));
			selectionBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

			Label title = new Label("Select a Map");
			title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
			title.setTextFill(Color.BLUE);
			title.setLayoutX((500 - 150) / 2.0);
			title.setLayoutY(5);

			Group group = new Group();
			ImageView confirmButton = new ImageView(buttonBlue3v2);
			confirmButton.setFitWidth(200);
			confirmButton.setFitHeight(70);
			confirmButton.setLayoutX(((350 - 120) / 2.0) + 40);
			confirmButton.setLayoutY(230);

			Label confirm = new Label("Confirm");
			confirm.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
			confirm.setTextFill(Color.WHITE);
			confirm.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
			confirm.setLayoutX(((350 - 120) / 2.0) + 85);
			confirm.setLayoutY(235);
			group.getChildren().addAll(confirmButton, confirm);
			group.setOnMouseClicked(e -> {
				if (!snapshotNames.isEmpty()) {
					String selectedMap = snapshotNames.get(currentSnapshotIndex);
					MainMenuController.startNewGame(selectedMap);
					app.startGame();
					root.getChildren().remove(mapSelectionOverlay);
				}
			});
			ImageView blueBView = new ImageView(buttonBlue);
			blueBView.setFitWidth(350);
			blueBView.setFitHeight(200);
			blueBView.setLayoutX(80);
			blueBView.setLayoutY(30);

			mapPreview = new ImageView();
			mapPreview.setFitWidth(280);
			mapPreview.setFitHeight(112);
			mapPreview.setLayoutX(115);
			mapPreview.setLayoutY(70);

			Rectangle previewClip = new Rectangle(280, 112);
			previewClip.setArcWidth(30);
			previewClip.setArcHeight(30);
			mapPreview.setClip(previewClip);

			if (!snapshotImages.isEmpty()) {
				mapPreview.setImage(snapshotImages.get(0));
			}

			mapNameLabel = new Label(snapshotNames.isEmpty() ? "" : snapshotNames.get(0));
			mapNameLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
			mapNameLabel.setTextFill(Color.DARKBLUE);
			mapNameLabel.setLayoutX(10);
			mapNameLabel.setLayoutY(0);

			ImageView rightView = new ImageView(rightButton);
			rightView.setFitWidth(85);
			rightView.setFitHeight(85);
			rightView.setLayoutX(blueBView.getLayoutX() + blueBView.getFitWidth() - 3);
			rightView.setLayoutY(blueBView.getLayoutY() + blueBView.getFitHeight() / 2 - 42.5);

			ImageView leftView = new ImageView(leftButton);
			leftView.setFitWidth(85);
			leftView.setFitHeight(85);
			leftView.setLayoutX(blueBView.getLayoutX() - leftView.getFitWidth() + 2);
			leftView.setLayoutY(blueBView.getLayoutY() + blueBView.getFitHeight() / 2 - 42.5);

			rightView.setOnMouseClicked(e -> {
				if (!snapshotImages.isEmpty()) {
					currentSnapshotIndex = (currentSnapshotIndex + 1) % snapshotImages.size();
					mapPreview.setImage(snapshotImages.get(currentSnapshotIndex));
					mapNameLabel.setText(snapshotNames.get(currentSnapshotIndex));
				}
			});

			leftView.setOnMouseClicked(e -> {
				if (!snapshotImages.isEmpty()) {
					currentSnapshotIndex = (currentSnapshotIndex - 1 + snapshotImages.size()) % snapshotImages.size();
					mapPreview.setImage(snapshotImages.get(currentSnapshotIndex));
					mapNameLabel.setText(snapshotNames.get(currentSnapshotIndex));
				}
			});

			ImageView cancel = new ImageView(new Image(getClass().getResourceAsStream("/Images/exit.png")));
			cancel.setFitWidth(50);
			cancel.setFitHeight(50);
			cancel.setLayoutX(450);
			cancel.setLayoutY(1);
			cancel.setOnMouseClicked(e -> root.getChildren().remove(mapSelectionOverlay));

			selectionBox.getChildren().addAll(title, cancel, blueBView, mapPreview, mapNameLabel, rightView, leftView, group);
			mapSelectionOverlay.getChildren().add(selectionBox);
			root.getChildren().add(mapSelectionOverlay);
		} else {
			mapSelectionOverlay.setVisible(true);
		}

		if (!root.getChildren().contains(mapSelectionOverlay)) {
			root.getChildren().add(mapSelectionOverlay);
		}
	}

	private void loadMapSnapshotButtons() {
		List<StackPane> buttons = new ArrayList<>();
		snapshotImages.clear();
		snapshotNames.clear();

		File snapshotDir = new File("Data/Snapshots");
		if (!snapshotDir.exists()) return;

		File[] files = snapshotDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
		if (files == null) return;

		for (File file : files) {
			String mapName = file.getName().replace(".png", "");

			Image image = new Image(file.toURI().toString());
			snapshotImages.add(image);
			snapshotNames.add(mapName);

			ImageView thumb = new ImageView(image);
			thumb.setFitWidth(150);
			thumb.setFitHeight(100);

			Label label = new Label(mapName);
			label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			label.setTextFill(Color.BLACK);

			StackPane button = new StackPane(thumb, label);
			StackPane.setAlignment(label, javafx.geometry.Pos.BOTTOM_CENTER);

			buttons.add(button);
		}
	}

	private void processButtonEvents(ActionEvent event) {
		if(event.getSource() == newGameButton) {
			//MainMenuController.startNewGame("Pre-Built Map 2");
			showMapSelectionOverlay();
		} else if (event.getSource() == mapEditorButton) {
			MapEditorController.resetMap();
			app.showMapEditor();
		} else if (event.getSource() == optionsButton) {
			GameOptionsController.initializeGameOptions();
			app.showOptionsMenu(new StackPane());
		} else if (event.getSource() == quitGameButton) {
			MainMenuController.quitGame();
		}
	}
}
