package domain.entities;

import domain.map.Location;

public class GoldBagFactory {

    public static GoldBag createGoldBag(Location location) {
        return new GoldBag(location);
    }

}
