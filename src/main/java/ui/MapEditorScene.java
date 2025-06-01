package ui;

import domain.controller.MapEditorController;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MapEditorScene {
	private KuTowerDefenseA app;
	private MapEditorController mapEditorController = MapEditorController.getInstance();
	Pane editedMap = new Pane();
	Pane mapForEditor = new Pane();
	StackPane root = new StackPane();
	Pane draggingLayer = new Pane();
	Pane biggerSideBar = new Pane();
	Pane exitBar = new Pane();
    Pane optionBar = new Pane();
	Pane sideBar = new Pane();
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
	private AssetImage saveButton = new AssetImage("savebutton", this);
	private AssetImage exitButton = new AssetImage("exitbutton", this);

	
	public MapEditorScene(KuTowerDefenseA app) {
		System.out.println(getClass());
		this.app = app;
	}
	
	public Scene getScene() {
		Pane dashedLineMap = dashedLines(9,16,70,70);
		dashedLineMap.setPickOnBounds(false);
		draggingLayer.setPickOnBounds(false);
		editedMap.setPickOnBounds(false);
		HBox content = new HBox(renderMapEditor(), getSideBar());
		root.getChildren().addAll(content,editedMap,dashedLineMap, draggingLayer);
		Scene scene = new Scene(root);
		return scene;
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
		System.out.println(getClass());

		biggerSideBar.setPrefSize(255,630);
        biggerSideBar.setStyle("-fx-background-color: #434447;");
        
		exitBar.setPrefSize(254,62.5);
		exitBar.setLayoutX(4);
		exitBar.setLayoutY(0);
        exitBar.setStyle("-fx-background-color: #8FD393;");
        
        optionBar.setPrefSize(254, 62.5);
        optionBar.setLayoutX(4);
		optionBar.setLayoutY(63.5);
        optionBar.setStyle("-fx-background-color: #8FD393;");
        
        ImageView saveView = new ImageView(saveButton);
        ImageView exitView = new ImageView(exitButton);
        exitView.setFitHeight(57);
        exitView.setFitWidth(57);
        exitView.setLayoutX(194);
        exitView.setLayoutY(0);

        saveView.setFitHeight(57);
        saveView.setFitWidth(57);
        saveView.setLayoutX(194);
        saveView.setLayoutY(0);
        
        saveView.setOnMouseClicked(e->{
        	boolean valid = mapEditorController.validateMap();
        	
        	if(!valid) {
        		
        	}
        	else {
        		
        		mapEditorController.saveMap();
        	}
        });
        
        optionBar.getChildren().addAll(saveView);
        
        exitBar.getChildren().addAll(exitView);

		sideBar.setPrefSize(250, 630); 
        sideBar.setStyle(" -fx-background-color: #8FD393;");
        sideBar.setLayoutX(4);
		sideBar.setLayoutY(127);
		
		
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
		ImageView treeFirstView = createTileImageView(treeFirst, 0, 250);
		ImageView treeSecondView = createTileImageView(treeSecond, 62.5, 250);
		ImageView treeThirdView = createTileImageView(treeThird, 125, 250);
		ImageView rockFirstView = createTileImageView(rockFirst, 187.5, 250);
		ImageView artilleryTowerView = createTileImageView(artilleryTower, 0, 312.5);
		ImageView mageTowerView = createTileImageView(mageTower, 62.5, 312.5);
		ImageView houseView = createTileImageView(house, 125, 312.5);
		ImageView rockSecondView = createTileImageView(rockSecond, 187.5, 312.5);
		ImageView castleView = createTileImageView(castle, 0, 375);
		castleView.setFitHeight(125);
		castleView.setFitWidth(125);
		ImageView archerTowerView = createTileImageView(archerTower, 125, 375);
		ImageView wellView = createTileImageView(well, 187.5, 375);
		ImageView houseSecondView = createTileImageView(houseSecond, 125, 437.5);
		ImageView woodView = createTileImageView(wood, 187.5, 437.5);


		sideBar.getChildren().addAll(topBot,topMid, topRight,horizontalTop,midL,grassView,midRightView,horizontalMidView,leftBotomView,midBottomView,midBottomRightView,horizontalBottomView,verticalLeftView,verticalMidView,verticalRightView,lotView,treeFirstView,treeSecondView, treeThirdView,rockFirstView, artilleryTowerView,mageTowerView,houseView,rockSecondView,castleView, archerTowerView,wellView,houseSecondView,woodView);
		

		Pane dashedLineSide = dashedLines(20,4,62.5,62.5);
		dashedLineSide.setPickOnBounds(false);
		dashedLineSide.setMouseTransparent(true);
		sideBar.getChildren().add(dashedLineSide);
		
		highlightEffect();
		

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
			String tileName = (String) image.getUserData();
			Image originalImage = new Image(getClass().getResourceAsStream("/Images/" + tileName + ".png"));
			draggingClone = new ImageView(originalImage);
			draggingClone.setUserData(image.getUserData());
	        draggingClone.setFitWidth(image.getFitWidth());
	        draggingClone.setFitHeight(image.getFitHeight());
	        draggingClone.setLayoutX(event.getSceneX() - image.getFitWidth() / 2);
	        draggingClone.setLayoutY(event.getSceneY() - image.getFitHeight() / 2);
	        draggingLayer.getChildren().add(draggingClone);
	    });

		image.setOnMouseDragged(event -> {
	        if (draggingClone != null) {
	            draggingClone.setLayoutX(event.getSceneX() - draggingClone.getFitWidth() / 2);
	            draggingClone.setLayoutY(event.getSceneY() - draggingClone.getFitHeight() / 2);
	        }
	    });

		image.setOnMouseReleased(event -> {
	        if (draggingClone != null) {
	        	//getting our mouse's coordinates
	        	double mouseX = event.getSceneX();
	        	double mouseY = event.getSceneY();
	        	
	        	
	        	for(Node node : mapForEditor.getChildren()) {
	        		if (node instanceof ImageView) {	 
	        	        ImageView tile = (ImageView) node;
	        	        
	        	        Bounds bounds = tile.localToScene(tile.getBoundsInLocal());
	        	        System.out.println(bounds);
	        	        
	        	        if (bounds.contains(mouseX, mouseY)) {
	        	        	 double centerX = bounds.getMinX() + bounds.getWidth() / 2;
	                         double centerY = bounds.getMinY() + bounds.getHeight() / 2;
	                         System.out.println(centerX);
	                         System.out.println(centerY);

	                         draggingClone.setFitHeight(70);
	                         draggingClone.setFitWidth(70);

	                         draggingClone.setLayoutX(centerX - draggingClone.getFitWidth() / 2);
	                         draggingClone.setLayoutY(centerY - draggingClone.getFitHeight() / 2);
	                         

	                         double cellX = draggingClone.getLayoutX();
	                         double cellY = draggingClone.getLayoutY();

	                         
	                         for (int dx = 0; dx <= 70; dx++) {
	                        	    for (int dy = 0; dy <= 70; dy++) {
	                        	        double tx = cellX - dx;
	                        	        double ty = cellY - dy;
	                        	        editedMap.getChildren().removeIf(cImage -> {
	                        	            if (!(cImage instanceof ImageView)) return false;
	                        	            double tileX = cImage.getLayoutX();
	                        	            double tileY = cImage.getLayoutY();
	                        	            return tileX == tx && tileY == ty;
	                        	        });
	                        	    }
	                        	}
	                         String tileName = (String) draggingClone.getUserData();
	                         
	                         if(tileName.equals("castle")) {
	                        	 
	                        	 mapEditorController.forcePlaceCastle((int)(draggingClone.getLayoutX()/70),(int)(draggingClone.getLayoutY()/70));
	     	            		 draggingClone.setFitHeight(140);
		                         draggingClone.setFitWidth(140);
	     	            		editedMap.getChildren().add(draggingClone);
	     	            		
	     	            		break;
	     	            	 }
	     	            	 if(tileName.equals("lot")) {
	     	            		mapEditorController.forcePlaceLot((int)(draggingClone.getLayoutX()/70),(int)(draggingClone.getLayoutY()/70));
	     	            		 
	     	            	 }
	     	            	 else {
	     	            		mapEditorController.forcePlaceTile(tileName, (int)(draggingClone.getLayoutX()/70),(int)(draggingClone.getLayoutY()/70));
	     	            	 }
	     	            
	                         if(tileName.equals("grass")) {
	                        	 editedMap.getChildren().remove(draggingClone);
	                        	 mapEditorController.removeTile((int)(draggingClone.getLayoutX()/70),(int)(draggingClone.getLayoutY()/70));
	                        	 mapEditorController.forcePlaceTile("grass",(int)(draggingClone.getLayoutX()/70),(int)(draggingClone.getLayoutY()/70));
	                         }
	                         else {
	                        	 editedMap.getChildren().add(draggingClone);
	                        	 
	                         }
	                         break;
	        	        }
	        		}
	        		
	        	}
	        	
	            draggingLayer.getChildren().remove(draggingClone);
	            draggingClone = null;
	            System.out.println(editedMap.getChildren());
	            mapEditorController.printMap();

	           
	        }
	    });
	}
	private void highlightEffect() {
		for(Node node : sideBar.getChildren()) {
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
	private class AssetImage extends Image{
		private String name;
		
		public AssetImage(String name, Object source) {
			super(source.getClass().getResourceAsStream("/Images/"+name+ ".png"));
			System.out.println(source.getClass());
			this.name = name;
			
		}

		public String getName() {
			return name;
		}
		
	}	
	

}

