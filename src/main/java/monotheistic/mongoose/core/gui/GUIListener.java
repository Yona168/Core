package monotheistic.mongoose.core.gui;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static com.gitlab.avelyn.core.base.Events.listen;

public class GUIListener extends Component {
    public GUIListener() {
        addChild(listen((InventoryClickEvent event) -> {
            final Inventory clicked = event.getClickedInventory();
            if (clicked == null)
                return;
            final InventoryHolder holder = event.getView().getTopInventory().getHolder();
            if (holder instanceof MyGUI) {
                if (clicked.getHolder() != null && clicked.getHolder() == holder) {
                    event.setCancelled(true);
                    final MyGUI gui = (MyGUI) holder;
                    final ItemStack currentItem = event.getCurrentItem();
                    if (currentItem == null)
                        return;
                    gui.allTimeListeners.forEach(listener -> listener.accept(event));
                    final Consumer<InventoryClickEvent> action = gui.listeners.get(currentItem);
                    if (action != null) {
                        action.accept(event);
                    }
                }
            }
        }));

        onDisable(() -> {
            PaginatorGUI.EMERALD_FORWARDS = null;
            PaginatorGUI.REDSTONE_BACK = null;
        });
    }
}
