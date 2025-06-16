package ui;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import domain.controller.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class PlayModeScene extends AnimationTimer {
	private KuTowerDefenseApp app;
	private Group towerSelection = null;
	private TileView selectedLot = null;
	private TileView selectedTower = null;
	private Circle rangeCircle = null;
	private Group circle = null;
	private MapEditorController mapEditorController = MapEditorController.getInstance(false);
	private Pane rootPane;
	private Pane enemyPane;
	private Pane goldBagPane;
	private Pane overlayPane;
	private Pane hudPane;
	private Pane projectilePane;
	private Canvas projectileCanvas = new Canvas();
	private Pane effectPane;
	private Group removeSelection = null;

	private Image archerButtonImage  = new Image(getClass().getResourceAsStream("/Images/archerbutton.png"));
    private Image mageButtonImage = new Image(getClass().getResourceAsStream("/Images/magebutton.png"));
    private Image artilleryButtonImage = new Image(getClass().getResourceAsStream("/Images/artillerybutton.png"));
    private Image circleImage = new Image(getClass().getResourceAsStream("/Images/circle.png"));
    private Image hoverArtillery = new Image(getClass().getResourceAsStream("/Images/artillerybuttonhover.png"));
    private Image hoverMage = new Image(getClass().getResourceAsStream("/Images/magebuttonhover.png"));
    private Image hoverArcher  = new Image(getClass().getResourceAsStream("/Images/archerbuttonhover.png"));
    private Image gearButtonImage = new Image(getClass().getResourceAsStream("/Images/gearbutton.png"));
    private Image resumeButtonImage =  new Image(getClass().getResourceAsStream("/Images/resume.png"));
    private Image pauseButtonImage = new Image(getClass().getResourceAsStream("/Images/pause.png"));
    private Image accelerateButtonImage = new Image(getClass().getResourceAsStream("/Images/accelerate.png"));
    private Image deleteHover = new Image(getClass().getResourceAsStream("/Images/deletehover.png"));
    private Image gearHover = new Image(getClass().getResourceAsStream("/Images/gearHover.png"));
    private Image resumeHover = new Image(getClass().getResourceAsStream("/Images/resumehover.png"));
    private Image accelerateHover = new Image(getClass().getResourceAsStream("/Images/acceleratehover.png"));
    private Image pauseHover = new Image(getClass().getResourceAsStream("/Images/pausehover.png"));
	private Image redRibbon = new Image(getClass().getResourceAsStream("/Images/HUD/ribbon_red.png"));
	private Image blueRibbon = new Image(getClass().getResourceAsStream("/Images/HUD/ribbon_blue.png"));
	private Image yellowRibbon = new Image(getClass().getResourceAsStream("/Images/HUD/ribbon_yellow.png"));
	private Image buttonBlue3 = new Image(getClass().getResourceAsStream("/Images/HUD/blueb3.png"));
	private Image buttonHover3 = new Image(getClass().getResourceAsStream("/Images/HUD/hoverb3.png"));

	private HBox hbox;
	
	private Map<Integer, EnemyStack> enemyStacks = new HashMap<Integer, EnemyStack>();
	private Map<Integer, Integer> enemyFrameIndicies = new HashMap<Integer, Integer>();
	private Map<String, List<Image>> enemyAnimations = new HashMap<>();
	private Map<Integer, GoldBagView> goldBags = new HashMap<>();
	private Map<Integer, EffectView> effectViews = new HashMap<>();
	private Map<String, Image> projectileImages = new HashMap<>();

	private double totalElapsed = 0;
	private long lastUpdate = 0;
	
	private SecureRandom random = new SecureRandom();

	private class TileView extends ImageView {
		public TileView(Image image, int xIndex, int yIndex) {
			super(image);
			double renderScale = 16;
			
			double fit = PlayModeController.getTileLength() * renderScale;
			this.setFitWidth(fit);  
			this.setFitHeight(fit);

			double x = PlayModeController.getTileXCoord(xIndex, yIndex) * renderScale - (fit) / 2.0;
			double y = PlayModeController.getTileYCoord(xIndex, yIndex) * renderScale - (fit) / 2.0;

			this.setLayoutX(x);
			this.setLayoutY(y);
		}
	}
	
	private class HealthBar extends StackPane {
		private final Image barEmpty = new Image(getClass().getResourceAsStream("/Images/HUD/bar.png"));
		private final Image barFull = new Image(getClass().getResourceAsStream("/Images/HUD/bar_full.png"));

		private ImageView emptyView;
		private ImageView fullView;
		private DoubleProperty widthProperty = new SimpleDoubleProperty(1.0);

		double width = 45.0;
		double height = 10.0;

		public HealthBar() {
			super();
			emptyView = new ImageView(barEmpty);
			fullView = new ImageView(barFull);

			emptyView.setFitWidth(width);
			emptyView.setFitHeight(height);
			fullView.setFitWidth(width);
			fullView.setFitHeight(height);

			fullView.fitWidthProperty().bind(widthProperty);
			
			this.getChildren().addAll(emptyView, fullView);
		}

		private void updateFullViewPosition() {
			double fitWidth = widthProperty.getValue();

			double difference = width - fitWidth;
			double offset = -difference * 0.38; // DON'T CHANGE THIS NUMBER IT BREAKS EVERYTHING!!?!?!!?

			fullView.setTranslateX(offset); // Had to remove the Rectangle Clip because of pixel issues
			// This is smoother but needed a number found by trial and error :D
		}
		
		public void setPercentage(double value) {
			if (value < 0.0) value = 0.0;
			else if (value > 1.0) value = 1.0;
			widthProperty.set(value * width);
			updateFullViewPosition();
		}
	}
	
	private class EnemyStack extends StackPane {
		private final Image snowflake = new Image(getClass().getResourceAsStream("/Images/HUD/snowflake_icon.png"));
		private final Image thunder = new Image(getClass().getResourceAsStream("/Images/HUD/thunder_icon.png"));

		private ImageView snowflakeView;
		private ImageView thunderView;
		private ImageView enemyView;
		private HealthBar healthBar;

		double thunderX, thunderY = 35;
		double snowflakeX, snowflakeY = 35;
		boolean isSlowed;
		boolean isFast;

		public EnemyStack() {
			isSlowed = false;
			isFast = false;

			double iconSize = 12;
			thunderView = new ImageView(thunder);
			thunderView.setTranslateY(thunderY);
			thunderView.setFitHeight(iconSize);
			thunderView.setFitWidth(iconSize);

			snowflakeView = new ImageView(snowflake);
			snowflakeView.setTranslateY(snowflakeY);
			snowflakeView.setFitHeight(iconSize);
			snowflakeView.setFitWidth(iconSize);

			enemyView = new ImageView();
			healthBar = new HealthBar();
			
			enemyView.setFitWidth(128);
            enemyView.setFitHeight(128);
            healthBar.setTranslateY(35);
            
            this.getChildren().addAll(enemyView, healthBar);
		}

		private void repaintEnemyStack(boolean isSlowed, boolean isFast) {
			this.getChildren().clear();
			this.getChildren().addAll(enemyView, healthBar);
			if (isSlowed) this.getChildren().add(snowflakeView);
			if (isFast) this.getChildren().add(thunderView);
		}

		public void updateIcons(boolean isSlowed, boolean isFast) {
			if (this.isSlowed == isSlowed && this.isFast == isFast) return;

			this.isSlowed = isSlowed; this.isFast = isFast;
			if (!isSlowed && !isFast) {
				if (thunderX != 0 || snowflakeX != 0) {
					thunderX = 0; snowflakeX = 0;
					repaintEnemyStack(false, false);
					return;
				}
			}
			if (isSlowed && isFast) {
				snowflakeX = -42.0; thunderX = -30.0;
			} else if (isSlowed && !isFast) {
				snowflakeX = -30;
			} else if (!isSlowed && isFast) {
				thunderX = -30;
			}

			thunderView.setTranslateX(thunderX);
			snowflakeView.setTranslateX(snowflakeX);
			repaintEnemyStack(isSlowed, isFast);
		}
		
		public void setImage(Image image) {
			enemyView.setImage(image);
		}
		
		public ImageView getEnemyView() {
			return enemyView;
		}

		public Consumer<Double> getConsumer() {
			return (val) -> {
				healthBar.applyCss();
				healthBar.layout();
				healthBar.setPercentage(val);
			};
		}
	}

	private class GoldBagView extends ImageView {
		public static final ArrayList<Image> frames = new ArrayList<>();
		public static final double FRAME_DURATION = 0.15;

		private int frameIndex;
		private int id;
		private double timeSinceLastFrame;
		public GoldBagView(int id) {
			super();
			this.id = id;
			frameIndex = 0;
			timeSinceLastFrame = 0;
			setImage(frames.get(frameIndex));
			setFitHeight(128);
			setFitWidth(128);

			setOnMouseClicked(event -> {
				EntityController.pickUpBag(id);
				goldBagPane.getChildren().remove(this);
			});
		}

		private void nextFrame() {
			setImage(frames.get(++frameIndex));
			timeSinceLastFrame = 0;
		}

		public void updateBag(double deltaTime) {
			timeSinceLastFrame += deltaTime;
			if (timeSinceLastFrame >= FRAME_DURATION && frameIndex < frames.size() - 1) {
				nextFrame();
			}

			double x = EntityController.getGoldBagX(id);
			double y = EntityController.getGoldBagY(id);
			setLayoutX(x * 16 - getFitWidth()/2);
			setLayoutY(y * 16 - getFitHeight()/2);

			if (EntityController.isGoldBagFlashing(id)) {
				double timeSinceCreation = EntityController.getGoldBagTime(id);
				double opacity = (Math.sin(10 * timeSinceCreation) > 0) ? 1.0 : 0.0;
				setOpacity(opacity);
			}
		}
	}

	private class EffectView extends ImageView {
		private static final double FRAME_DURATION = 0.1;

		public static List<Image> fireRedFrames = new ArrayList<>();
		public static List<Image> fireBlueFrames = new ArrayList<>();
		public static List<Image> explosionFrames = new ArrayList<>();

		private final int id;
		private final int frameCount;
		private int frameIndex;
		private List<Image> frames;
		private double timeSinceLastFrame;

		public EffectView(int id) {
			this.id = id;
			String name = EntityController.getEffectName(id);

			double x = EntityController.getEffectX(id);
			double y = EntityController.getEffectY(id);

			frames = switch (name) {
				case "explosion" -> explosionFrames;
				case "fireRed" -> fireRedFrames;
				case "fireBlue" -> fireBlueFrames;
				default -> null;
			};

			if (frames == null) {
				throw new IllegalArgumentException("Invalid effect name: " + name);
			}

			frameCount = frames.size();
			frameIndex = 0;
			setImage(frames.get(frameIndex++));

			if (name.equals("explosion")) {
				setFitHeight(128);
				setFitWidth(128);
			} else {
				setFitHeight(96);
				setFitWidth(96);
			}

			timeSinceLastFrame = 0.0;

			setLayoutX(x * 16 - getFitWidth()/2);
			setLayoutY(y * 16 - getFitHeight()/2);

			effectViews.put(id, this);
			effectPane.getChildren().add(this);
		}

		public void update(double deltaTime) {
			if (frameIndex >= frameCount) {
				EntityController.killEffect(id);
				effectViews.remove(id);
				effectPane.getChildren().remove(this);
				return;
			}

			timeSinceLastFrame += deltaTime;
			if (timeSinceLastFrame >= FRAME_DURATION) {
				setImage(frames.get(frameIndex++));
				timeSinceLastFrame = 0;
			}
		}
	}
	

	public PlayModeScene(KuTowerDefenseApp app) {
		this.app = app;

		GoldBagView.frames.clear();
		EffectView.fireRedFrames.clear();
		EffectView.fireBlueFrames.clear();
		EffectView.explosionFrames.clear();

		for (int i = 1; i <= 7; i++) {
			String name = "/Images/goldbag/goldbag" + i + ".png";
			Image frame = new Image(getClass().getResourceAsStream(name));
			GoldBagView.frames.add(frame);
		}

		for (int i = 1; i <= 7; i++) {
			String name = "/Images/fireRed/fireRed" + i + ".png";
			Image frame = new Image(getClass().getResourceAsStream(name));
			EffectView.fireRedFrames.add(frame);
		}

		for (int i = 1; i <= 7; i++) {
			String name = "/Images/fireBlue/fireBlue" + i + ".png";
			Image frame = new Image(getClass().getResourceAsStream(name));
			EffectView.fireBlueFrames.add(frame);
		}

		for (int i = 1; i <= 9; i++) {
			String name = "/Images/explosion/explosion" + i + ".png";
			Image frame = new Image(getClass().getResourceAsStream(name));
			EffectView.explosionFrames.add(frame);
		}

		if (projectileImages.isEmpty()) {
			projectileImages.put("bomb", new Image(getClass().getResourceAsStream("/Images/projectiles/bomb.png")));
			projectileImages.put("arrow", new Image(getClass().getResourceAsStream("/Images/projectiles/arrow.png")));
			projectileImages.put("red_ball", new Image(getClass().getResourceAsStream("/Images/projectiles/red_ball.png")));
			projectileImages.put("blue_ball", new Image(getClass().getResourceAsStream("/Images/projectiles/blue_ball.png")));
		}
	}
	
	public Scene getScene() {
		loadEnemyFrames();
		rootPane = renderMap();
		enemyPane = new Pane();
		enemyPane.setMouseTransparent(true);
		goldBagPane = new Pane();
		goldBagPane.setPickOnBounds(false);
		overlayPane = new Pane();
		overlayPane.setPickOnBounds(false);
		hudPane = new Pane();
		hudPane.setPickOnBounds(false);
		effectPane = new Pane();
		effectPane.setMouseTransparent(true);
		//projectilePane = new Pane();
		//projectilePane.setMouseTransparent(true);
		projectileCanvas = new Canvas();
		projectileCanvas.setMouseTransparent(true);
		pauseResumeMenuButtons();
		StackPane stack = new StackPane(rootPane, enemyPane, projectileCanvas, effectPane, goldBagPane, overlayPane, hudPane);
		Platform.runLater(() -> {
			projectileCanvas.setWidth(stack.getWidth());
			projectileCanvas.setHeight(stack.getHeight());
		});
		this.start();

		playerStatsRender();
		AtomicBoolean gameEnd = new AtomicBoolean(false);
		Consumer<Integer> gameOverAction = (val) -> {
			if (val <= 0 && !gameEnd.get()) {
				gameEnd.set(true);
				PlayModeController.decelerateGame();

				Pane gameOverPane = gameEndPane(false);
				DynamicPopup dynamicPopup = new DynamicPopup(gameOverPane, stack, DynamicPopupAlignment.CENTER, 1);
			}
		};
		PlayerController.addLivesListener(gameOverAction);

		Consumer<Boolean> winGameAction = (val) -> {
			if (val && !gameEnd.get()) {
				gameEnd.set(true);
				PlayModeController.decelerateGame();

				Pane gameWinPane = gameEndPane(true);
				DynamicPopup dynamicPopup = new DynamicPopup(gameWinPane, stack, DynamicPopupAlignment.CENTER, 1);
			}
		};
		EntityController.addRemovedEnemyListener(winGameAction);

		Scene scene = new Scene(stack);
		return scene;
	}

	private Pane gameEndPane(boolean win) {
		Pane gameOverPane = new Pane();
		StackPane gameOverLabelStack;
		if (win) {
			gameOverLabelStack = createLabelStackPane(yellowRibbon, new Label("You Win"),
					35, 300, 100);
		} else {
			gameOverLabelStack = createLabelStackPane(redRibbon, new Label("Game Over"),
					35, 300, 100);
		}
		gameOverLabelStack.setLayoutX(12);

		Button mainMenuButton = new Button();
		mainMenuButton.setBackground(null);
		mainMenuButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3,
				new Label("Main Menu"), 20, 150, 50));

		mainMenuButton.setOnAction(e -> {
			stop();
			MainMenuController.cleanupSession();
			app.showMainMenu(new StackPane());
		});

		Button replayButton = new Button();
		replayButton.setBackground(null);
		replayButton.setGraphic(createButtonStackPane(buttonBlue3, buttonHover3, buttonBlue3,
				new Label("Replay"), 20, 150, 50));

		replayButton.setOnAction(e -> {
			String mapName = PlayModeController.getMapName();
			stop();
			MainMenuController.cleanupSession();
			MainMenuController.startNewGame(mapName);
			app.startGame();
		});

		HBox buttonBox = new HBox(mainMenuButton, replayButton);
		buttonBox.setLayoutY(140);

		gameOverPane.getChildren().addAll(gameOverLabelStack, buttonBox);
		return gameOverPane;
	}

	private StackPane createButtonStackPane(Image image, Image hoverImage, Image clickedImage, Label label, int fontSize, int width, int height) {
		ImageView view = new ImageView(image);
		view.setFitHeight(height);
		view.setFitWidth(width);

		Font font = Font.font("Comic Sans MS", FontWeight.BOLD, fontSize);
		label.setFont(font);
		label.setStyle("-fx-text-fill: white;");

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

		pane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

		return pane;
	}

	private StackPane createLabelStackPane(Image image, Label label, int fontSize, int width, int height) {
		ImageView view = new ImageView(image);
		view.setFitHeight(height);
		view.setFitWidth(width);

		Font font = Font.font("Comic Sans MS", FontWeight.BOLD, fontSize);
		label.setFont(font);
		label.setStyle("-fx-text-fill: white;");

		StackPane pane = new StackPane();
		pane.getChildren().addAll(view, label);

		label.setTranslateY(-10);

		return pane;
	}

	private Pane grassRender() {
		Pane map = new Pane();
		double renderScale = 16;

		int height = PlayModeController.getMapHeight();
		int width = PlayModeController.getMapWidth();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {

				Image grassImage = new Image(getClass().getResourceAsStream("/Images/grass.png"));
				TileView grassView = new TileView(grassImage, j, i);
				
				map.getChildren().addAll(grassView);
			}
		}
		return map;
	}
	
	private Pane renderMap() {
		Pane map = grassRender(); 
		String assetName;
		double renderScale = 16;
		boolean castleRender = false;
		
		int height = PlayModeController.getMapHeight();
		int width = PlayModeController.getMapWidth();
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width;j++) {
				assetName = PlayModeController.getAssetName(j, i);
				
				if (assetName.equals("castle")) {
					if (j + 1 < width && i + 1 < height) {
						String r = PlayModeController.getAssetName(j + 1, i);
						String d = PlayModeController.getAssetName(j, i + 1);
						String dr = PlayModeController.getAssetName(j + 1, i + 1);

						if (r.equals("castle") && d.equals("castle") && dr.equals("castle")) {
							castleMaker(map, i, j);
							continue;
						}
					}
					continue;
			}
				
				Image tileImage = new Image(getClass().getResourceAsStream("/Images/"+assetName+".png"));
				TileView tileView = new TileView(tileImage, j, i);
				
				if(assetName.equals("lot")) {
					tileView.setOnMouseClicked(event ->{
						selectedLot = tileView;
						showLotCircle(tileView);
						System.out.println("damn");
					});
				}
				else {
					tileView.setOnMouseClicked(event->{
						removeCircle(towerSelection);
					});
				}
				if(assetName.equals("archertower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
					tileView.setOnMouseClicked(event->{
						removeRange();
						selectedTower = tileView;
						showDeleteCircle(tileView);
					});
				}
				else if(assetName.equals("magetower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
					tileView.setOnMouseClicked(event->{
						removeRange();
						selectedTower = tileView;
						showDeleteCircle(tileView);

					});
					
				}
				else if(assetName.equals("artillerytower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
					tileView.setOnMouseClicked(event->{
						removeRange();
						selectedTower = tileView;
						showDeleteCircle(tileView);

					});
				}
				else if (!assetName.equals("lot")) {
					tileView.setOnMouseEntered(event -> {
						removeRange();
					});
					tileView.setOnMouseClicked(event->{
						removeCircle(removeSelection);
						removeCircle(towerSelection);
					});
				}
				else {
					tileView.setOnMouseEntered(event -> {
						removeRange();
					});
				}
				
				map.getChildren().addAll(tileView);

			}
		}
		return map;
		
	}
	
	
	private void castleMaker(Pane pane , int i , int j) {
		int[][] tileIndicies = new int[][] {{i, j}, {i, j+1}, {i+1, j}, {i+1, j+1}};
		String[] assetNames = new String[] {"/Images/hugetowertopleft.png", "/Images/hugetowertopright.png",
				"/Images/hugetowerbottomleft.png", "/Images/hugetowerbottomright.png"};
		
		for (int index = 0; index < 4; index++) {
			int h = tileIndicies[index][0];
			int w = tileIndicies[index][1];
			String assetName = assetNames[index];
			
			double x = PlayModeController.getTileXCoord(w, h) * 16 - (80) / 2.0;
			double y = PlayModeController.getTileYCoord(w, h) * 16 - (80) / 2.0;
			
			Image castleImage = new Image(getClass().getResourceAsStream(assetName));
			TileView castleView = new TileView(castleImage, w, h);
			
			castleView.setFitWidth(80);  
			castleView.setFitHeight(80);
			castleView.setLayoutX(x);
			castleView.setLayoutY(y);
			
			pane.getChildren().addAll(castleView);
		}
	}
	
	private void showLotCircle(ImageView lot) {
		if (towerSelection != null&& towerSelection.getParent() != null) {
	        ((Pane) towerSelection.getParent()).getChildren().remove(towerSelection);
	    }	
		int x = (int)(lot.getLayoutX()/80);
    	int y = (int)(lot.getLayoutY()/80);
		double centerX = lot.getLayoutX() + lot.getFitWidth() / 2;
	    double centerY = lot.getLayoutY() + lot.getFitHeight() / 2;
	    
	    ImageView archerButtonView = new ImageView(archerButtonImage);
	    ImageView mageButtonView = new ImageView(mageButtonImage);
	    ImageView artilleryButtonView = new ImageView(artilleryButtonImage);
	    archerButtonView.setFitWidth(40);
	    archerButtonView.setFitHeight(40);
	    mageButtonView.setFitWidth(40);
	    mageButtonView.setFitHeight(40);
	    artilleryButtonView.setFitWidth(40);
	    artilleryButtonView.setFitHeight(40);


	    ImageView circleView = new ImageView(circleImage);
	    circleView.setFitWidth(100);
	    circleView.setFitHeight(100);
	    circleView.setLayoutX(centerX - 50);
	    circleView.setLayoutY(centerY - 50);
	    
	    Button archerButton = new Button();
	    archerButton.setGraphic(archerButtonView);
	    archerButton.setBackground(null);
	    archerButton.setPrefSize(40, 40);
	    if(lot.getLayoutY() == 0) {
	    	archerButton.setLayoutX(centerX- 27);
		    archerButton.setLayoutY(centerY +25 );
	    }
	    else {
	    	archerButton.setLayoutX(centerX- 25);
		    archerButton.setLayoutY(centerY - 80);
	    }
	   
	    Button mageButton = new Button();
	    mageButton.setPrefSize(20,20);
	    mageButton.setGraphic(mageButtonView);
	    mageButton.setStyle("-fx-background-color: transparent;");
	    mageButton.setLayoutX(centerX- 75);
	    mageButton.setLayoutY(centerY - 20);
	    mageButton.setFocusTraversable(false);

	    
	    Button artilleryButton = new Button();
	    artilleryButton.setStyle("-fx-background-color: transparent;");
	    artilleryButton.setGraphic(artilleryButtonView);
	    artilleryButton.setPrefSize(40, 40);
	    artilleryButton.setLayoutX(centerX + 20);
	    artilleryButton.setLayoutY(centerY -20);
	    
	    
	    archerButton.setOnMouseClicked(event ->{
	    	removeCircle(towerSelection);
	    	if (!TowerController.canBuildArcher()) return;
			mapEditorController.createArcherTower(x, y);
					
			Pane parent = (Pane) selectedLot.getParent();
			parent.getChildren().remove(selectedLot);
			selectedLot = null;
			
			Image archerImage = new Image(getClass().getResourceAsStream("/Images/archertower.png"));
			TileView archerTile = new TileView(archerImage, x, y);
			
			archerTile.setOnMouseEntered(event2 -> {
				rangeRender(archerTile);
			});
			archerTile.setOnMouseClicked(event2 -> {
				selectedTower = archerTile;
				removeRange();
				showDeleteCircle(archerTile);
			});
			
			parent.getChildren().add(archerTile);
			
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("aaaa");
			
		});
	    archerButton.setOnMouseEntered(e -> { archerButtonView.setImage(hoverArcher); });
	    archerButton.setOnMouseExited(e -> {archerButtonView.setImage(archerButtonImage); });
	    
	    mageButton.setOnMouseClicked(event -> {
	    	removeCircle(towerSelection);
	    	if (!TowerController.canBuildMage()) return;
			mapEditorController.createMageTower(x, y);

			Pane parent = (Pane) selectedLot.getParent();
			parent.getChildren().remove(selectedLot);
			selectedLot = null;
			
			Image mageImage = new Image(getClass().getResourceAsStream("/Images/magetower.png"));
			TileView mageTile = new TileView(mageImage, x, y);
			
			mageTile.setOnMouseEntered(event2 -> {
				rangeRender(mageTile);
			});
			mageTile.setOnMouseClicked(event2 -> {
				selectedTower = mageTile;
				removeRange();
				showDeleteCircle(mageTile);
			});
			
			parent.getChildren().add(mageTile);
			
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("mmmmm"); //mmmm

	    });
	    mageButton.setOnMouseEntered(e -> { mageButtonView.setImage(hoverMage); });
	    mageButton.setOnMouseExited(e -> { mageButtonView.setImage(mageButtonImage); });
	    
	    artilleryButton.setOnMouseClicked(event->{
	    	removeCircle(towerSelection);
	    	if (!TowerController.canBuildArtillery()) return;
			mapEditorController.createArtilleryTower(x, y);

			Pane parent = (Pane) selectedLot.getParent();
			parent.getChildren().remove(selectedLot);
			selectedLot = null;
			
			Image artilleryImage = new Image(getClass().getResourceAsStream("/Images/artillerytower.png"));
			TileView artilleryTile = new TileView(artilleryImage, x, y);
			
			artilleryTile.setOnMouseEntered(event2 -> {
				rangeRender(artilleryTile);
			});
			artilleryTile.setOnMouseClicked(event2 -> {
				selectedTower = artilleryTile;
				removeRange();
				showDeleteCircle(artilleryTile);
			});
			
			parent.getChildren().add(artilleryTile);
			
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("arrrr");

	    });
	    artilleryButton.setOnMouseEntered(e -> { artilleryButtonView.setImage(hoverArtillery); });
		artilleryButton.setOnMouseExited(e -> { artilleryButtonView.setImage(artilleryButtonImage); });
	    
	    towerSelection = new Group(circleView, archerButton,mageButton,artilleryButton);

	    overlayPane.getChildren().add(towerSelection);
	}
	
	private void showDeleteCircle(ImageView tower) {
		if (removeSelection != null&& removeSelection.getParent() != null) {
	        ((Pane) removeSelection.getParent()).getChildren().remove(removeSelection);
	    }	
		int x = (int)(tower.getLayoutX()/80);
    	int y = (int)(tower.getLayoutY()/80);
        double centerX = tower.getLayoutX() + tower.getFitWidth() / 2;
        double centerY = tower.getLayoutY() + tower.getFitHeight() / 2;
        Image circleImage = new Image(getClass().getResourceAsStream("/Images/circle.png"));
        ImageView circleView = new ImageView(circleImage);
        circleView.setFitWidth(100);
        circleView.setFitHeight(100);
        circleView.setLayoutX(centerX - 50);
        circleView.setLayoutY(centerY - 50);

        Image deleteButtonImage  = new Image(getClass().getResourceAsStream("/Images/delete.png"));
        ImageView deleteButtonView = new ImageView(deleteButtonImage);
        deleteButtonView.setFitWidth(40);
        deleteButtonView.setFitHeight(40);

		ImageView upgradeButtonView = new ImageView(archerButtonImage);
		upgradeButtonView.setFitWidth(40);
		upgradeButtonView.setFitHeight(40);

        Button deleteButton = new Button();
        deleteButton.setGraphic(deleteButtonView);
        deleteButton.setBackground(null);
        deleteButton.setPrefSize(40, 40);
        if(tower.getLayoutY() == 0) {
            deleteButton.setLayoutX(centerX - 27);
             deleteButton.setLayoutY(centerY + 25);
        }
        else {
            deleteButton.setLayoutX(centerX - 27);
            deleteButton.setLayoutY(centerY - 80);
        }

		Button upgradeButton = new Button();
		upgradeButton.setGraphic(upgradeButtonView);
		upgradeButton.setBackground(null);
		upgradeButton.setPrefSize(40, 40);

        deleteButton.setOnMouseClicked(event -> {
            removeCircle(removeSelection);
            mapEditorController.removeTower(x,y);
            
            Pane parent = (Pane) selectedTower.getParent();
            
            if (selectedTower == null) return;
            parent.getChildren().remove(selectedTower);
            selectedTower = null;
            
            Image lotImage = new Image(getClass().getResourceAsStream("/Images/lot.png"));
            TileView lotView = new TileView(lotImage, x, y);
            
            lotView.setOnMouseClicked(event2 -> {
				selectedLot = lotView;
				showLotCircle(lotView);
				System.out.println("damn");
			});
            
			parent.getChildren().add(lotView);
			System.out.print(PlayerController.getPlayerGold());
        });
        deleteButton.setOnMouseEntered(e -> { deleteButtonView.setImage(deleteHover); });
	    deleteButton.setOnMouseExited(e -> { deleteButtonView.setImage(deleteButtonImage); });

        removeSelection = new Group(circleView,deleteButton);

        overlayPane.getChildren().add(removeSelection);
	}
	
	
	private void removeCircle(Group group) {
		if (group != null && group.getParent() != null) {
	        ((Pane) group.getParent()).getChildren().remove(group);
	        group = null;
	    }	
	}
	
	
	private void rangeRender(ImageView tower) {
	 if (circle != null && circle.getParent() != null) {
	        ((Pane) circle.getParent()).getChildren().remove(circle);
	    }
		double centerX = tower.getLayoutX() + tower.getFitWidth() / 2;
	    double centerY = tower.getLayoutY() + tower.getFitHeight() / 2;
	    int x = (int)(tower.getLayoutX() / 80);
	    int y = (int)(tower.getLayoutY() / 80);
	    double range = PlayModeController.getTowerRange(x, y);
	    
	    double uiRange = range * 16;
	    rangeCircle = new Circle(centerX,centerY, uiRange);
	    rangeCircle.setFill(Color.color(0.678, 0.847, 0.902, 0.3));
	    rangeCircle.setStroke(Color.LIGHTBLUE);
	    rangeCircle.setStrokeWidth(2);
	    rangeCircle.setMouseTransparent(true);
	    
	    circle = new Group(rangeCircle);
	    
	    overlayPane.getChildren().add(circle);
	    
	}
	private void removeRange() {
		if (circle != null && circle.getParent() != null) {
	        ((Pane) circle.getParent()).getChildren().remove(circle);
	        circle = null;
	    }
	}
	private void playerStatsRender() {
		Image coinImage  = new Image(getClass().getResourceAsStream("/Images/HUD/coin.png"));
        ImageView coinView = new ImageView(coinImage);
        Image heartImage  = new Image(getClass().getResourceAsStream("/Images/HUD/health.png"));
        ImageView heartImageView = new ImageView(heartImage);
        Image waveImage  = new Image(getClass().getResourceAsStream("/Images/HUD/wave.png"));
        ImageView waveView = new ImageView(waveImage);
        Image infoImage  = new Image(getClass().getResourceAsStream("/Images/HUD/blueb3.png"));
        ImageView infoView1 = new ImageView(infoImage);
        ImageView infoView2 = new ImageView(infoImage);
        ImageView infoView3 = new ImageView(infoImage);

        
        coinView.setFitHeight(50);
        coinView.setFitWidth(50);
        
        heartImageView.setFitHeight(50);
        heartImageView.setFitWidth(50);
        
        waveView.setFitHeight(50);
        waveView.setFitWidth(50);
        coinView.setLayoutX(0);
        coinView.setLayoutY(0);
        heartImageView.setLayoutX(0);
        heartImageView.setLayoutY(50);
        waveView.setLayoutX(0);
        waveView.setLayoutY(100);
        
        infoView1.setFitHeight(50);
        infoView1.setFitWidth(110);
        infoView2.setFitHeight(50);
        infoView2.setFitWidth(110);
        infoView3.setFitHeight(50);
        infoView3.setFitWidth(110);
        
        infoView1.setLayoutX(50);
        infoView1.setLayoutY(0);
       
        Label coin = new Label(String.format("%d", PlayerController.getPlayerGold()));
        Label lives = new Label(String.format("%d/%d", PlayerController.getPlayerLives(),GameOptionsController.getOption("Player Lives")));
        Label waves = new Label(String.format("%d/%d", PlayerController.getWaveNumber(),GameOptionsController.getOption("Wave Number")));
        
        
        coin.setLayoutX(80);
        coin.setLayoutY(7);
        
        lives.setLayoutX(75);
        lives.setLayoutY(57);

        waves.setLayoutX(80);
        waves.setLayoutY(107);

		// 							Yippeeee comic sans
        Font font = Font.font("Comic Sans MS",FontWeight.BOLD,FontPosture.REGULAR, 18);  
        coin.setFont(font);
        lives.setFont(font);
        waves.setFont(font);
        
        coin.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
        lives.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
        waves.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
        
        Consumer<Integer> goldAction = (val) -> coin.setText(String.format("%d", PlayerController.getPlayerGold()));
        Consumer<Integer> livesAction = (val) -> {
			int playerLives = PlayerController.getPlayerLives();
			if (playerLives <= 0) { playerLives = 0; }
			lives.setText(String.format("%d/%d", playerLives,
					GameOptionsController.getOption("Player Lives")));
		};
        Consumer<Integer> wavesAction = (val) -> waves.setText(String.format("%d/%d", PlayerController.getWaveNumber(),
        																		GameOptionsController.getOption("Wave Number")));
        
        PlayerController.addGoldListener(goldAction);
        PlayerController.addLivesListener(livesAction);
        PlayerController.addWaveNumberListener(wavesAction);
        
        Group statCoin = playerStats(coinView, infoView1,coin);
        hudPane.getChildren().addAll(statCoin);

        infoView2.setLayoutX(50);
        infoView2.setLayoutY(50);
        Group statHealth = playerStats(heartImageView, infoView2,lives);
        hudPane.getChildren().addAll(statHealth);

        infoView3.setLayoutX(50);
        infoView3.setLayoutY(100);
        Group statWave = playerStats(waveView, infoView3,waves);
        hudPane.getChildren().addAll(statWave);
	}
	
	private Group playerStats(ImageView image, ImageView info, Label label) {
		Group stats = new Group(image, info, label);
		return stats;
        
        
	}

	private void loadEnemyFrames() {
		List<Image> warriorFrames = new ArrayList<>();
		List<Image> goblinFrames = new ArrayList<>();
		
		for(int i = 1; i < 7 ; i++) {
			Image warrior = new Image(getClass().getResourceAsStream("/Images/warrior/warrior"+i+".png"));
			Image goblin = new Image(getClass().getResourceAsStream("/Images/goblin/goblin"+i+".png"));
			warriorFrames.add(warrior);
			goblinFrames.add(goblin);
			enemyAnimations.put("warrior", warriorFrames);
			enemyAnimations.put("goblin", goblinFrames);
		}
	}

	private Pane getGameOverPane() {
		Pane gameOverPane = new Pane();
		gameOverPane.setPrefSize(600, 400);
		ImageView gameOverBanner = new ImageView(redRibbon);
		Label gameOverLabel = new Label("Game Over");
		gameOverBanner.setLayoutX(gameOverPane.getPrefWidth()/2 - gameOverBanner.getFitWidth()/2);
		gameOverLabel.setLayoutX(gameOverPane.getPrefWidth()/2 - gameOverLabel.getLayoutBounds().getWidth()/2);

		gameOverPane.getChildren().addAll(gameOverBanner, gameOverLabel);
		return gameOverPane;
	}

	@Override
	public void handle(long arg0) {
		if (PlayModeController.getGameSpeed() == 0) {
			lastUpdate = arg0;
			return;
		}
		
		if (lastUpdate == 0) {
			lastUpdate = arg0;
			return;
		}
		
		double deltaTime = (arg0 - lastUpdate)/1_000_000_000.0;
		deltaTime *= PlayModeController.getGameSpeed();
		lastUpdate = arg0;
		totalElapsed += deltaTime;
        
		int addFrame = 0;
		if (totalElapsed >= 0.1) {
        	addFrame = 1;
        	totalElapsed = 0;
        }

		GraphicsContext projectileGraphicsContext = projectileCanvas.getGraphicsContext2D();
		projectileGraphicsContext.clearRect(0, 0, projectileCanvas.getWidth(), projectileCanvas.getHeight());

		for (int i = 0; i < TowerController.getNumberOfProjectiles(); i++) {
			int id = TowerController.getProjectileID(i);
			double centroidX = TowerController.getProjectileX(i) * 16;
			double centroidY = TowerController.getProjectileY(i) * 16;
			String name = TowerController.getProjectileName(id);
			int width = 32;
			int height = 32;

			if (name.equals("bomb")) {
				width = 48;
				height = 48;
			}

			Image projectileImage = projectileImages.get(name);

			projectileGraphicsContext.save();
			projectileGraphicsContext.translate(centroidX, centroidY);
			if (!name.equals("bomb")) projectileGraphicsContext.rotate(TowerController.getProjectileAngle(i));
			projectileGraphicsContext.drawImage(projectileImage, -width * 0.5, -height * 0.5, width, height);
			projectileGraphicsContext.restore();
		}

		for (int i = 0; i < EntityController.getNumberOfEnemies(); i++) {

	        String type = null;
	        if (EntityController.isGoblin(i)) type = "goblin";
	        else type = "warrior";
	        
	        List<Image> frames = enemyAnimations.get(type);

	        double x = EntityController.getEnemyXCoord(i);
	        double y = EntityController.getEnemyYCoord(i);
	        int id = EntityController.getEnemyID(i);

	        // If this enemy doesn't have an ImageView yet
	        if (!enemyStacks.containsKey(id)) {
	        	EnemyStack es = new EnemyStack();
	        	enemyFrameIndicies.put(id, 0);

				es.getConsumer().accept(1.0);
				Consumer<Double> healthBarUpdater = es.getConsumer();
				EntityController.addEnemyHPListener(i, healthBarUpdater);

	        	enemyPane.getChildren().add(es);
	            enemyStacks.put(id, es);
	        }

	        EnemyStack es = enemyStacks.get(id);
	        int frameIndex = (enemyFrameIndicies.get(id) + addFrame) % 6;
	        es.setImage(frames.get(frameIndex));
	        enemyFrameIndicies.put(id, frameIndex);
	        es.updateIcons(EntityController.isEnemySlowedDown(i), EntityController.isKnightFast(i));

	        int scale = EntityController.getXScale(i);
	        if (scale != 0) {
	        	es.getEnemyView().setScaleX(scale);
	        }
	        
	        es.setLayoutX(x * 16 - es.getEnemyView().getFitWidth()/2);  // adjust scale as needed
	        es.setLayoutY(y * 16 - es.getEnemyView().getFitHeight()/2);
	    }

		// Bring every enemy stack to front from top to down so that the most down enemy is always drawn on top
		List<Integer> enemyIdList = EntityController.getEnemyIDsRenderSort();
		for (int id : enemyIdList) {
			EnemyStack es = enemyStacks.get(id);
			es.toFront();
		}
		
		if (rangeCircle != null) {
			rangeCircle.toFront();
		}

		enemyStacks.entrySet().removeIf(entry -> {
	        int id = entry.getKey();
	        if (!EntityController.isEnemyIDInitialized(id)) {
	        	enemyPane.getChildren().remove(entry.getValue());
	        	enemyFrameIndicies.remove(id);
	            return true;
	        }
	        return false;
		});

		// Add any new gold bags
		boolean newBag = false;
		for (int id : EntityController.getGoldBagIDs()) {
			if (!goldBags.containsKey(id)) {
				GoldBagView gbv = new GoldBagView(id);
				goldBags.put(id, gbv);
				goldBagPane.getChildren().add(gbv);
			}
		}

		// Remove expired gold bags
		goldBags.entrySet().removeIf(entry -> {
			int id = entry.getKey();
			if (EntityController.isGoldBagDead(id)) {
				goldBagPane.getChildren().remove(entry.getValue());
				return true;
			}
			return false;
		});

		// Update gold bag renders
		for (int id : goldBags.keySet()) {
			double x = EntityController.getGoldBagX(id);
			double y = EntityController.getGoldBagY(id);

			GoldBagView gbv = goldBags.get(id);
			gbv.updateBag(deltaTime);
		}

		List<Integer> goldBagIdList = EntityController.getGoldBagsIDsRenderSort();
		for (int id : goldBagIdList) {
			GoldBagView gbv = goldBags.get(id);
			gbv.toFront();
		}

		for (int id : EntityController.getEffectIDs()) {
			if (!effectViews.containsKey(id)) {
				EffectView ev = new EffectView(id);
			}
		}

		for (EffectView ev : new ArrayList<>(effectViews.values())) {
			ev.update(deltaTime);
		}
	}

	private void pauseResumeMenuButtons() {
		ImageView gearView = new ImageView(gearButtonImage);
		ImageView accelerateView = new ImageView(accelerateButtonImage);
		ImageView pauseView = new ImageView(pauseButtonImage);
		gearView.setFitWidth(40);
		gearView.setFitHeight(40);
		accelerateView.setFitHeight(40);
		accelerateView.setFitWidth(40);
		pauseView.setFitHeight(40);
		pauseView.setFitWidth(40);

		hbox = new HBox(accelerateView,pauseView,gearView);
		hbox.setLayoutX(1150);
		hbox.setLayoutY(10); 
		hbox.setPickOnBounds(false);

		
		gearView.setOnMouseClicked(e->{
			stop();
			MainMenuController.cleanupSession();
			app.showMainMenu(new StackPane());
		});
		gearView.setOnMouseEntered(e->{
			gearView.setImage(gearHover);
		});
		gearView.setOnMouseExited(e->{
			gearView.setImage(gearButtonImage);
		});
		
		accelerateView.setOnMouseClicked(e->{
			pauseView.setImage(pauseButtonImage);
			if(PlayModeController.getGameSpeed() == 2.0) {
				PlayModeController.decelerateGame();
			}
			else {
				PlayModeController.accelerateGame();

			}

		});
		accelerateView.setOnMouseEntered(e->{
			accelerateView.setImage(accelerateHover);
		});
		accelerateView.setOnMouseExited(e->{
			accelerateView.setImage(accelerateButtonImage);
		});
		
		pauseView.setOnMouseClicked(e->{
			if(PlayModeController.getGameSpeed() == 0) {
				PlayModeController.resumeGame();
				pauseView.setImage(pauseButtonImage);
			}
			else {
				PlayModeController.pauseGame();
				pauseView.setImage(resumeButtonImage);
			}
		});
		
		
		pauseView.setOnMouseEntered(e->{
			if(PlayModeController.getGameSpeed() == 0) {
				pauseView.setImage(resumeHover);
			}
			else {
				pauseView.setImage(pauseHover);
			}
		});
		pauseView.setOnMouseExited(e->{
			if(PlayModeController.getGameSpeed() == 0) {
				pauseView.setImage(resumeButtonImage);
			}
			else {
				pauseView.setImage(pauseButtonImage);
			}
		});
		
		hudPane.getChildren().addAll(hbox);
	}
}