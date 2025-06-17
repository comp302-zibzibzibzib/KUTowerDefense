package domain.kutowerdefense;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerValueListener<T extends Number> {
	// Listener object used to publish consumers when there is a change in an attribute of the Player singleton
	// Pattern used: Observer Pattern
	private List<Consumer<T>> listenerList = new ArrayList<Consumer<T>>();
	
	public void addListener(Consumer<T> consumer) {
		listenerList.add(consumer);
	}
	
	public void removeListener(Consumer<T> consumer) {
        listenerList.remove(consumer);
	}
	
	public void invoke(Number value) {
		for (Consumer<T> consumer : listenerList) {
			consumer.accept((T) value);
		}
	}
}
