package ui;

import domain.controller.MapEditorController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class MapEditorScene {
	private KuTowerDefenseApp app;
	private MapEditorController mapEditorController = MapEditorController.getInstance(true);
	private HashMap<Point2D, GroupedTile> placedTiles = new HashMap<>();
	private HashMap<Integer, Rectangle> tileHighlights = new HashMap<>();
	private Pane editedMap = new Pane();
	private Pane mapForEditor = new Pane();
	private StackPane root = new StackPane();
	private Pane highlightPane = new Pane();
	private Pane draggingLayer = new Pane();
	private Pane biggerSideBar = new Pane();
	private Pane exitBar = new Pane();
	private Pane optionBar = new Pane();
	private ScrollPane sideBar;
	private Rectangle highlightBox = new Rectangle(70, 70);
	private ImageView draggingClone = null;
	private AssetImage topmid = new AssetImage("top", this);
	private AssetImage topbottom = new AssetImage("topleft", this);
	private AssetImage topright = new AssetImage("topright", this);
	private AssetImage horizontalTopEnd = new AssetImage("verticalendtop", this);
	private AssetImage midLeft = new AssetImage("left", this);
	private AssetImage grass = new AssetImage("grass", this);
	private AssetImage midRight = new AssetImage("right", this);
	private AssetImage horizontalMid = new AssetImage("verticalmiddle", this);
	private AssetImage leftBottom = new AssetImage("bottomleft", this);
	private AssetImage midBottom = new AssetImage("bottom", this);
	private AssetImage midBottomRight = new AssetImage("bottomright", this);
	private AssetImage horizontalBottom = new AssetImage("verticalendbottom", this);
	private AssetImage verticalLeft = new AssetImage("horizontalendleft", this);
	private AssetImage verticalMid = new AssetImage("horizontalmiddle", this);
	private AssetImage verticalRight = new AssetImage("horizontalendright", this);
	private AssetImage junctionNoBottom = new AssetImage("junctionnobottom", this);
	private AssetImage junctionNoLeft = new AssetImage("junctionnoleft", this);
	private AssetImage junctionNoRight = new AssetImage("junctionnoright", this);
	private AssetImage junctionNoTop = new AssetImage("junctionnotop", this);
	private AssetImage junctionCross = new AssetImage("junctioncross", this);
	private AssetImage treeFirst = new AssetImage("tree1", this);
	private AssetImage treeSecond = new AssetImage("tree2", this);
	private AssetImage treeThird = new AssetImage("tree3", this);
	private AssetImage rockFirst = new AssetImage("rock1", this);
	private AssetImage artilleryTower = new AssetImage("artillerytower", this);
	private AssetImage mageTower = new AssetImage("magetower", this);
	private AssetImage house = new AssetImage("house", this);
	private AssetImage rockSecond = new AssetImage("rock2", this);
	private AssetImage lot = new AssetImage("lot", this);
	private AssetImage castle = new AssetImage("castle", this);
	private AssetImage archerTower = new AssetImage("archertower", this);
	private AssetImage well = new AssetImage("well", this);
	private AssetImage houseSecond = new AssetImage("house2", this);
	private AssetImage wood = new AssetImage("wood", this);
	private AssetImage saveButton = new AssetImage("save", this);
	private AssetImage exitButton = new AssetImage("exit", this);
	private AssetImage setEndButton = new AssetImage("setend", this);
	private AssetImage setStartButton = new AssetImage("setstart", this);
	private AssetImage eraserButton = new AssetImage("eraser", this);
	private Image erasserPressed = new Image(getClass().getResourceAsStream("/Images/eraserpressed.png"));
	private Image setEndPressed = new Image(getClass().getResourceAsStream("/Images/setendpressed.png"));
	private Image setStartPressed = new Image(getClass().getResourceAsStream("/Images/setstartpressed.png"));
	private Image clickButtonPressed = new Image(getClass().getResourceAsStream("/Images/clickbuttonpressed.png"));
	private boolean isErasing = false;
	private Rectangle eraserHighlightBox = new Rectangle(70, 70);
	private Image editMapImage = new Image(getClass().getResourceAsStream("/Images/HUD/blueb3.png"));
	private Group editModeGroup = new Group();
	private boolean isSetStart = false;
	private boolean isSetEnd = false;
	private boolean isClick = false;
	private Image saveExitButton = new Image(getClass().getResourceAsStream("/Images/exit.png"));
	private AssetImage deleteButton = new AssetImage("delete", this);
	private AssetImage clickButton = new AssetImage("clickbutton", this);
	private String selectedClickTileName;

	public MapEditorScene(KuTowerDefenseApp app) {
		File dir = new File("/Data/Snapshots");
		if (!dir.exists()) dir.mkdirs();
		System.out.println(getClass());
		this.app = app;
	}

	public Scene getScene() {
		highlightBox.setFill(Color.color(0.678, 0.847, 0.902, 0.3));
		highlightBox.setStroke(Color.LIGHTBLUE);
		highlightBox.setStrokeWidth(2);
		highlightBox.setVisible(false);
		eraserHighlightBox.setFill(Color.color(0.678, 0.847, 0.902, 0.3));
		eraserHighlightBox.setStroke(Color.LIGHTBLUE);
		eraserHighlightBox.setStrokeWidth(2);
		eraserHighlightBox.setVisible(false);
		highlightPane.setMouseTransparent(true);
		draggingLayer.getChildren().add(eraserHighlightBox);
		draggingLayer.getChildren().add(highlightBox);
		mapForEditor.setPickOnBounds(false);
		Pane dashedLineMap = dashedLines(9,16,70,70);
		dashedLineMap.setPickOnBounds(false);
		draggingLayer.setPickOnBounds(false);
		editedMap.setPickOnBounds(false);
		dashedLineMap.setMouseTransparent(true);
		HBox content = new HBox(renderMapEditor(), getSideBar());
		root.getChildren().addAll(content, editedMap, dashedLineMap, highlightPane, draggingLayer);
		Scene scene = new Scene(root);

		mapEditorController.addHighlightListener((Integer id) -> {
			if (id != null && tileHighlights.containsKey(id)) {
				Rectangle highlight = tileHighlights.remove(id);
				highlightPane.getChildren().remove(highlight);
			}
		});

		return scene;
	}
	private void setupNamePromptOverlay() {
		Pane promptBox = new Pane();
		promptBox.setPrefSize(400, 220);
		promptBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

		Label nameLabel = new Label("Enter Map Name:");
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		nameLabel.setLayoutX(100);
		nameLabel.setLayoutY(35);

		TextField nameField = new javafx.scene.control.TextField();
		nameField.setLayoutX(70);
		nameField.setLayoutY(75);
		nameField.setPrefWidth(260);

		javafx.scene.control.Button saveBtn = new javafx.scene.control.Button("Save");
		saveBtn.setLayoutX(160);
		saveBtn.setLayoutY(125);

		ImageView cancelButton = new ImageView(saveExitButton);
		cancelButton.setFitWidth(57);
		cancelButton.setFitHeight(57);
		cancelButton.setLayoutX(343);
		cancelButton.setLayoutY(0);

		promptBox.getChildren().addAll(nameLabel, nameField, saveBtn, cancelButton);
		DynamicPopup dynamicPopup = new DynamicPopup(promptBox, root, DynamicPopupAlignment.TOPCENTER, 1);

		cancelButton.setOnMouseClicked(e -> {
			dynamicPopup.cleanupPopup();
		});

		saveBtn.setOnAction(e -> {
			String enteredName = nameField.getText();
			if (!enteredName.isBlank()) {
				mapEditorController.saveMap(enteredName);
				dynamicPopup.cleanupPopup();

				String sanitized = enteredName.replaceAll("[^a-zA-Z0-9-_.]", "_");
				File outputDir = new File("Data/Snapshots");
				if (!outputDir.exists()) outputDir.mkdirs();
				Pane snapshotPane = new Pane();
				snapshotPane.setPrefSize(1120, 630);

				for (int y = 0; y < 9; y++) {
					for (int x = 0; x < 16; x++) {
						ImageView grassTile = new ImageView(grass);
						grassTile.setFitWidth(70);
						grassTile.setFitHeight(70);
						grassTile.setLayoutX(x * 70);
						grassTile.setLayoutY(y * 70);
						snapshotPane.getChildren().add(grassTile);
					}
				}
				for (GroupedTile tile : placedTiles.values()) {
					ImageView original = tile.getView();
					ImageView copy = new ImageView(original.getImage());
					copy.setFitWidth(original.getFitWidth());
					copy.setFitHeight(original.getFitHeight());
					copy.setLayoutX(original.getLayoutX());
					copy.setLayoutY(original.getLayoutY());
					snapshotPane.getChildren().add(copy);
				}

				snapshotPane.applyCss();
				snapshotPane.layout();

				WritableImage snapshot = snapshotPane.snapshot(new SnapshotParameters(), null);
				File outputFile = new File(outputDir, sanitized + ".png");
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", outputFile);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private Pane dashedLines(int inty, int intx, double height,double width) {
		Pane dashedPane = new Pane();
		double x;
		double y;
		for(int i = 0; i< inty; i++) {
			for(int j = 0; j< intx; j++) {
				x = j* width;
				y = i* height;

				Pane tilePane = new Pane();
				tilePane.setPrefSize(height, width);
				tilePane.setStyle("-fx-border-style: dashed; -fx-border-width: 0.8; -fx-border-color: black;");
				tilePane.setLayoutX(x);
				tilePane.setLayoutY(y);
				dashedPane.getChildren().addAll(tilePane);

			}
		}
		return dashedPane;

	}
	private Pane renderMapEditor() {
		double x;
		double y;

		for(int i = 0; i <9; i++) {
			for(int j = 0; j< 16; j++) {
				ImageView tileView = new ImageView(grass);

				tileView.setFitWidth(70);
				tileView.setFitHeight(70);

				x = j* 70;
				y = i* 70;

				tileView.setLayoutX(x);
				tileView.setLayoutY(y);

				mapForEditor.getChildren().addAll(tileView);

			}
		}

		return mapForEditor;
	}

	private Pane getSideBar() {
		biggerSideBar.setPrefSize(269,630);
        biggerSideBar.setStyle("-fx-background-color: #434447;");

		exitBar.setPrefSize(269,62.5);
		exitBar.setLayoutX(4);
		exitBar.setLayoutY(0);
        exitBar.setStyle("-fx-background-color: #8FD393;");

        optionBar.setPrefSize(269, 62.5);
        optionBar.setLayoutX(4);
		optionBar.setLayoutY(63.5);
        optionBar.setStyle("-fx-background-color: #8FD393;");

        ImageView saveView = new ImageView(saveButton);
		saveView.setUserData(saveButton.getName());
		ImageView exitView = new ImageView(exitButton);
		exitView.setUserData(exitButton.getName());
		ImageView setEndView = new ImageView(setEndButton);
		setEndView.setUserData(setEndButton.getName());
		ImageView setStartView = new ImageView(setStartButton);
		setStartView.setUserData(setStartButton.getName());
		ImageView eraserView = new ImageView(eraserButton);
		eraserView.setUserData(eraserButton.getName());
		ImageView editMapView = new ImageView(editMapImage);
		ImageView deleteView = new ImageView(deleteButton);
		deleteView.setUserData(deleteButton.getName());
		ImageView clickView = new ImageView(clickButton);
		clickView.setUserData(clickButton.getName());



		editMapView.setFitWidth(185);
		editMapView.setFitHeight(57);
		editMapView.setLayoutX(12);
		editMapView.setLayoutY(5);

		Label editor = new Label("Edit Mode");
		Font font = Font.font("Comic Sans MS", FontWeight.BOLD, 22);
		editor.setFont(font);
		editor.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
		editor.setLayoutY(12);
		editor.setLayoutX(53);

		editModeGroup.getChildren().addAll(editMapView,editor);
		exitView.setFitHeight(57);
        exitView.setFitWidth(57);
        exitView.setLayoutX(194);
        exitView.setLayoutY(0);

        saveView.setFitHeight(52);
        saveView.setFitWidth(52);
        saveView.setLayoutX(207);
        saveView.setLayoutY(4);

		eraserView.setFitHeight(52);
		eraserView.setFitWidth(52);
		eraserView.setLayoutX(165);
		eraserView.setLayoutY(5);

		setStartView.setFitHeight(52);
		setStartView.setFitWidth(52);
		setStartView.setLayoutX(123);
		setStartView.setLayoutY(5);

		setEndView.setFitHeight(52);
		setEndView.setFitWidth(52);
		setEndView.setLayoutX(83);
		setEndView.setLayoutY(5);

		deleteView.setFitHeight(52);
		deleteView.setFitWidth(52);
		deleteView.setLayoutX(41);
		deleteView.setLayoutY(3);

		clickView.setFitHeight(52);
		clickView.setFitWidth(52);
		clickView.setLayoutX(-1);
		clickView.setLayoutY(5);

		deleteView.setOnMouseClicked(e->{
			deleteEventProcessor();
		});

		eraserView.setOnMouseEntered(e -> {
			if (!isErasing) {
				eraserView.setImage(new Image(getClass().getResourceAsStream("/Images/eraserhover.png")));
			}
		});
		eraserView.setOnMouseExited(e -> {
			if (!isErasing) {
				eraserView.setImage(new Image(getClass().getResourceAsStream("/Images/eraser.png")));
			}
		});

		setStartView.setOnMouseEntered(e -> {
			if (!isSetStart) {
				setStartView.setImage(new Image(getClass().getResourceAsStream("/Images/setstarthover.png")));
			}
		});
		setStartView.setOnMouseExited(e -> {
			if (!isSetStart) {
				setStartView.setImage(new Image(getClass().getResourceAsStream("/Images/setstart.png")));
			}
		});

		setEndView.setOnMouseEntered(e -> {
			if (!isSetEnd) {
				setEndView.setImage(new Image(getClass().getResourceAsStream("/Images/setendhover.png")));
			}
		});
		setEndView.setOnMouseExited(e -> {
			if (!isSetEnd) {
				setEndView.setImage(new Image(getClass().getResourceAsStream("/Images/setend.png")));
			}
		});

		clickView.setOnMouseEntered(e -> {
			if (!isClick) {
				clickView.setImage(new Image(getClass().getResourceAsStream("/Images/clickbuttonhover.png")));
			}
		});
		clickView.setOnMouseExited(e -> {
			if (!isClick) {
				clickView.setImage(new Image(getClass().getResourceAsStream("/Images/clickbutton.png")));
			}
		});

		hoverEffect(exitView);
		hoverEffect(saveView);
		hoverEffect(deleteView);

		saveView.setOnMouseClicked(e -> {
			if (mapEditorController.validateMap()) {
				setupNamePromptOverlay();
			} else {

			}
		});


		eraserView.setOnMouseClicked(e -> {
			isErasing = !isErasing;
			isSetStart = false;
			isSetEnd = false;
			isClick = false;

			if (isErasing) {
				eraserView.setImage(erasserPressed);
				setStartView.setImage(new Image(getClass().getResourceAsStream("/Images/setstart.png")));
				setEndView.setImage(new Image(getClass().getResourceAsStream("/Images/setend.png")));
				clickView.setImage(new Image(getClass().getResourceAsStream("/Images/clickbutton.png")));
				eraserEventProcess();
			} else {
				eraserView.setImage(new Image(getClass().getResourceAsStream("/Images/eraser.png")));
				editedMap.setOnMouseClicked(null);
			}
		});


		setStartView.setOnMouseClicked(e -> {
			isSetStart = !isSetStart;
			isSetEnd = false;
			isErasing = false;
			isClick = false;

			if (isSetStart) {
				setStartView.setImage(setStartPressed);
				setEndView.setImage(new Image(getClass().getResourceAsStream("/Images/setend.png")));
				eraserView.setImage(new Image(getClass().getResourceAsStream("/Images/eraser.png")));
				clickView.setImage(new Image(getClass().getResourceAsStream("/Images/clickbutton.png")));
				setStartEventProcess();
			} else {
				setStartView.setImage(new Image(getClass().getResourceAsStream("/Images/setstart.png")));
				editedMap.setOnMouseClicked(null);
			}
		});

		setEndView.setOnMouseClicked(e -> {
			isSetEnd = !isSetEnd;
			isSetStart = false;
			isErasing = false;
			isClick = false;

			if (isSetEnd) {
				setEndView.setImage(setEndPressed);
				setStartView.setImage(new Image(getClass().getResourceAsStream("/Images/setstart.png")));
				eraserView.setImage(new Image(getClass().getResourceAsStream("/Images/eraser.png")));
				clickView.setImage(new Image(getClass().getResourceAsStream("/Images/clickbutton.png")));

				setEndEventProcess();
			} else {
				setEndView.setImage(new Image(getClass().getResourceAsStream("/Images/setend.png")));
				editedMap.setOnMouseClicked(null);
			}
		});

		clickView.setOnMouseClicked(e->{
			isClick = !isClick;
			isSetStart = false;
			isErasing = false;
			isSetEnd = false;

			if (isClick) {
				clickView.setImage(clickButtonPressed);
				setStartView.setImage(new Image(getClass().getResourceAsStream("/Images/setstart.png")));
				setEndView.setImage(new Image(getClass().getResourceAsStream("/Images/setend.png")));
				eraserView.setImage(new Image(getClass().getResourceAsStream("/Images/eraser.png")));
				clickEventProcess();
			} else {
				clickView.setImage(new Image(getClass().getResourceAsStream("/Images/clickbutton.png")));
				editedMap.setOnMouseClicked(null);
			}

		});

		exitView.setOnMouseClicked(e->{
			app.showMainMenu(new StackPane());
		});

        optionBar.getChildren().addAll(saveView,eraserView,setStartView,setEndView,deleteView,clickView);
        exitBar.getChildren().addAll(exitView,editModeGroup);

		ImageView topBot = createTileImageView(topbottom, 0, 0);
		ImageView topMid = createTileImageView(topmid, 62.5, 0);
		ImageView topRight = createTileImageView(topright, 125, 0);
		ImageView horizontalTop = createTileImageView(horizontalTopEnd, 187.5, 0);
		ImageView midL = createTileImageView(midLeft, 0, 62.5);
		ImageView grassView = createTileImageView(grass, 62.5, 62.5);
		ImageView midRightView = createTileImageView(midRight, 125, 62.5);
		ImageView horizontalMidView = createTileImageView(horizontalMid, 187.5, 62.5);
		ImageView leftBotomView = createTileImageView(leftBottom, 0, 125);
		ImageView midBottomView = createTileImageView(midBottom, 62.5, 125);
		ImageView midBottomRightView = createTileImageView(midBottomRight, 125, 125);
		ImageView horizontalBottomView = createTileImageView(horizontalBottom, 187.5, 125);
		ImageView verticalLeftView = createTileImageView(verticalLeft, 0, 187.5);
		ImageView verticalMidView = createTileImageView(verticalMid, 62.5, 187.5);
		ImageView verticalRightView = createTileImageView(verticalRight, 125, 187.5);
		ImageView lotView = createTileImageView(lot, 187.5, 187.5);
		ImageView junctionNoLeftView = createTileImageView(junctionNoLeft, 0, 250);
		ImageView junctionNoBottomView = createTileImageView(junctionNoBottom, 62.5, 250);
		ImageView junctionNoTopView = createTileImageView(junctionNoTop, 125, 250);
		ImageView junctionNoRightView = createTileImageView(junctionNoRight, 187.5, 250);
		ImageView junctionCrossView = createTileImageView(junctionCross, 0, 312.5);
		ImageView treeFirstView = createTileImageView(treeFirst, 62.5, 312.5);
		ImageView treeSecondView = createTileImageView(treeSecond, 125, 312.5);
		ImageView treeThirdView = createTileImageView(treeThird, 187.5, 312.5);
		ImageView archerTowerView = createTileImageView(archerTower, 0, 375);
		ImageView artilleryTowerView = createTileImageView(artilleryTower, 62.5, 375);
		ImageView mageTowerView = createTileImageView(mageTower, 125, 375);
		ImageView houseView = createTileImageView(house, 187.5, 375);
		ImageView wellView = createTileImageView(well, 125, 437.5);
		ImageView houseSecondView = createTileImageView(houseSecond, 187.5, 437.5);
		ImageView castleView = createTileImageView(castle, 0, 437.5);
		castleView.setFitHeight(125);
		castleView.setFitWidth(125);
		ImageView woodView = createTileImageView(wood, 125, 500);
		ImageView rockFirstView = createTileImageView(rockFirst, 187.5, 500);
		ImageView rockSecondView = createTileImageView(rockSecond, 0, 562.5);

		Pane bar = new Pane();
		bar.getChildren().addAll(topBot,topMid, topRight,horizontalTop,midL,
				grassView,midRightView,horizontalMidView,leftBotomView,midBottomView,midBottomRightView,
				horizontalBottomView,verticalLeftView,verticalMidView,verticalRightView,lotView,treeFirstView,treeSecondView,
				treeThirdView,rockFirstView, artilleryTowerView,mageTowerView,houseView,rockSecondView,
				castleView, archerTowerView,wellView,houseSecondView,woodView,junctionCrossView,junctionNoBottomView,junctionNoLeftView,
				junctionNoRightView,junctionNoTopView);

		Pane dashedLineSide = dashedLines(10,4,62.5,62.5);
		dashedLineSide.setPickOnBounds(false);
		dashedLineSide.setMouseTransparent(true);
		bar.getChildren().add(dashedLineSide);

		highlightEffect(bar);

		sideBar = new ScrollPane(bar);
		sideBar.setPrefSize(265, 505);
		sideBar.setLayoutX(4);
		sideBar.setLayoutY(127);
		sideBar.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		sideBar.getStylesheets().add(getClass().getResource("/styles/scrollPaneStyleWithFill.css").toExternalForm());

        biggerSideBar.getChildren().addAll(exitBar,optionBar,sideBar);


		return biggerSideBar;
	}


	private ImageView createTileImageView(AssetImage assetImage, double x, double y) {
		ImageView view = new ImageView(assetImage);
		sizingImage(view);
		view.setLayoutX(x);
		view.setLayoutY(y);
		view.setPickOnBounds(true);
		view.setUserData(assetImage.getName());
		eventProcessor(view);
		return view;
	}

	private void sizingImage(ImageView image) {
		image.setFitHeight(62.5);
		image.setFitWidth(62.5);
	}

	private void eventProcessor(ImageView image) { //anlayana
		image.setOnMousePressed(event -> {
			if (isClick) {
				String tileName = (String) image.getUserData();
				selectedClickTileName = tileName;
				System.out.println("tilename:" + selectedClickTileName);
				return;
			}
			if(!isErasing && !isSetStart && !isSetEnd && !isClick){
				String tileName = (String) image.getUserData();
				Image originalImage = new Image(getClass().getResourceAsStream("/Images/" + tileName + ".png"));
				draggingClone = new ImageView(originalImage);
				draggingClone.setUserData(image.getUserData());
				draggingClone.setFitWidth(image.getFitWidth());
				draggingClone.setFitHeight(image.getFitHeight());
				draggingClone.setLayoutX(event.getSceneX() - image.getFitWidth() / 2);
				draggingClone.setLayoutY(event.getSceneY() - image.getFitHeight() / 2);
				draggingClone.setOpacity(0.5);
				draggingLayer.getChildren().add(draggingClone);
			}
	    });

		image.setOnMouseDragged(event -> {
	        if (draggingClone != null && !isErasing) {
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();

	            draggingClone.setLayoutX(event.getSceneX() - draggingClone.getFitWidth() / 2);
	            draggingClone.setLayoutY(event.getSceneY() - draggingClone.getFitHeight() / 2);
				int cellX = (int) (mouseX / 70);
				int cellY = (int) (mouseY / 70);
				String tileName = (String) draggingClone.getUserData();

				if (cellX < 0 || cellX >= 16 || cellY < 0 || cellY >= 9) {
					highlightBox.setVisible(false);
					return;
				}

				if (tileName.equals("castle")) {
					highlightBox.setWidth(140);
					highlightBox.setHeight(140);
					highlightBox.setLayoutX(cellX * 70);
					highlightBox.setLayoutY(cellY * 70);
					highlightBox.setVisible(true);
				} else {
					highlightBox.setWidth(70);
					highlightBox.setHeight(70);
					highlightBox.setLayoutX(cellX * 70);
					highlightBox.setLayoutY(cellY * 70);
					highlightBox.setVisible(true);
				}
			}
	    });

		image.setOnMouseReleased(event -> { //i'll write the other comments
			if (draggingClone != null) {
				draggingClone.setOpacity(1);
				//getting our mouse coordinates
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();

				//turning it into grid cell coordinates
				int cellX = (int) (mouseX / 70);
				int cellY = (int) (mouseY / 70);

				//checking if it is inside our map if not remove
				if (cellX < 0 || cellX >= 16 || cellY < 0 || cellY >= 9) {
					draggingLayer.getChildren().remove(draggingClone);
					draggingClone = null;
					return;
				}

				//creating our tile's cell coordinate to store
				Point2D cell = new Point2D(cellX, cellY);
				String tileName = (String) draggingClone.getUserData();

				if (!tileName.equals("castle")) {
					//removing our group
					removeGroupedTileAt(cell);
				}

				if (tileName.equals("castle")) {
					//updating at backend
					boolean success = mapEditorController.forcePlaceCastle(cellX, cellY);
					if (!success) {
						draggingLayer.getChildren().remove(draggingClone);
						draggingClone = null;
						highlightBox.setVisible(false);
						return;
					}

					//removing our group
					removeGroupedTileAt(cell);

					//getting where our castle will be placed
					Set<Point2D> cells = Set.of(
							new Point2D(cellX, cellY),
							new Point2D(cellX + 1, cellY),
							new Point2D(cellX, cellY + 1),
							new Point2D(cellX + 1, cellY + 1)
					);
					//it is to be sure to not have intersect castles
					for (Point2D pt : cells) {
						removeGroupedTileAt(pt);
					}

					//fixing the image
					draggingClone.setFitWidth(140);
					draggingClone.setFitHeight(140);
					draggingClone.setLayoutX(cellX * 70);
					draggingClone.setLayoutY(cellY * 70);
					//adding the image to our pane
					editedMap.getChildren().add(draggingClone);
					//creating a groupedtile object
					GroupedTile castleTile = new GroupedTile("castle", cells, draggingClone);
					//adding it to our hashmap to store it
					for (Point2D pt : cells) {
						placedTiles.put(pt, castleTile);
					}
				}
				else if (tileName.equals("lot")) {
					//updating at backend
					mapEditorController.forcePlaceLot(cellX, cellY);
					//fixing the image
					draggingClone.setFitWidth(70);
					draggingClone.setFitHeight(70);
					draggingClone.setLayoutX(cellX * 70);
					draggingClone.setLayoutY(cellY * 70);
					//adding the image to our pane
					editedMap.getChildren().add(draggingClone);
					//creating a groupedtile object
					GroupedTile lotTile = new GroupedTile("lot", Set.of(cell), draggingClone);
					//adding it to our hashmap to store it
					placedTiles.put(cell, lotTile);
				}
				else if (tileName.equals("grass")) {
					//removing the tile
					mapEditorController.removeTile(cellX, cellY);
					//updating the backend due to tower removement logic
					mapEditorController.forcePlaceTile("grass", cellX, cellY);
					//updating the ui
					draggingLayer.getChildren().remove(draggingClone);
					draggingClone = null;
					highlightBox.setVisible(false);
					MapEditorController.getInstance(true).printMap();
					return;
				}
				else {
					//updating the backend
					mapEditorController.forcePlaceTile(tileName, cellX, cellY);
					//fixing the image
					draggingClone.setFitWidth(70);
					draggingClone.setFitHeight(70);
					draggingClone.setLayoutX(cellX * 70);
					draggingClone.setLayoutY(cellY * 70);
					//adding the image to our pane
					editedMap.getChildren().add(draggingClone);
					//creating a groupedtile object
					GroupedTile regularTile = new GroupedTile(tileName, Set.of(cell), draggingClone);
					//adding it to our hashmap to store it
					placedTiles.put(cell, regularTile);
				}

				//updating ui
				highlightBox.setVisible(false);
				draggingLayer.getChildren().remove(draggingClone);
				draggingClone = null;

				//console check
				mapEditorController.printMap();
				System.out.println(editedMap.getChildren());
			}
		});
	}

	private void removeGroupedTileAt(Point2D cell) {
		//gets the tile
		GroupedTile tile = placedTiles.get(cell);
		if (tile != null) {
			for (Point2D pt : tile.getCoveredCells()) {
				placedTiles.remove(pt);//removes the tile
			}
			editedMap.getChildren().remove(tile.getView());
		}
	}

	private Rectangle createHighlightBox(boolean isStart, double x, double y) {
		Rectangle highlight = new Rectangle();

		if (isStart) {
			highlight.setFill(Color.color(0.2, 0.847, 0.3, 0.3));
			highlight.setStroke(Color.LIGHTGREEN);
			highlight.setStrokeWidth(2);
		} else {
			highlight.setFill(Color.color(0.8, 0.2, 0.2, 0.3));
			highlight.setStroke(Color.DARKRED);
			highlight.setStrokeWidth(2);
		}

		highlight.setMouseTransparent(true);
		highlight.setWidth(70);
		highlight.setHeight(70);
		highlight.setX(x);
		highlight.setY(y);
		return highlight;
	}

	private void highlightEffect(Pane pane) {
		for(Node node : pane.getChildren()) {
			if(node instanceof ImageView) {
				ImageView tile = (ImageView) node;
				String tileName = (String) tile.getUserData();

				tile.setOnMouseEntered(e->{
					tile.setImage(new Image(getClass().getResourceAsStream("/Images/Highlighted/" + tileName +".png" )));
				});

				tile.setOnMouseExited(e->{
					tile.setImage(new Image(getClass().getResourceAsStream("/Images/" + tileName +".png" )));
				});

			}
		}
	}

	private void eraserEventProcess(){
		editedMap.setOnMouseClicked(e -> { //if you click at the background of a tower or castle or other thing it doesn't remove it is cause of editedMap
			if (!isErasing) return;

			double mouseX = e.getSceneX();
			double mouseY = e.getSceneY();
			int cellX = (int) (mouseX / 70);
			int cellY = (int) (mouseY / 70);
			Point2D cell = new Point2D(cellX, cellY);

			GroupedTile tile = null;
			for (GroupedTile t : placedTiles.values()) {
				if (t.getCoveredCells().contains(cell)) {
					tile = t;
					break;
				}
			}

			if (tile != null) {
				for (Point2D pt : tile.getCoveredCells()) {
					placedTiles.remove(pt);
					mapEditorController.removeTile((int) pt.getX(), (int) pt.getY());
					mapEditorController.forcePlaceTile("grass", (int) pt.getX(), (int) pt.getY());
				}
				editedMap.getChildren().remove(tile.getView());
			}
			eraserHighlightBox.setVisible(false);
			mapEditorController.printMap();
		});

		editedMap.setOnMouseMoved(e -> { //highlight ekle

		});

		editedMap.setOnMouseExited(e -> {
			if (isErasing) {
				eraserHighlightBox.setVisible(false);
			}
		});
	}
	private void clickEventProcess(){
		mapForEditor.setOnMouseClicked(this::placeTileForClick);
		editedMap.setOnMouseClicked(this::placeTileForClick);
//		mapForEditor.setOnMouseMoved(this::updateHighlightBoxForClick);
		//editedMap.setOnMouseMoved(this::updateHighlightBoxForClick);
//		mapForEditor.setOnMouseEntered(e -> highlightBox.setVisible(true));
		//editedMap.setOnMouseEntered(e -> highlightBox.setVisible(true));
//		mapForEditor.setOnMouseExited(e -> highlightBox.setVisible(false));
//		editedMap.setOnMouseExited(e -> highlightBox.setVisible(false));
	}

//	private void updateHighlightBoxForClick(MouseEvent e){
//		double mouseX = e.getSceneX();
//		double mouseY = e.getSceneY();
//		int cellX = (int) (mouseX / 70);
//		int cellY = (int) (mouseY / 70);
//		if (cellX < 0 || cellX >= 16 || cellY < 0 || cellY >= 9) {
//			highlightBox.setVisible(false);
//			return;
//		}
//
//		if (selectedClickTileName.equals("castle")) {
//			highlightBox.setWidth(140);
//			highlightBox.setHeight(140);
//			highlightBox.setLayoutX(cellX * 70);
//			highlightBox.setLayoutY(cellY * 70);
//			highlightBox.setVisible(true);
//		} else {
//			highlightBox.setWidth(70);
//			highlightBox.setHeight(70);
//			highlightBox.setLayoutX(cellX * 70);
//			highlightBox.setLayoutY(cellY * 70);
//			highlightBox.setVisible(true);
//		}
//	}

	private void deleteEventProcessor(){
		editedMap.getChildren().clear();
		MapEditorController.resetMap();
		mapEditorController =  MapEditorController.getInstance(true);
	}
	private void placeTileForClick(MouseEvent e){
		System.out.println("clicked");
		if (!isClick || selectedClickTileName == null) return;

		double mouseX = e.getSceneX();
		double mouseY = e.getSceneY();
		int cellX = (int) (mouseX / 70);
		int cellY = (int) (mouseY / 70);
		Point2D cell = new Point2D(cellX, cellY);

		if (cellX < 0 || cellX >= 16 || cellY < 0 || cellY >= 9) {
			return;
		}

		if (selectedClickTileName.equals("castle")) {
			boolean success = mapEditorController.forcePlaceCastle(cellX, cellY);
			if (!success) return;

			removeGroupedTileAt(cell);

			Set<Point2D> cells = Set.of(
					new Point2D(cellX, cellY),
					new Point2D(cellX + 1, cellY),
					new Point2D(cellX, cellY + 1),
					new Point2D(cellX + 1, cellY + 1)
			);
			for (Point2D pt : cells) removeGroupedTileAt(pt);

			ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Images/castle.png")));
			imageView.setFitWidth(140);
			imageView.setFitHeight(140);
			imageView.setLayoutX(cellX * 70);
			imageView.setLayoutY(cellY * 70);
			editedMap.getChildren().add(imageView);

			GroupedTile castleTile = new GroupedTile("castle", cells, imageView);
			for (Point2D pt : cells){
				placedTiles.put(pt, castleTile);
			}
		}
		else if (selectedClickTileName.equals("lot")) {
			mapEditorController.forcePlaceLot(cellX, cellY);
			ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Images/lot.png")));
			imageView.setFitWidth(70);
			imageView.setFitHeight(70);
			imageView.setLayoutX(cellX * 70);
			imageView.setLayoutY(cellY * 70);
			editedMap.getChildren().add(imageView);
			placedTiles.put(cell, new GroupedTile("lot", Set.of(cell), imageView));
		}
		else if (selectedClickTileName.equals("grass")) {
			mapEditorController.removeTile(cellX, cellY);
			mapEditorController.forcePlaceTile("grass", cellX, cellY);
			removeGroupedTileAt(cell);
		}
		else {
			mapEditorController.forcePlaceTile(selectedClickTileName, cellX, cellY);
			ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Images/" + selectedClickTileName + ".png")));
			imageView.setFitWidth(70);
			imageView.setFitHeight(70);
			imageView.setLayoutX(cellX * 70);
			imageView.setLayoutY(cellY * 70);
			editedMap.getChildren().add(imageView);
			placedTiles.put(cell, new GroupedTile(selectedClickTileName, Set.of(cell), imageView));
		}

		mapEditorController.printMap();
	}
	private void setEndEventProcess() {
		editedMap.setOnMouseClicked(e -> {
			if (!isSetEnd) return;

			double mouseX = e.getSceneX();
			double mouseY = e.getSceneY();
			int cellX = (int) (mouseX / 70);
			int cellY = (int) (mouseY / 70);
			Point2D cell = new Point2D(cellX, cellY);

			GroupedTile tile = null;
			for (GroupedTile t : placedTiles.values()) {
				if (t.getCoveredCells().contains(cell)) {
					tile = t;
					break;
				}
			}

			if (tile != null) {
				for (Point2D pt : tile.getCoveredCells()) {
					Boolean success = mapEditorController.addEndingTile((int) pt.getX(), (int) pt.getY());
					if (success == null) return;
					int id = cellY * mapEditorController.getMapWidth() + cellX;
					if (success) {
						double x = cellX * 70;
						double y = cellY * 70;
						Rectangle highlight = createHighlightBox(false, x, y);
						tileHighlights.put(id, highlight);
						highlightPane.getChildren().add(highlight);
					} else {
						Rectangle highlight = tileHighlights.remove(id);
						highlightPane.getChildren().remove(highlight);
					}
				}
			}
		});
	}

	private void setStartEventProcess() {
		editedMap.setOnMouseClicked(e -> {
			if (!isSetStart) return;

			double mouseX = e.getSceneX();
			double mouseY = e.getSceneY();
			int cellX = (int) (mouseX / 70);
			int cellY = (int) (mouseY / 70);
			Point2D cell = new Point2D(cellX, cellY);

			GroupedTile tile = null;
			for (GroupedTile t : placedTiles.values()) {
				if (t.getCoveredCells().contains(cell)) {
					tile = t;
					break;
				}
			}

			if (tile != null) {
				for (Point2D pt : tile.getCoveredCells()) {
					Boolean success = mapEditorController.addStartingTile((int) pt.getX(), (int) pt.getY());
					if (success == null) return;
					int id = cellY * mapEditorController.getMapWidth() + cellX;
					if (success) {
						double x = cellX * 70;
						double y = cellY * 70;
						Rectangle highlight = createHighlightBox(true, x, y);
						tileHighlights.put(id, highlight);
						highlightPane.getChildren().add(highlight);
					} else {
						Rectangle highlight = tileHighlights.remove(id);
						highlightPane.getChildren().remove(highlight);
					}
				}
			}
		});
	}

	private void hoverEffect(ImageView image){
		image.setOnMouseEntered(e->{
			image.setImage(new Image(getClass().getResourceAsStream("/Images/" + image.getUserData() +"hover.png" )));
		});
		image.setOnMouseExited(e->{
			image.setImage(new Image(getClass().getResourceAsStream("/Images/" + image.getUserData() +".png" )));
		});
	}
	private class AssetImage extends Image{
		private String name;

		public AssetImage(String name, Object source) {
			super(source.getClass().getResourceAsStream("/Images/"+name+".png"));
			System.out.println(source.getClass());
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private class GroupedTile {
		private String name;
		private Set<Point2D> coveredCells;
		private ImageView view;

		public GroupedTile(String name, Set<Point2D> coveredCells, ImageView view) {
			this.name = name;
			this.coveredCells = coveredCells;
			this.view = view;
		}

		public Set<Point2D> getCoveredCells(){
			return coveredCells;
		}
		public ImageView getView(){
			return view;
		}
	}

}

