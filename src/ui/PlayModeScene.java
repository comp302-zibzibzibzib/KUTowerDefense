package ui;

import java.io.InputStream;

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
	
	private Pane renderMap() {
		Pane map = new Pane();
		String assetName;
		double renderScale = 16;
		
		for(int i = 0; i<gameMap.height; i++) {
			for(int j = 0; j<gameMap.width;j++) {
				Tile tile = gameMap.tileMap[i][j];
				
				assetName = getAssetName(tile);
				
				Image tileImage = new Image(getClass().getResourceAsStream("/Images/"+ assetName +".png"));
		        System.out.println("Image size: " + tileImage.getWidth() + "x" + tileImage.getHeight());  
				ImageView tileView = new ImageView(tileImage);
				
				double fit = Tile.tileLength *renderScale;
				tileView.setFitWidth(fit);  
				tileView.setFitHeight(fit);
				

				double x = tile.getLocation().getXCoord() * renderScale - (fit) / 2.0;
				double y = tile.getLocation().getYCoord() * renderScale - (fit) / 2.0;

				tileView.setLayoutX(x);
				tileView.setLayoutY(y);
				
				Pane tilePane = new Pane();
				tilePane.setPrefSize(fit, fit);
				tilePane.setStyle("-fx-border-color: red; -fx-border-width: 1;");
				tilePane.setLayoutX(x);
				tilePane.setLayoutY(y);

				map.getChildren().addAll(tileView);
				map.getChildren().addAll(tilePane);
				
				
			}
		}
		return map;
		
	}
	
	private String getAssetName(Tile tile) {
		String name = null;
		
		if (tile.type.equals(TileType.GRASS)) {
			name = "grass";
		}
		else if (tile.getType().equals(TileType.PATH)) {
			PathTile path = (PathTile) tile;
			if(path.getPathType().equals(PathType.TOPLEFT)) {
				
			}
			
		}
		return name;
		
	}

}
