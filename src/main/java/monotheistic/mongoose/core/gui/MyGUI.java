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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MyGUI extends AbstractGUI implements InventoryHolder {
  private final Inventory inventory;
  private MyGUI parent;
  private final Set<Consumer<InventoryOpenEvent>> onOpens = new HashSet<>();
  private final Set<Consumer<InventoryCloseEvent>> onCloses = new HashSet<>();

  private MyGUI(MyGUI parent, String name, int size) {
    this(name, size);
    this.parent = parent;
  }

  public MyGUI(String name, int size) {
    inventory = Bukkit.createInventory(this, size, name);
  }

  @Override
  public MyGUI set(int slot, ItemStack item, Consumer<InventoryClickEvent> eventConsumer) {
    inventory.setItem(slot, item);
    super.addListener(slot, eventConsumer);
    return this;
  }

  @Override
  public MyGUI set(ItemStack item, int slot) {
    inventory.setItem(slot, item);
    return this;
  }

  @Override
  public MyGUI addItems(ItemStack... items) {
    inventory.addItem(items);
    return this;
  }

  public MyGUI addItems(Collection<ItemStack> items) {
    items.forEach(inventory::addItem);
    return this;
  }


  @Override
  public GUI createChild(String name, int size) {
    return new MyGUI(this, name, size);
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
    onOpens.forEach(listener -> listener.accept(event));
  }

  @Override
  public void onClose(InventoryCloseEvent event) {
    onCloses.forEach(listener -> listener.accept(event));
  }

  public void onOpen(Consumer<InventoryOpenEvent> listener) {
    onOpens.add(listener);
  }

  public void onClose(Consumer<InventoryCloseEvent> listener) {
    onCloses.add(listener);
  }
}
