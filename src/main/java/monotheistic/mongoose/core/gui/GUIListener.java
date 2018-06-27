package monotheistic.mongoose.core.gui;

import com.gitlab.avelyn.architecture.base.Component;
import monotheistic.mongoose.core.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
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
                event.setCancelled(true);
                if (clicked.getHolder() != null && clicked.getHolder() == holder) {
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
        onEnable(() -> {
            PaginatorGUI.REDSTONE_BACK = new ItemBuilder(Material.REDSTONE_BLOCK).name(ChatColor.GOLD + "<- Back").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();
            PaginatorGUI.EMERALD_FORWARDS = new ItemBuilder(Material.EMERALD_BLOCK).name(ChatColor.GOLD + "Next ->").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();
        });
    }
}
