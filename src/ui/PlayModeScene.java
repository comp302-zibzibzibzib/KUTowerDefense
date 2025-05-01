package ui;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import domain.controller.EntityController;
import domain.controller.GameOptionsController;
import domain.controller.MapEditorController;
import domain.controller.PlayModeController;
import domain.controller.PlayerController;
import domain.controller.TowerController;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class PlayModeScene extends AnimationTimer {
	private KuTowerDefenseA app;
	private Group towerSelection = null;
	private TileView selectedLot = null;
	private TileView selectedTower = null;
	private Circle rangeCircle = null;
	private Group circle = null;
	private MapEditorController mapEditorController = MapEditorController.getInstance();
	private Pane rootPane;
	private Pane overlayPane;
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

	private HBox hbox;

	private Map<Integer, EnemyStack> enemyStacks = new HashMap<Integer, EnemyStack>();
	private Map<String, List<Image>> enemyAnimations = new HashMap<>();
	private int frameCounter = 0;

	private double totalElapsed = 0;
	private long lastUpdate = 0;
	private int frameIndex = 0;
	
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
		private Image barEmpty = new Image(getClass().getResourceAsStream("/Images/HUD/bar.png"));
		private Image barFull = new Image(getClass().getResourceAsStream("/Images/HUD/bar_full.png"));
		
		private ImageView emptyView;
		private ImageView fullView;
		private Rectangle clip;
		
		public HealthBar() {
			super();
			emptyView = new ImageView(barEmpty);
			fullView = new ImageView(barFull);
			emptyView.setFitWidth(45);
			emptyView.setFitHeight(10);
			fullView.setFitWidth(45);
			fullView.setFitHeight(10);
			
			clip = new Rectangle(45, 10);
			fullView.setClip(clip);
			
			this.getChildren().addAll(emptyView, fullView);
		}
		
		public void setPercentage(double percentage) {
			if (percentage < 0) percentage = 0;
			else if (percentage > 1) percentage = 1;
			clip.setWidth(percentage * 45);
		}
	}
	
	private class EnemyStack extends StackPane {
		private ImageView enemyView;
		private HealthBar healthBar;
		
		public EnemyStack() {
			enemyView = new ImageView();
			healthBar = new HealthBar();
			
			enemyView.setFitWidth(128);
            enemyView.setFitHeight(128);
            healthBar.setTranslateY(35);
            
            this.getChildren().addAll(enemyView, healthBar);
		}
		
		public void setImage(Image image) {
			enemyView.setImage(image);
		}
		
		public ImageView getEnemyView() {
			return enemyView;
		}
	}
	

	public PlayModeScene(KuTowerDefenseA app) {
		this.app = app;
	}
	
	public Scene getScene() {
		loadEnemyFrames();
		rootPane = renderMap();
		overlayPane = new Pane();
		overlayPane.setMouseTransparent(true);
		overlayPane.setPickOnBounds(false);
    pauseResumeButton();
		StackPane stack = new StackPane(rootPane, overlayPane);
		EntityController.startEntityLogic();
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
				
				if (assetName.equals("hugetower")) {
					if (j + 1 < width && i + 1 < height) {
						String r = PlayModeController.getAssetName(j + 1, i);
						String d = PlayModeController.getAssetName(j, i + 1);
						String dr = PlayModeController.getAssetName(j + 1, i + 1);

						if (r.equals("hugetower") && d.equals("hugetower") && dr.equals("hugetower")) {
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
			System.out.println("mmmmm");

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

	    ((Pane) lot.getParent()).getChildren().add(towerSelection);
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

        deleteButton.setOnMouseClicked(event -> {
            removeCircle(removeSelection);
            mapEditorController.removeTower(x,y);
            
            Pane parent = (Pane) selectedTower.getParent();
            
            if (selectedTower == null) return;
            parent.getChildren().remove(selectedTower);
            selectedTower = null;
            
            Image lotImage = new Image(getClass().getResourceAsStream("/Images/lot.png"));
            TileView lotView = new TileView(lotImage, x, y);
            
            lotView.setOnMouseClicked(event2 ->{
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

        ((Pane) tower.getParent()).getChildren().add(removeSelection);

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
        Label lives = new Label(String.format("%d/%d", PlayerController.getPlayerLives(),GameOptionsController.getStartingLives()));
        Label waves = new Label(String.format("%d/%d", PlayerController.getWaveNumber(),GameOptionsController.getNumberOfWaves()));
        
        
        coin.setLayoutX(80);
        coin.setLayoutY(7);
        
        lives.setLayoutX(75);
        lives.setLayoutY(57);

        waves.setLayoutX(80);
        waves.setLayoutY(107);
        
        Font font = Font.font("Comic Sans MS",FontWeight.BOLD,FontPosture.REGULAR, 18);  
        coin.setFont(font);
        lives.setFont(font);
        waves.setFont(font);
        
        coin.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
        lives.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
        waves.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(one-pass-box, black, 5, 0.5, 0, 0);");
        
        Consumer<Integer> goldAction = (val) -> coin.setText(String.format("%d", PlayerController.getPlayerGold()));
        Consumer<Integer> livesAction = (val) -> lives.setText(String.format("%d/%d", PlayerController.getPlayerLives(),
        																		GameOptionsController.getStartingLives()));
        Consumer<Integer> wavesAction = (val) -> waves.setText(String.format("%d/%d", PlayerController.getWaveNumber(), 
        																		GameOptionsController.getNumberOfWaves()));
        
        PlayerController.addGoldListener(goldAction);
        PlayerController.addLivesListener(livesAction);
        PlayerController.addWaveNumberListener(wavesAction);
        
        Group statCoin = playerStats(coinView, infoView1,coin);
        overlayPane.getChildren().addAll(statCoin);

        infoView2.setLayoutX(50);
        infoView2.setLayoutY(50);
        Group statHealth = playerStats(heartImageView, infoView2,lives);
        overlayPane.getChildren().addAll(statHealth);

        infoView3.setLayoutX(50);
        infoView3.setLayoutY(100);
        Group statWave = playerStats(waveView, infoView3,waves);
        overlayPane.getChildren().addAll(statWave);
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
		lastUpdate = arg0;
		totalElapsed += deltaTime;
		
		frameCounter++;
        
		if (totalElapsed >= 0.1) {
        	frameIndex = (frameIndex + 1) % 6;
        	totalElapsed = 0;
        }

		for (int i = 0; i < EntityController.getNumberOfEnemies(); i++) {
	        if (!EntityController.isEnemyInitialized(i)) continue;
	        
	        String type = null;
	        if (EntityController.isGoblin(i)) type = "goblin";
	        if (EntityController.isKnight(i)) type = "warrior";
	        
	        List<Image> frames = enemyAnimations.get(type);

	        double x = EntityController.getEnemyXCoord(i);
	        double y = EntityController.getEnemyYCoord(i);
	        int id = EntityController.getEnemyID(i);

	        // If this enemy doesn't have an ImageView yet
	        if (!enemyStacks.containsKey(id)) {
	        	EnemyStack es = new EnemyStack();
	        	es.healthBar.setPercentage(random.nextDouble(0.3,1));
	        	overlayPane.getChildren().add(es);
	            enemyStacks.put(id, es);
	        }

	        EnemyStack es = enemyStacks.get(id);
	        es.setImage(frames.get(frameIndex));
	        int scale = EntityController.getXScale(i);
	        if (scale != 0) {
	        	es.getEnemyView().setScaleX(scale);
	        }
	        
	        es.setLayoutX(x * 16 - es.getEnemyView().getFitWidth()/2);  // adjust scale as needed
	        es.setLayoutY(y * 16 - es.getEnemyView().getFitHeight()/2);
	    }
		
		if (rangeCircle != null) {
			rangeCircle.toFront();
		}

		enemyStacks.entrySet().removeIf(entry -> {
	        int id = entry.getKey();
	        if (!EntityController.isEnemyIDInitialized(id)) {
	        	overlayPane.getChildren().remove(entry.getValue());
	            return true;
	        }
	        return false;
		});
	}

	private void pauseResumeButton() {
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
			EntityController.stop();
			PlayModeController.resetManager();
			app.showMainMenu(new StackPane());
		});
		gearView.setOnMouseEntered(e->{
			gearView.setImage(gearHover);
		});
		gearView.setOnMouseExited(e->{
			gearView.setImage(gearButtonImage);
		});
		
		accelerateView.setOnMouseClicked(e->{
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
		
		rootPane.getChildren().addAll(hbox);
	}
}