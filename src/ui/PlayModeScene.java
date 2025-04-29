package ui;

import domain.controller.MapEditorController;
import domain.controller.PlayModeController;
import domain.controller.PlayerController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayModeScene {
	private KuTowerDefenseA app;
	private Group towerSelection = null;
	private Circle rangeCircle = null;
	private Group circle = null;
	private MapEditorController mapEditorController = MapEditorController.getInstance();
	private Pane rootPane;
	private Group removeSelection = null;

	public PlayModeScene(KuTowerDefenseA app) {
		this.app = app;
	}
	
	public Scene getScene() {
		rootPane = renderMap();
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
				ImageView grassView = new ImageView(grassImage);
				
				double fit = PlayModeController.getTileLength() * renderScale;
				grassView.setFitWidth(fit);  
				grassView.setFitHeight(fit);

				double x = PlayModeController.getTileXCoord(j, i) * renderScale - (fit) / 2.0;
				double y = PlayModeController.getTileYCoord(j, i) * renderScale - (fit) / 2.0;

				grassView.setLayoutX(x);
				grassView.setLayoutY(y);
				
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
				
				double fit = PlayModeController.getTileLength() *renderScale;
				double x = PlayModeController.getTileXCoord(j, i) * renderScale - (fit) / 2.0;
				double y = PlayModeController.getTileYCoord(j, i) * renderScale - (fit) / 2.0;
				
				if(assetName.equals("hugetower")) {
					if(castleRender) {
						continue;
					}	
					castleMaker(map, i ,j);
					castleRender = true;
					continue;
				}
				
				Image tileImage = new Image(getClass().getResourceAsStream("/Images/"+assetName+".png"));
				ImageView tileView = new ImageView(tileImage);
				
				tileView.setFitWidth(fit);  
				tileView.setFitHeight(fit);
				tileView.setLayoutX(x);
				tileView.setLayoutY(y);
				
				if(assetName.equals("lot")) {
					tileView.setOnMouseClicked(event ->{
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
						showDeleteCircle(tileView);
					});
				}
				else if(assetName.equals("magetower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
					tileView.setOnMouseClicked(event->{
						removeRange();
						showDeleteCircle(tileView);

					});
					
				}
				else if(assetName.equals("artillerytower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
					tileView.setOnMouseClicked(event->{
						removeRange();
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
			ImageView castleView = new ImageView(castleImage);
			
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
			mapEditorController.createArcherTower(x, y);
			rootPane.getChildren().clear();
			Pane newMap = renderMap();
			rootPane.getChildren().add(newMap);
			playerStatsPutter();
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("aaaa");

		});
	    mageButton.setOnMouseClicked(event -> {
	    	removeCircle(towerSelection);
			mapEditorController.createMageTower(x, y);
			rootPane.getChildren().clear();
			Pane newMap = renderMap();
			rootPane.getChildren().add(newMap);
			playerStatsPutter();
			System.out.print(PlayerController.getPlayerGold());
			System.out.println("mmmmm");

	    });
	    artilleryButton.setOnMouseClicked(event->{
	    	removeCircle(towerSelection);
			mapEditorController.createArtilleryTower(x, y);
			rootPane.getChildren().clear();
			Pane newMap = renderMap();
			rootPane.getChildren().add(newMap);
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
            Pane newMap = renderMap();
			rootPane.getChildren().add(newMap);
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
		Image coinImage  = new Image(getClass().getResourceAsStream("/Images/coin.png"));
        ImageView coinView = new ImageView(coinImage);
        Image heartImage  = new Image(getClass().getResourceAsStream("/Images/health.png"));
        ImageView heartImageView = new ImageView(heartImage);
        Image waveImage  = new Image(getClass().getResourceAsStream("/Images/wave.png"));
        ImageView waveView = new ImageView(waveImage);
        Image infoImage  = new Image(getClass().getResourceAsStream("/Images/blueb.png"));
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
       
        
        Group statCoin = playerStats(coinView, infoView1);
        rootPane.getChildren().addAll(statCoin);

        infoView2.setLayoutX(50);
        infoView2.setLayoutY(50);
        Group statHealth = playerStats(heartImageView, infoView2);
        rootPane.getChildren().addAll(statHealth);

        infoView3.setLayoutX(50);
        infoView3.setLayoutY(100);
        Group statWave = playerStats(waveView, infoView3);
        rootPane.getChildren().addAll(statWave);


        
	}
	
	private Group playerStats(ImageView image, ImageView info) {
		Group stats = new Group(image, info);
		return stats;
        
        
	}
}