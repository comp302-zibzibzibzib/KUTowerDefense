package ui;

import domain.controller.PlayModeController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayModeScene {
	private KuTowerDefenseA app;

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

				Pane tilePane = new Pane();
				tilePane.setPrefSize(fit, fit);
				tilePane.setStyle("-fx-border-color: gray; -fx-border-width: 0.001;");
				tilePane.setLayoutX(x);
				tilePane.setLayoutY(y);
				
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
				
				map.getChildren().addAll(tileView);
				map.getChildren().addAll(tilePane);
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
			linePane.setStyle("-fx-border-color: gray; -fx-border-width: 0.001;");
			linePane.setLayoutX(x);
			linePane.setLayoutY(y);
			
			pane.getChildren().addAll(castleView);
			
			pane.getChildren().addAll(linePane);
		}
	}
}
