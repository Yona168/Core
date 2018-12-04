package monotheistic.mongoose.core.gui;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface OpenCloseListener {
  void onOpen(InventoryOpenEvent event);

  void onClose(InventoryCloseEvent event);
}
