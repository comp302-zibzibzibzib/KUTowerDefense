package ui;

import java.io.InputStream;

import domain.map.DecorativeTile;
import domain.map.Lot;
import domain.map.Map;
import domain.map.PathTile;
import domain.map.PathType;
import domain.map.Tile;
import domain.map.TileType;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

public class PlayModeScene {
	private KuTowerDefenseA app;
	private Map gameMap;

	public PlayModeScene(KuTowerDefenseA app, Map gameMap) {
		// TODO Auto-generated constructor stub
		this.app = app;
		this.gameMap = gameMap;
	}
	
	public Scene getScene() {
		Pane root = renderMap();
		Scene scene = new Scene(root);
		return scene;
		
	}
	private Pane grassRender(Pane map) {
		double renderScale = 16;

		for(int i = 0; i<gameMap.height; i++) {
			for(int j = 0; j< gameMap.width; j++) {
				Tile tile = gameMap.tileMap[i][j];
				

				Image grassImage = new Image(getClass().getResourceAsStream("/Images/grass.png"));
				ImageView grassView = new ImageView(grassImage);
				
				double fit = Tile.tileLength *renderScale;
				grassView.setFitWidth(fit);  
				grassView.setFitHeight(fit);
				

				double x = tile.getLocation().getXCoord() * renderScale - (fit) / 2.0;
				double y = tile.getLocation().getYCoord() * renderScale - (fit) / 2.0;

				grassView.setLayoutX(x);
				grassView.setLayoutY(y);
				
				map.getChildren().addAll(grassView);
				
			}
		}
		return map;
	}
	
	private Pane renderMap() {
		Pane map = new Pane();
		Pane grassMap = grassRender(map); 
		String assetName;
		double renderScale = 16;
		boolean castleRender = false;
		
		for(int i = 0; i<gameMap.height; i++) {
			for(int j = 0; j<gameMap.width;j++) {
				Tile tile = gameMap.tileMap[i][j];
				
				assetName = getAssetName(tile);
				
				
				
				double fit = Tile.tileLength *renderScale;
				double x = tile.getLocation().getXCoord() * renderScale - (fit) / 2.0;
				double y = tile.getLocation().getYCoord() * renderScale - (fit) / 2.0;

				

				Pane tilePane = new Pane();
				tilePane.setPrefSize(fit, fit);
				tilePane.setStyle("-fx-border-color: gray; -fx-border-width: 0.3;");
				tilePane.setLayoutX(x);
				tilePane.setLayoutY(y);
				
				if(assetName.equals("hugetower")) {

					if(castleRender) {
						continue;
					}	
					castleMaker(grassMap, i ,j);

					castleRender = true;
					continue;
				}
				
				Image tileImage = new Image(getClass().getResourceAsStream("/Images/"+assetName+".png"));
				ImageView tileView = new ImageView(tileImage);
				
				tileView.setFitWidth(fit);  
				tileView.setFitHeight(fit);
				tileView.setLayoutX(x);
				tileView.setLayoutY(y);
				
				

				
				grassMap.getChildren().addAll(tileView);
				grassMap.getChildren().addAll(tilePane);

				
				
			}
		}
		return grassMap;
		
	}
	
	private void castleMaker(Pane pane , int i , int j) {
		Tile castleTopLeft = gameMap.tileMap[i][j];
		Tile castleTopRight = gameMap.tileMap[i][j+1];
		Tile castleBottomLeft = gameMap.tileMap[i+1][j];
		Tile castleBottomRight = gameMap.tileMap[i+1][j+1];
		
		
		double x = castleTopLeft.getLocation().getXCoord() * 16 - (80) / 2.0;
		double y = castleTopLeft.getLocation().getYCoord() * 16 - (80) / 2.0;
		double x_1 = castleTopRight.getLocation().getXCoord() * 16 - (80) / 2.0;
		double y_1 = castleTopRight.getLocation().getYCoord() * 16 - (80) / 2.0;
		double x_2 = castleBottomLeft.getLocation().getXCoord() * 16 - (80) / 2.0;
		double y_2 = castleBottomLeft.getLocation().getYCoord() * 16 - (80) / 2.0;
		double x_3 = castleBottomRight.getLocation().getXCoord() * 16 - (80) / 2.0;
		double y_3 = castleBottomRight.getLocation().getYCoord() * 16 - (80) / 2.0;
		
	
		Image castleTopleft = new Image(getClass().getResourceAsStream("/Images/hugetowertopleft.png"));
		ImageView castleTopLeftView = new ImageView(castleTopleft);
		
		castleTopLeftView.setFitWidth(80);  
		castleTopLeftView.setFitHeight(80);
		castleTopLeftView.setLayoutX(x);
		castleTopLeftView.setLayoutY(y);
		
		
		Image castleTopright = new Image(getClass().getResourceAsStream("/Images/hugetowertopright.png"));
		ImageView castleToprightView = new ImageView(castleTopright);
		
		castleToprightView.setFitWidth(80);  
		castleToprightView.setFitHeight(80);
		castleToprightView.setLayoutX(x_1);
		castleToprightView.setLayoutY(y_1);
		
		
		Image castleBottomLeftImage = new Image(getClass().getResourceAsStream("/Images/hugetowerbottomleft.png"));
		ImageView castleBottomLeftView = new ImageView(castleBottomLeftImage);
		
		castleBottomLeftView.setFitWidth(80);  
		castleBottomLeftView.setFitHeight(80);
		castleBottomLeftView.setLayoutX(x_2);
		castleBottomLeftView.setLayoutY(y_2);
		
		
		
		Image castleBottomRightImage = new Image(getClass().getResourceAsStream("/Images/hugetowerbottomright.png"));
		ImageView castleBottomRightView = new ImageView(castleBottomRightImage);
		
		castleBottomRightView.setFitWidth(80);  
		castleBottomRightView.setFitHeight(80);
		castleBottomRightView.setLayoutX(x_3);
		castleBottomRightView.setLayoutY(y_3);
		Pane linePane = new Pane();
		linePane.setPrefSize(80, 80);
		linePane.setStyle("-fx-border-color: gray; -fx-border-width: 0.3;");
		linePane.setLayoutX(x);
		linePane.setLayoutY(y);
		
		Pane linePane1 = new Pane();
		linePane1.setPrefSize(80, 80);
		linePane1.setStyle("-fx-border-color: gray; -fx-border-width: 0.3;");
		linePane1.setLayoutX(x_1);
		linePane1.setLayoutY(y_1);
		
		Pane linePane2 = new Pane();
		linePane2.setPrefSize(80, 80);
		linePane2.setStyle("-fx-border-color: gray; -fx-border-width: 0.3;");
		linePane2.setLayoutX(x_2);
		linePane2.setLayoutY(y_2);
		
		Pane linePane3 = new Pane();
		linePane3.setPrefSize(80, 80);
		linePane3.setStyle("-fx-border-color: gray; -fx-border-width: 0.3;");
		linePane3.setLayoutX(x_3);
		linePane3.setLayoutY(y_3);
		
		pane.getChildren().addAll(castleTopLeftView,castleToprightView,castleBottomLeftView,castleBottomRightView);
		
		pane.getChildren().addAll(linePane,linePane1,linePane2,linePane3);

	}
	
	private String getAssetName(Tile tile) {
		String name = null;
		
		if (tile.type.equals(TileType.GRASS)) {
			name = "grass";
		}
		else if (tile.getType().equals(TileType.PATH)) {
			PathTile path = (PathTile) tile;
			name = path.getPathType().getAssetName();
			
		}
		else if(tile.getType().equals(TileType.DECORATIVES)) {
			DecorativeTile decorative = (DecorativeTile) tile;
			name = decorative.getDecorativeType().getAssetName();
		}
		
		else if(tile.getType().equals(TileType.CASTLE)) {
			name ="hugetower";
		}
		
		else if(tile.getType().equals(TileType.TOWER)) {
			Lot tower = (Lot) tile;
			name = tower.getTowerType().getAssetName();
			
		}
		else if(tile.getType().equals(TileType.LOT)) {
			name = "lot";
		}
		return name;
		
	}

}
