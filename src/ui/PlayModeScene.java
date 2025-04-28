package ui;

import domain.controller.PlayModeController;
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

	public PlayModeScene(KuTowerDefenseA app) {
		
		this.app = app;
	}
	
	public Scene getScene() {
		Pane root = renderMap();
		Scene scene = new Scene(root);
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
						removeCircle();
					});
				}
				if(assetName.equals("archertower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
				}
				else if(assetName.equals("magetower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
					});
				}
				else if(assetName.equals("artillerytower")) {
					tileView.setOnMouseEntered(event -> {
						rangeRender(tileView);
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
			
			Pane linePane = new Pane();
			linePane.setPrefSize(80, 80);
			linePane.setStyle("-fx-border-color: gray; -fx-border-width: 0.3;");
			linePane.setLayoutX(x);
			linePane.setLayoutY(y);
			
			pane.getChildren().addAll(castleView);
			
			
		}
	}
	
	private void showLotCircle(ImageView lot) {
		if (towerSelection != null) {
		        ((Pane) towerSelection.getParent()).getChildren().remove(towerSelection);
		}
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
	    archerButton.setLayoutX(centerX- 25);
	    archerButton.setLayoutY(centerY - 80);
	    
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
			buyTower(lot);
			System.out.println("aaaa");

		});
	    mageButton.setOnMouseClicked(event -> {
	    	buyTower(lot);
			System.out.println("mmmmm");

	    });
	    artilleryButton.setOnMouseClicked(event->{
	    	buyTower(lot);
			System.out.println("arrrr");

	    });
	    
	    towerSelection = new Group(circleView, archerButton,mageButton,artilleryButton);

	    ((Pane) lot.getParent()).getChildren().add(towerSelection);
	}
	
	private void removeCircle() {
		if (towerSelection != null && towerSelection.getParent() != null) {
	        ((Pane) towerSelection.getParent()).getChildren().remove(towerSelection);
	        towerSelection = null;
	    }	
	}
	
	private void buyTower(ImageView lot) {
	}
	
	private void rangeRender(ImageView tower) {
	 if (circle != null && circle.getParent() != null) {
	        ((Pane) circle.getParent()).getChildren().remove(circle);
	    }
		double centerX = tower.getLayoutX() + tower.getFitWidth() / 2;
	    double centerY = tower.getLayoutY() + tower.getFitHeight() / 2;
	    double range = 100;
	    
	    rangeCircle = new Circle(centerX,centerY,range);
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
}
