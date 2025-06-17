package domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnemyHitPointsListener {
    // Enemy hit points listener listens to changes in the heath points of an enemy instance
    // Used to update the ui health bars in real time
    // Used pattern: Observer Pattern
    private List<Consumer<Double>> listenerList = new ArrayList<Consumer<Double>>();

    public void addListener(Consumer<Double> consumer) {
        listenerList.add(consumer);
    }

    public void removeListener(Consumer<Double> consumer) {
        listenerList.remove(consumer);
    }

    public void invoke(double hitPoints) {
        for (Consumer<Double> consumer : listenerList) {
            consumer.accept(hitPoints);
        }
    }
}
