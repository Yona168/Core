package monotheistic.mongoose.core.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MyGUI {
    private Inventory inventory;

    public MyGUI(String name, int size, Map<ItemStack, Consumer<InventoryClickEvent>> listeners) {
        inventory = Bukkit.createInventory(new InventoryHolderImpl(listeners), size, name);
        if (listeners != null)
            inventory.setContents(listeners.keySet().toArray(new ItemStack[size]));

    }

    public MyGUI(String name, int size) {
        this(name, size, null);
    }

    public MyGUI set(int slot, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        inventory.setItem(slot, item);
        getListeners().put(item, eventConsumer);
        return this;
    }

    private Map<ItemStack, Consumer<InventoryClickEvent>> getListeners() {
        return ((InventoryHolderImpl) this.inventory.getHolder()).listeners;
    }

    public void open(final Player player) {
        player.openInventory(getView(player));
    }

    private InventoryView getView(final Player player) {
        return new InventoryView() {

            @Override
            public Inventory getTopInventory() {
                return inventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return player.getInventory();
            }

            @Override
            public HumanEntity getPlayer() {
                return player;
            }

            @Override
            public InventoryType getType() {
                return InventoryType.CHEST;
            }
        };
    }


    protected class InventoryHolderImpl implements InventoryHolder {
        final Map<ItemStack, Consumer<InventoryClickEvent>> listeners;

        InventoryHolderImpl(Map<ItemStack, Consumer<InventoryClickEvent>> listeners) {
            this.listeners = listeners;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

    }

    //Decoration Things
    public enum PatternType {
        BORDER((item, inv) -> {
            //TOP
            for (int i = 0; i < 9; i++) {
                inv.setItem(i, item);
            }
            //BOTTOM
            for (int i = inv.getSize()-9; i<inv.getSize(); i++) {
                inv.setItem(i, item);
            }
            //LEFT
            for (int i = 0; i < inv.getSize(); i += 9) {
                inv.setItem(i, item);
            }
            //RIGHT
            for (int i = 8; i<inv.getSize(); i+=9) {
                inv.setItem(i, item);
            }
        });


        private final BiConsumer<ItemStack, Inventory> putAction;

        PatternType(BiConsumer<ItemStack, Inventory> action) {
            this.putAction = action;
        }

    }

    public MyGUI withPattern(ItemStack item, PatternType patternType) {
        patternType.putAction.accept(item, this.inventory);
        return this;
    }
}
