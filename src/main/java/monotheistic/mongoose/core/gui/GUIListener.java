package monotheistic.mongoose.core.gui;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import static com.gitlab.avelyn.core.base.Events.listen;

public class GUIListener extends Component {
  public GUIListener() {
    addChild(
            listen(InventoryClickEvent.class, (InventoryClickEvent event) -> {
              final Inventory clicked = event.getClickedInventory();
              if (clicked == null)
                return;
              final Inventory topInventory = event.getView().getTopInventory();
              if (topInventory == null)
                return;
              final InventoryHolder holder = topInventory.getHolder();
              if (holder instanceof AbstractGUI) {
                event.setCancelled(true);
                if (clicked.getHolder() != null && clicked.getHolder() == holder) {
                  final AbstractGUI gui = (AbstractGUI) holder;
                  gui.getAllTimeListeners().forEach(listener -> listener.accept(event));
                  gui.clickAt(event.getSlot(), event);
                }
              }
            }));

  }
}
