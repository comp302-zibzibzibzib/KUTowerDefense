package domain.kutowerdefense;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerValueListener<T extends Number> {
	private List<Consumer<T>> listenerList = new ArrayList<Consumer<T>>();
	
	public void addListener(Consumer<T> consumer) {
		listenerList.add(consumer);
	}
	
	public void removeListener(Consumer<T> consumer) {
		if (listenerList.contains(consumer)) listenerList.remove(consumer);
	}
	
	public void invoke() {
		for (Consumer<T> consumer : listenerList) {
			consumer.accept(null);
		}
	}
}
