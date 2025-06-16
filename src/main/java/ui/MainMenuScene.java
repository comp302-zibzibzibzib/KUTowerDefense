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
import javafx.scene.layout.Region;
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
	StackPane root = new StackPane();
	private Image buttonBlue = new Image(getClass().getResourceAsStream(SPRITE_PATH + "blueb5.png"));
	private Image rightButton = new Image(getClass().getResourceAsStream(SPRITE_PATH + "rightbutton.png"));
	private Image leftButton = new Image(getClass().getResourceAsStream(SPRITE_PATH + "leftbutton.png"));
	private List<Image> snapshotImages = new ArrayList<>();
	private List<String> snapshotNames = new ArrayList<>();
	private int currentSnapshotIndex = 0;
	private ImageView mapPreview;
	private Label mapNameLabel;

	private class MenuButton extends Button {
		public MenuButton(MainMenuScene scene) {
			this.setBackground(null);
			this.setOnAction(scene::processButtonEvents);
		}
	}
	
	private KuTowerDefenseApp app;
    private Button newGameButton;
	private Button mapEditorButton;
	private Button optionsButton;
    private Button quitGameButton;
    private Scene scene;
    
    private StackPane createButtonStackPane(Image image, Image hoverImage, Image clickedImage, Label label, int fontSize, int width, int height, int textOffset) {
		ImageView view = new ImageView(image);
		view.setFitHeight(height);
		view.setFitWidth(width);

		Font font = Font.font("Comic Sans MS", FontWeight.BOLD, fontSize);
		label.setFont(font);
		label.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");

		StackPane pane = new StackPane();
		pane.getChildren().addAll(view, label);

		label.setTranslateY(-textOffset);

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

		pane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

		return pane;
	}

    public MainMenuScene(KuTowerDefenseApp app, StackPane root) {
		this.app = app;
		this.root = root;
		newGameButton = new MenuButton(this);
		newGameButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("New Game"), 20, 150, 50, 5));
		mapEditorButton = new MenuButton(this);
		mapEditorButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Map Editor"), 20, 150, 50, 5));
		mapEditorButton.setTranslateY(50);
		optionsButton = new MenuButton(this);
		optionsButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Options"), 20, 150, 50, 5));
		optionsButton.setTranslateY(100);
		quitGameButton = new MenuButton(this);
		quitGameButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3, new Label("Quit Game"), 20, 150, 50, 5));
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

		Pane mapSelectionOverlay = new Pane();
		mapSelectionOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

		mapSelectionOverlay.setPrefSize(500, 300);
		mapSelectionOverlay.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		mapSelectionOverlay.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

		Label title = new Label("Select a Map");
		title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
		title.setTextFill(Color.BLUE);
		title.setLayoutX((500 - 150) / 2.0);
		title.setLayoutY(5);

		Button confirmButton = new Button();
		confirmButton.setBackground(null);
		confirmButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3,
				new Label("Confirm"), 35, 200, 80, 10));
		confirmButton.setLayoutX(155);
		confirmButton.setLayoutY(215);

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

		mapSelectionOverlay.getChildren().addAll(title, cancel, blueBView, mapPreview, mapNameLabel, rightView, leftView, confirmButton);
		DynamicPopup dynamicPopup = new DynamicPopup(mapSelectionOverlay, root, DynamicPopupAlignment.CENTER, 3);

		cancel.setOnMouseClicked(e -> dynamicPopup.cleanupPopup());
		confirmButton.setOnAction(e -> {
			if (!snapshotNames.isEmpty()) {
				String selectedMap = snapshotNames.get(currentSnapshotIndex);
				MainMenuController.startNewGame(selectedMap);
				app.startGame();
				dynamicPopup.cleanupPopup();
			}
		});
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
