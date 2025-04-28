package domain.services;

import java.util.ArrayList;
import java.util.List;

import domain.controller.MainMenuController;
import domain.entities.Enemy;
import domain.entities.EnemyFactory;
import domain.kutowerdefense.PlayModeManager;
import domain.map.Map;
import domain.map.MapEditor;
import domain.map.PathType;
import domain.map.TileType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

class MovementTimer extends AnimationTimer {
	private long lastUpdate = 0;
	private int s = 0;
	private double totalTimeElapsed = 0;
	
	@Override
	public void handle(long now) {
		if (lastUpdate == 0) {
			lastUpdate = now;
			return;
		}
		
		long deltaTime = (now - lastUpdate);
		lastUpdate = now;
		
		List<Enemy> enemyList = new ArrayList(Enemy.getAllEnemies());
		for (Enemy enemy : enemyList) {
			if(enemy.getPathIndex() == Enemy.path.size()-2) {
				//System.out.println();
			}
			enemy.moveEnemy(deltaTime);
            System.out.printf("%f x, %f y \n",enemy.getLocation().xCoord, enemy.getLocation().yCoord);
            
            s++;
            if (s == 60) {
                System.out.println("1 sec");
                s = 0;
                totalTimeElapsed ++;
            }
            
            if(enemy.getPathIndex() == Enemy.path.size()-1) {
            	System.out.printf("Enemy has reaced end in: %f seconds\n",totalTimeElapsed);
            	System.out.println("reached End");
            }
		}
		
		if (Enemy.getAllEnemies().isEmpty()) {
			stop();
			return;
		}
	}
}

public class MovementTest extends Application {
	PlayModeManager man = PlayModeManager.getInstance();
	MovementTimer timer;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage arg0) throws Exception {
		StackPane root = new StackPane();
        Scene scene = new Scene(root, 400, 400);
        
        arg0.setScene(scene);
        arg0.setTitle("Entity Test");
        arg0.show();
        
        Map map1 = new Map("map1", 5, 5);
		MapEditor me3 = new MapEditor(map1);
		PlayModeManager man = PlayModeManager.getInstance();
		
		
		me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE,4,3);
		me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 0,1);
		me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 3, 3);
		me3.placeTile(TileType.PATH, PathType.BOTTOMRIGHT, 3, 4);
		me3.placeTile(TileType.PATH, PathType.VERTICAL_END_TOP, 2, 4);
		me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 3, 2);
		me3.placeTile(TileType.PATH, PathType.TOPRIGHT, 2, 2);
		me3.placeTile(TileType.PATH, PathType.BOTTOMLEFT, 2, 1);
		me3.placeTile(TileType.PATH, PathType.VERTICAL_MIDDLE, 1, 1);
		
		Utilities.writeMap(map1);
        
        MainMenuController.startNewGame("map1");
        
        Enemy goblin = EnemyFactory.createGoblin();
        goblin.initialize(man.getCurrentMap().getStartingTile().getLocation());
        timer = new MovementTimer();
        timer.start();
	}

}
