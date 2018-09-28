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

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MyGUI implements InventoryHolder {
    private Inventory inventory;
    final Map<Integer, Consumer<InventoryClickEvent>> listeners = new HashMap<>();
    final Set<Consumer<InventoryClickEvent>> allTimeListeners = new HashSet<>();
    private MyGUI parent;

    public MyGUI(String name, int size) {
        inventory = Bukkit.createInventory(this, size, name);
    }

    public MyGUI set(int slot, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
        inventory.setItem(slot, item);
        getListeners().put(slot, eventConsumer);
        return this;
    }

    public MyGUI set(ItemStack item, int slot) {
        inventory.setItem(slot, item);
        return this;
    }

    public MyGUI addItems(ItemStack... items) {
        inventory.addItem(items);
        return this;
    }

    public MyGUI addItems(Collection<ItemStack> items) {
        items.forEach(inventory::addItem);
        return this;
    }

    public MyGUI addAllTimeListener(Consumer<InventoryClickEvent> inventoryClickEventConsumer) {
        getAllTimeListeners().add(inventoryClickEventConsumer);
        return this;
    }

    public boolean hasListenerSetFor(int slot) {
        return getListeners().containsKey(slot);
    }

    private Map<Integer, Consumer<InventoryClickEvent>> getListeners() {
        return listeners;
    }

    private Set<Consumer<InventoryClickEvent>> getAllTimeListeners() {
        return allTimeListeners;
    }

    public void open(final Player player) {
        player.openInventory(getView(player));
    }

    public void openParent(final Player player) {
        parent.open(player);
    }

    public void child(MyGUI child) {
        child.parent = this;
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

    @Override
    public Inventory getInventory() {
        return inventory;
    }


    //Decoration Things
    public enum Preset {
        BORDER((item, inv) -> {
            //TOP
            for (int i = 0; i < 9; i++) {
                inv.setItem(i, item);
            }
            //BOTTOM
            for (int i = inv.getSize() - 9; i < inv.getSize(); i++) {
                inv.setItem(i, item);
            }
            //LEFT
            for (int i = 0; i < inv.getSize(); i += 9) {
                inv.setItem(i, item);
            }
            //RIGHT
            for (int i = 8; i < inv.getSize(); i += 9) {
                inv.setItem(i, item);
            }
        });


        private final BiConsumer<ItemStack, Inventory> putAction;

        Preset(BiConsumer<ItemStack, Inventory> action) {
            this.putAction = action;
        }

    }

    public MyGUI withPattern(ItemStack item, Preset preset) {
        preset.putAction.accept(item, this.inventory);
        return this;
    }
}
