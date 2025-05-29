package domain.entities;

import domain.map.Location;
import domain.services.Utilities;

public class GoldBagFactory {
	
	private static GoldBagFactory instance;
    private GoldBagFactory() {}

    public static GoldBagFactory getInstance() {
        if (instance == null) {
            instance = new GoldBagFactory();
        }
        return instance;
    }
    
    public void trySpawnGoldBag(Location location) {
    	if(Utilities.globalRNG.nextDouble() <= 0.15) {
    		new GoldBag(location);
    	}
    }
}
