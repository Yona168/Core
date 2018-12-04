package monotheistic.mongoose.core.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public class MyGUI implements InventoryHolder, GUI {
    private Inventory inventory;
    final Map<Integer, Consumer<InventoryClickEvent>> listeners = new HashMap<>();
    final Set<Consumer<InventoryClickEvent>> allTimeListeners = new HashSet<>();
    private MyGUI parent;

  private MyGUI(MyGUI parent, String name, int size) {
    this(name, size);
    this.parent = parent;
  }

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

  @Override
  public GUI createChild(String name, int size) {
    return new MyGUI(this, name, size);
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

  public boolean openParent(final Player player) {
    if (parent == null)
      return false;
        parent.open(player);
    return true;
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

  @Override
  public void onOpen(InventoryOpenEvent event) {

  }

  @Override
  public void onClose(InventoryCloseEvent event) {

    }
}
