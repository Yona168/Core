package monotheistic.mongoose.core.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractGUI implements GUI {
  private final Set<Consumer<InventoryClickEvent>> allTimeListeners = new HashSet<>();
  private final Map<Integer, Consumer<InventoryClickEvent>> listeners = new HashMap<>();

  Set<Consumer<InventoryClickEvent>> getAllTimeListeners() {
    return allTimeListeners;
  }

  @Override
  public GUI addAllTimeListener(Consumer<InventoryClickEvent> listener) {
    allTimeListeners.add(listener);
    return this;
  }

  void clickAt(int slot, InventoryClickEvent event) {
    Consumer<InventoryClickEvent> listener = listeners.get(slot);
    if (listener != null) {
      listener.accept(event);
    }
  }

  protected AbstractGUI addListener(int slot, Consumer<InventoryClickEvent> listener) {
    listeners.put(slot, listener);
    return this;
  }
}
