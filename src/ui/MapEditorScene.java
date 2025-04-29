package ui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MapEditorScene {
	private KuTowerDefenseA app;
	StackPane root = new StackPane();
	Pane draggingLayer = new Pane();
	private ImageView draggingClone = null;
	
	
	public MapEditorScene(KuTowerDefenseA app) {
		// TODO Auto-generated constructor stub
		this.app = app;
	}
	
	public Scene getScene() {
		draggingLayer.setPickOnBounds(false);
		HBox content = new HBox(renderMapEditor(), getSideBar());
		root.getChildren().addAll(content, draggingLayer);
		Scene scene = new Scene(root);
		return scene;
	}
	
	private Pane renderMapEditor() {
		Pane mapForEditor = new Pane();
		double x;
		double y;
		
		for(int i = 0; i <9; i++) {
			for(int j = 0; j< 16; j++) {
				Image grass = new Image(getClass().getResourceAsStream("/Images/grass.png"));
				ImageView tileView = new ImageView(grass);
				
				tileView.setFitWidth(70);  
				tileView.setFitHeight(70);
				
				x = j* 70;
				y = i* 70;
				
				tileView.setLayoutX(x);
				tileView.setLayoutY(y);
				Pane tilePane = new Pane();
				tilePane.setPrefSize(70, 70);
				tilePane.setStyle("-fx-border-style: dashed; -fx-border-width: 0.8; -fx-border-color: black;");
				tilePane.setLayoutX(x);
				tilePane.setLayoutY(y);
				
				mapForEditor.getChildren().addAll(tileView);
				mapForEditor.getChildren().addAll(tilePane);
				
			}
		}
		
		return mapForEditor;
	}
	
	private Pane getSideBar() {
		Pane biggerSideBar = new Pane();
		biggerSideBar.setPrefSize(255,630);
        biggerSideBar.setStyle("-fx-background-color: #434447;");

		Pane exitBar = new Pane();
		exitBar.setPrefSize(254,62.5);
		exitBar.setLayoutX(4);
		exitBar.setLayoutY(0);
        exitBar.setStyle("-fx-background-color: #8FD393;");
        
        Pane optionBar = new Pane();
        optionBar.setPrefSize(254, 62.5);
        optionBar.setLayoutX(4);
		optionBar.setLayoutY(63.5);
        optionBar.setStyle("-fx-background-color: #8FD393;");



		Pane sideBar = new Pane();
		sideBar.setPrefSize(250, 630); 
        sideBar.setStyle(" -fx-background-color: #8FD393;");
        sideBar.setLayoutX(4);
		sideBar.setLayoutY(127);
		
		Image topbottom = new Image(getClass().getResourceAsStream("/Images/topleft.png"));
		ImageView topBot = new ImageView(topbottom);
		topBot.setFitWidth(62.5);
		topBot.setFitHeight(62.5);
		topBot.setLayoutX(0);
		topBot.setLayoutY(0);
		
		Image topmid = new Image(getClass().getResourceAsStream("/Images/top.png"));
		ImageView topMid = new ImageView(topmid);
		topMid.setFitWidth(62.5);
		topMid.setFitHeight(62.5);
		topMid.setLayoutX(62.5);
		topMid.setLayoutY(0);
		
		Image topright = new Image(getClass().getResourceAsStream("/Images/topright.png"));
		ImageView topRight = new ImageView(topright);
		topRight.setFitWidth(62.5);
		topRight.setFitHeight(62.5);
		topRight.setLayoutX(125);
		topRight.setLayoutY(0);
		
		
		Image horizontalTopEnd = new Image(getClass().getResourceAsStream("/Images/verticalendtop.png"));
		ImageView horizontalTop = new ImageView(horizontalTopEnd);
		horizontalTop.setFitWidth(62.5);
		horizontalTop.setFitHeight(62.5);
		horizontalTop.setLayoutX(187.5);
		horizontalTop.setLayoutY(0);
		
		Image midLeft = new Image(getClass().getResourceAsStream("/Images/left.png"));
		ImageView midL = new ImageView(midLeft);
		midL.setFitWidth(62.5);
		midL.setFitHeight(62.5);
		midL.setLayoutX(0);
		midL.setLayoutY(62.5);
		
		
		Image grass = new Image(getClass().getResourceAsStream("/Images/grass.png"));
		ImageView grassView = new ImageView(grass);
		grassView.setFitWidth(62.5);
		grassView.setFitHeight(62.5);
		grassView.setLayoutX(62.5);
		grassView.setLayoutY(62.5);
		
		
		Image midRight = new Image(getClass().getResourceAsStream("/Images/right.png"));
		ImageView midRightView = new ImageView(midRight);
		midRightView.setFitWidth(62.5);
		midRightView.setFitHeight(62.5);
		midRightView.setLayoutX(125);
		midRightView.setLayoutY(62.5);
		
		
		Image horizontalMid = new Image(getClass().getResourceAsStream("/Images/verticalmiddle.png"));
		ImageView horizontalMidView = new ImageView(horizontalMid);
		horizontalMidView.setFitWidth(62.5);
		horizontalMidView.setFitHeight(62.5);
		horizontalMidView.setLayoutX(187.5);
		horizontalMidView.setLayoutY(62.5);
		
		
		Image leftBottom = new Image(getClass().getResourceAsStream("/Images/bottomleft.png"));
		ImageView leftBotomView = new ImageView(leftBottom);
		leftBotomView.setFitWidth(62.5);
		leftBotomView.setFitHeight(62.5);
		leftBotomView.setLayoutX(0);
		leftBotomView.setLayoutY(125);
		
		Image midBottom = new Image(getClass().getResourceAsStream("/Images/bottom.png"));
		ImageView midBottomView = new ImageView(midBottom);
		midBottomView.setFitWidth(62.5);
		midBottomView.setFitHeight(62.5);
		midBottomView.setLayoutX(62.5);
		midBottomView.setLayoutY(125);
		
		Image midBottomRight = new Image(getClass().getResourceAsStream("/Images/bottomright.png"));
		ImageView midBottomRightView = new ImageView(midBottomRight);
		midBottomRightView.setFitWidth(62.5);
		midBottomRightView.setFitHeight(62.5);
		midBottomRightView.setLayoutX(125);
		midBottomRightView.setLayoutY(125);
		
		Image horizontalBottom = new Image(getClass().getResourceAsStream("/Images/verticalendbottom.png"));
		ImageView horizontalBottomView = new ImageView(horizontalBottom);
		horizontalBottomView.setFitWidth(62.5);
		horizontalBottomView.setFitHeight(62.5);
		horizontalBottomView.setLayoutX(187.5);
		horizontalBottomView.setLayoutY(125);
		
		
		Image verticalLeft = new Image(getClass().getResourceAsStream("/Images/horizontalendleft.png"));
		ImageView verticalLeftView = new ImageView(verticalLeft);
		verticalLeftView.setFitWidth(62.5);
		verticalLeftView.setFitHeight(62.5);
		verticalLeftView.setLayoutX(0);
		verticalLeftView.setLayoutY(187.5);
		
		Image verticalMid = new Image(getClass().getResourceAsStream("/Images/horizontalmiddle.png"));
		ImageView verticalMidView = new ImageView(verticalMid);
		verticalMidView.setFitWidth(62.5);
		verticalMidView.setFitHeight(62.5);
		verticalMidView.setLayoutX(62.5);
		verticalMidView.setLayoutY(187.5);
		
		Image verticalRight = new Image(getClass().getResourceAsStream("/Images/horizontalendright.png"));
		ImageView verticalRightView = new ImageView(verticalRight);
		verticalRightView.setFitWidth(62.5);
		verticalRightView.setFitHeight(62.5);
		verticalRightView.setLayoutX(125);
		verticalRightView.setLayoutY(187.5);
		
		Image lot = new Image(getClass().getResourceAsStream("/Images/lot.png"));
		ImageView lotView = new ImageView(lot);
		lotView.setFitWidth(62.5);
		lotView.setFitHeight(62.5);
		lotView.setLayoutX(187.5);
		lotView.setLayoutY(187.5);
		
		Image treeFirst = new Image(getClass().getResourceAsStream("/Images/tree1.png"));
		ImageView treeFirstView = new ImageView(treeFirst);
		treeFirstView.setFitWidth(62.5);
		treeFirstView.setFitHeight(62.5);
		treeFirstView.setLayoutX(0);
		treeFirstView.setLayoutY(250);
		
		Image treeSecond = new Image(getClass().getResourceAsStream("/Images/tree2.png"));
		ImageView treeSecondView = new ImageView(treeSecond);
		treeSecondView.setFitWidth(62.5);
		treeSecondView.setFitHeight(62.5);
		treeSecondView.setLayoutX(62.5);
		treeSecondView.setLayoutY(250);
		
		Image treeThird = new Image(getClass().getResourceAsStream("/Images/tree3.png"));
		ImageView treeThirdView = new ImageView(treeThird);
		treeThirdView.setFitWidth(62.5);
		treeThirdView.setFitHeight(62.5);
		treeThirdView.setLayoutX(125);
		treeThirdView.setLayoutY(250);
		
		Image rockFirst = new Image(getClass().getResourceAsStream("/Images/rock1.png"));
		ImageView rockFirstView = new ImageView(rockFirst);
		rockFirstView.setFitWidth(62.5);
		rockFirstView.setFitHeight(62.5);
		rockFirstView.setLayoutX(187.5);
		rockFirstView.setLayoutY(250);
		
		Image artilleryTower = new Image(getClass().getResourceAsStream("/Images/artillerytower.png"));
		ImageView artilleryTowerView = new ImageView(artilleryTower);
		artilleryTowerView.setFitWidth(62.5);
		artilleryTowerView.setFitHeight(62.5);
		artilleryTowerView.setLayoutX(0);
		artilleryTowerView.setLayoutY(312.5);
		
		Image mageTower = new Image(getClass().getResourceAsStream("/Images/magetower.png"));
		ImageView mageTowerView = new ImageView(mageTower);
		mageTowerView.setFitWidth(62.5);
		mageTowerView.setFitHeight(62.5);
		mageTowerView.setLayoutX(62.5);
		mageTowerView.setLayoutY(312.5);
		
		Image house = new Image(getClass().getResourceAsStream("/Images/house.png"));
		ImageView houseView = new ImageView(house);
		houseView.setFitWidth(62.5);
		houseView.setFitHeight(62.5);
		houseView.setLayoutX(125);
		houseView.setLayoutY(312.5);	
		
		Image rockSecond = new Image(getClass().getResourceAsStream("/Images/rock2.png"));
		ImageView rockSecondView = new ImageView(rockSecond);
		rockSecondView.setFitWidth(62.5);
		rockSecondView.setFitHeight(62.5);
		rockSecondView.setLayoutX(187.5);
		rockSecondView.setLayoutY(312.5);
		
		Image hugeTower = new Image(getClass().getResourceAsStream("/Images/hugetower.png"));
		ImageView hugeTowerView = new ImageView(hugeTower);
		hugeTowerView.setFitWidth(125);
		hugeTowerView.setFitHeight(125);
		hugeTowerView.setLayoutX(0);
		hugeTowerView.setLayoutY(375);	
		
		Image archerTower = new Image(getClass().getResourceAsStream("/Images/archertower.png"));
		ImageView archerTowerView = new ImageView(archerTower);
		archerTowerView.setFitWidth(62.5);
		archerTowerView.setFitHeight(62.5);
		archerTowerView.setLayoutX(125);
		archerTowerView.setLayoutY(375);	
		
		Image well = new Image(getClass().getResourceAsStream("/Images/house.png"));
		ImageView wellView = new ImageView(well);
		wellView.setFitWidth(62.5);
		wellView.setFitHeight(62.5);
		wellView.setLayoutX(187.5);
		wellView.setLayoutY(375);	
		
		Image houseSecond = new Image(getClass().getResourceAsStream("/Images/house2.png"));
		ImageView houseSecondView = new ImageView(houseSecond);
		houseSecondView.setFitWidth(62.5);
		houseSecondView.setFitHeight(62.5);
		houseSecondView.setLayoutX(125);
		houseSecondView.setLayoutY(437.5);
		
		Image wood = new Image(getClass().getResourceAsStream("/Images/wood.png"));
		ImageView woodView = new ImageView(wood);
		woodView.setFitWidth(62.5);
		woodView.setFitHeight(62.5);
		woodView.setLayoutX(187.5);
		woodView.setLayoutY(437.5);

		sideBar.getChildren().addAll(topBot,topMid, topRight,horizontalTop,midL,grassView,midRightView,horizontalMidView,leftBotomView,midBottomView,midBottomRightView,horizontalBottomView,verticalLeftView,verticalMidView,verticalRightView,lotView,treeFirstView,treeSecondView, treeThirdView,rockFirstView, artilleryTowerView,mageTowerView,houseView,rockSecondView,hugeTowerView, archerTowerView,wellView,houseSecondView,woodView);
		
        
		for (int i = 0; i < 20; i++) {
	        for (int j = 0; j < 4; j++) {
	            double x = j * 62.5;
	            double y = i * 62.5;

	            Pane sideBarBorderDash = new Pane();
	            sideBarBorderDash.setPrefSize(62.5, 62.5);
	            sideBarBorderDash.setStyle("-fx-border-style: dashed; -fx-border-width: 0.8; -fx-border-color: black;");
	            sideBarBorderDash.setLayoutX(x);
	            sideBarBorderDash.setLayoutY(y);

	            sideBar.getChildren().add(sideBarBorderDash);
	        }
	    }
		
        biggerSideBar.getChildren().addAll(exitBar,optionBar,sideBar);
        
        
		return biggerSideBar;
	}
	
	private void eventController(ImageView image) {
			
	}

}
