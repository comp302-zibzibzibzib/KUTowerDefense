package ui;

import java.security.SecureRandom;
import java.util.*;
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
	private Image upgradeHover = new Image(getClass().getResourceAsStream("/Images/upgradehover.png"));

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
		
		playerStatsPutter();
		Scene scene = new Scene(stack);
		return scene;
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
						showTowerOverlay(tileView, "lot",null);
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
						showTowerOverlay(tileView, "tower","archertower");
					});
				}
				else if(assetName.equals("magetower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
					tileView.setOnMouseClicked(event->{
						removeRange();
						selectedTower = tileView;
						showTowerOverlay(tileView, "tower","magetower");

					});
					
				}
				else if(assetName.equals("artillerytower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
					tileView.setOnMouseClicked(event->{
						removeRange();
						selectedTower = tileView;
						showTowerOverlay(tileView, "tower","artillerytower");

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


	private void showTowerOverlay(ImageView tile, String mode,String towerName){
		removeCircle(mode.equals("lot") ? towerSelection : removeSelection);

		int x = (int)(tile.getLayoutX() / 80);
		int y = (int)(tile.getLayoutY() / 80);
		double centerX = tile.getLayoutX() + tile.getFitWidth() / 2;
		double centerY = tile.getLayoutY() + tile.getFitHeight() / 2;

		ImageView circleView = new ImageView(circleImage);
		circleView.setFitWidth(100);
		circleView.setFitHeight(100);
		circleView.setLayoutX(centerX - 50);
		circleView.setLayoutY(centerY - 50);

		Group overlayGroup = new Group(circleView);

		if (mode.equals("lot")) {
			ImageView archerView = new ImageView(archerButtonImage);
			ImageView mageView = new ImageView(mageButtonImage);
			ImageView artilleryView = new ImageView(artilleryButtonImage);
			archerView.setFitWidth(40);
			archerView.setFitHeight(40);
			mageView.setFitWidth(40);
			mageView.setFitHeight(40);
			artilleryView.setFitWidth(40);
			artilleryView.setFitHeight(40);
			Button archer = new Button(null, archerView);
			Button mage = new Button(null, mageView);
			Button artillery = new Button(null, artilleryView);

			archer.setPrefSize(40, 40);
			mage.setPrefSize(40, 40);
			artillery.setPrefSize(40, 40);
			archer.setStyle("-fx-background-color: transparent;");
			mage.setStyle("-fx-background-color: transparent;");
			artillery.setStyle("-fx-background-color: transparent;");

			if (tile.getLayoutY() == 0) {
				archer.setLayoutX(centerX - 27);
				archer.setLayoutY(centerY + 25);
			} else {
				archer.setLayoutX(centerX - 25);
				archer.setLayoutY(centerY - 80);
			}

			mage.setLayoutX(centerX - 75);
			mage.setLayoutY(centerY - 20);

			artillery.setLayoutX(centerX + 20);
			artillery.setLayoutY(centerY - 20);

			archer.setOnMouseClicked(e -> {
				removeCircle(towerSelection);
				if (!TowerController.canBuildArcher()) return;
				mapEditorController.createArcherTower(x, y);
				replaceWithTower(tile, x, y, "archertower");
			});
			mage.setOnMouseClicked(e -> {
				removeCircle(towerSelection);
				if (!TowerController.canBuildMage()) return;
				mapEditorController.createMageTower(x, y);
				replaceWithTower(tile, x, y, "magetower");
			});
			artillery.setOnMouseClicked(e -> {
				removeCircle(towerSelection);
				if (!TowerController.canBuildArtillery()) return;
				mapEditorController.createArtilleryTower(x, y);
				replaceWithTower(tile, x, y, "artillerytower");
			});

			archer.setOnMouseEntered(e -> ((ImageView) archer.getGraphic()).setImage(hoverArcher));
			archer.setOnMouseExited(e -> ((ImageView) archer.getGraphic()).setImage(archerButtonImage));
			mage.setOnMouseEntered(e -> ((ImageView) mage.getGraphic()).setImage(hoverMage));
			mage.setOnMouseExited(e -> ((ImageView) mage.getGraphic()).setImage(mageButtonImage));
			artillery.setOnMouseEntered(e -> ((ImageView) artillery.getGraphic()).setImage(hoverArtillery));
			artillery.setOnMouseExited(e -> ((ImageView) artillery.getGraphic()).setImage(artilleryButtonImage));

			overlayGroup.getChildren().addAll(archer, mage, artillery);
			towerSelection = overlayGroup;
		}
		else if (mode.equals("tower")) {
			ImageView deleteView = new ImageView(new Image(getClass().getResourceAsStream("/Images/delete.png")));
			ImageView upgradeView = new ImageView(new Image(getClass().getResourceAsStream("/Images/upgrade.png")));
			upgradeView.setFitHeight(40);
			upgradeView.setFitWidth(40);
			deleteView.setFitWidth(40);
			deleteView.setFitHeight(40);
			Button deleteButton = new Button(null, deleteView);
			Button upgradeButton = new Button(null, upgradeView);
			upgradeButton.setPrefSize(40, 40);
			deleteButton.setPrefSize(40, 40);
			deleteButton.setStyle("-fx-background-color: transparent;");
			upgradeButton.setStyle("-fx-background-color: transparent;");

			if (tile.getLayoutY() == 0) {
				deleteButton.setLayoutX(centerX - 27);
				deleteButton.setLayoutY(centerY + 25);
				upgradeButton.setLayoutX(centerX - 75);
				upgradeButton.setLayoutY(centerY - 20);

			}
			else if (tile.getLayoutX()/80 == 15) {
				deleteButton.setLayoutX(centerX - 27);
				deleteButton.setLayoutY(centerY - 80);
				upgradeButton.setLayoutX(centerX - 75);
				upgradeButton.setLayoutY(centerY - 20);
				
			}
			else if (tile.getLayoutX() == 0) {
				deleteButton.setLayoutX(centerX - 27);
				deleteButton.setLayoutY(centerY - 80);
				upgradeButton.setLayoutX(centerX + 20);
				upgradeButton.setLayoutY(centerY - 20);
			}
			else if (tile.getLayoutY()/80 == 8) {
				deleteButton.setLayoutX(centerX - 27);
				deleteButton.setLayoutY(centerY - 80);
				upgradeButton.setLayoutX(centerX - 75);
				upgradeButton.setLayoutY(centerY - 20);
			}
			else {
				deleteButton.setLayoutX(centerX - 27);
				deleteButton.setLayoutY(centerY - 80);
				upgradeButton.setLayoutX(centerX - 27);
				upgradeButton.setLayoutY(centerY + 25);
			}

			deleteButton.setOnMouseClicked(e -> {
				removeCircle(removeSelection);
				mapEditorController.removeTower(x, y);

				Pane parent = (Pane) tile.getParent();
				parent.getChildren().remove(tile);
				Image lotImage = new Image(getClass().getResourceAsStream("/Images/lot.png"));
				TileView lotTile = new TileView(lotImage, x, y);
				lotTile.setOnMouseClicked(ev -> {
					selectedLot = lotTile;
					showTowerOverlay(lotTile, "lot",null);
				});
				parent.getChildren().add(lotTile);
			});

			upgradeButton.setOnMouseClicked(e->{
				removeCircle(removeSelection);
				if (!TowerController.canUpgrade()) return;
				mapEditorController.upgradeTower(x, y);

				Pane parent = (Pane) tile.getParent();
				parent.getChildren().remove(tile);

				if(towerName.equals("magetower")){
					Image upMage = new Image(getClass().getResourceAsStream("/Images/upgradedmage.png"));
					TileView upMageView = new TileView(upMage, x, y);
					upMageView.setOnMouseClicked(ev -> {
						selectedLot = upMageView;
						showTowerOverlay(upMageView, "tower","magetower");
					});
					parent.getChildren().add(upMageView);
				}
				else if(towerName.equals("archertower")){
					Image upArcher = new Image(getClass().getResourceAsStream("/Images/upgradedarcher.png"));
					TileView upArcherView = new TileView(upArcher, x, y);
					upArcherView.setOnMouseClicked(ev -> {
						selectedLot = upArcherView;
						showTowerOverlay(upArcherView, "tower","archertower");
					});
					parent.getChildren().add(upArcherView);
				}
				else if(towerName.equals("artillerytower")){
					Image upArt = new Image(getClass().getResourceAsStream("/Images/upgradedartillery.png"));
					TileView upArtView = new TileView(upArt, x, y);
					upArtView.setOnMouseClicked(ev -> {
						selectedLot = upArtView;
						showTowerOverlay(upArtView, "tower","artillerytower");
					});
					parent.getChildren().add(upArtView);
				}

			});

			deleteButton.setOnMouseEntered(e -> deleteView.setImage(deleteHover));
			deleteButton.setOnMouseExited(e -> deleteView.setImage(new Image(getClass().getResourceAsStream("/Images/delete.png"))));
			upgradeButton.setOnMouseEntered(e->upgradeView.setImage(upgradeHover));
			upgradeButton.setOnMouseExited(e -> upgradeView.setImage(new Image(getClass().getResourceAsStream("/Images/upgrade.png"))));

			overlayGroup.getChildren().addAll(deleteButton,upgradeButton);
			removeSelection = overlayGroup;
		}

		overlayPane.getChildren().add(overlayGroup);
		}


	private void replaceWithTower(ImageView oldTile, int x, int y, String towerName) {
		Pane parent = (Pane) oldTile.getParent();
		parent.getChildren().remove(oldTile);
		Image towerImage = new Image(getClass().getResourceAsStream("/Images/" + towerName + ".png"));
		TileView newTile = new TileView(towerImage, x, y);
		newTile.setOnMouseEntered(ev -> rangeRender(newTile));
		newTile.setOnMouseClicked(ev -> {
			selectedTower = newTile;
			removeRange();
			showTowerOverlay(newTile, "tower",towerName);
		});
		parent.getChildren().add(newTile);
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
	private void playerStatsPutter() {
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
        Consumer<Integer> livesAction = (val) -> lives.setText(String.format("%d/%d", PlayerController.getPlayerLives(),
																				GameOptionsController.getOption("Player Lives")));
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
		gameOverBanner.setLayoutX(300);
		gameOverLabel.setLayoutX(300);

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