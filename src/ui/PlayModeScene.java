package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
	private Group removeSelection = null;
	private Map<Integer, ImageView> enemyViews = new HashMap<>();
	private Map<String, List<Image>> enemyAnimations = new HashMap<>();
	private int frameCounter = 0;

	private double totalElapsed = 0;
	private long lastUpdate = 0;
	private int frameIndex = 0;
	
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
	

	public PlayModeScene(KuTowerDefenseA app) {
		this.app = app;
	}
	
	public Scene getScene() {
		loadEnemyFrames();
		rootPane = renderMap();
		EntityController.startEntityLogic();
		this.start();
		
		playerStatsPutter();
		Scene scene = new Scene(rootPane);
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
				
				if(assetName.equals("hugetower")) {
					if(castleRender) {
						continue;
					}	
					castleMaker(map, i ,j);
					castleRender = true;
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
	    
	    Image archerButtonImage  = new Image(getClass().getResourceAsStream("/Images/archerbutton.png"));
	    Image mageButtonImage = new Image(getClass().getResourceAsStream("/Images/magebutton.png"));
	    Image artilleryButtonImage = new Image(getClass().getResourceAsStream("/Images/artillerybutton.png"));
	    ImageView archerButtonView = new ImageView(archerButtonImage);
	    ImageView mageButtonView = new ImageView(mageButtonImage);
	    ImageView artilleryButtonView = new ImageView(artilleryButtonImage);
	    archerButtonView.setFitWidth(40);
	    archerButtonView.setFitHeight(40);
	    mageButtonView.setFitWidth(40);
	    mageButtonView.setFitHeight(40);
	    artilleryButtonView.setFitWidth(40);
	    artilleryButtonView.setFitHeight(40);


	    Image circleImage = new Image(getClass().getResourceAsStream("/Images/circle.png"));
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
	    mageButton.setGraphic(mageButtonView);
	    mageButton.setBackground(null);
	    mageButton.setPrefSize(40, 40);
	    mageButton.setLayoutX(centerX- 80);
	    mageButton.setLayoutY(centerY - 20);
	    
	    Button artilleryButton = new Button();
	    artilleryButton.setBackground(null);
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
			
			playerStatsPutter();
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("aaaa");
			
		});
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
			
			playerStatsPutter();
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("mmmmm");

	    });
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
			
			playerStatsPutter();
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("arrrr");

	    });
	    
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
            deleteButton.setLayoutX(centerX- 27);
             deleteButton.setLayoutY(centerY + 25);
        }
        else {
            deleteButton.setLayoutX(centerX- 25);
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
			playerStatsPutter();
			System.out.print(PlayerController.getPlayerGold());
        });


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
	    rangeCircle.setFill(null);
	    rangeCircle.setStroke(Color.BLUE);
	    
	    circle = new Group(rangeCircle);
	    
	    ((Pane) tower.getParent()).getChildren().add(circle);
	    
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
        Group statCoin = playerStats(coinView, infoView1,coin);
        rootPane.getChildren().addAll(statCoin);

        infoView2.setLayoutX(50);
        infoView2.setLayoutY(50);
        Group statHealth = playerStats(heartImageView, infoView2,lives);
        rootPane.getChildren().addAll(statHealth);

        infoView3.setLayoutX(50);
        infoView3.setLayoutY(100);
        Group statWave = playerStats(waveView, infoView3,waves);
        rootPane.getChildren().addAll(statWave);
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
	        if (!enemyViews.containsKey(id)) {
	            ImageView iv = new ImageView();
	            iv.setFitWidth(128);
	            iv.setFitHeight(128);
	            rootPane.getChildren().add(iv);
	            enemyViews.put(id, iv);
	        }

	        ImageView iv = enemyViews.get(id);
	        iv.setImage(frames.get(frameIndex));
	        
	        iv.setLayoutX(x * 16 - iv.getFitWidth()/2);  // adjust scale as needed
	        iv.setLayoutY(y * 16 - iv.getFitHeight()/2);
	    }
		enemyViews.entrySet().removeIf(entry -> {
	        int id = entry.getKey();
	        if (!EntityController.isEnemyIDInitialized(id)) {
	            rootPane.getChildren().remove(entry.getValue());
	            return true;
	        }
	        return false;
		});
	}
}