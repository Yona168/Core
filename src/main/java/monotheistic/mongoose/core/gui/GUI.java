package monotheistic.mongoose.core.gui;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public interface GUI extends OpenCloseListener {

  void open(Player player);

  boolean openParent(Player player);

  GUI addAllTimeListener(Consumer<InventoryClickEvent> event);

  GUI set(int slot, ItemStack item, Consumer<InventoryClickEvent> onClick);

  GUI set(ItemStack item, int slot);

  GUI addItems(ItemStack... items);

  GUI createChild(String name, int size);

  static Optional<GUI> paginatorGui(ItemStack[] contents, int size, Consumer<InventoryClickEvent> listener, BiFunction<String, Integer, GUI> guiCreator, PaginateIcons paginateInfo) {
    if (size <= 9)
      size = 18;
    ItemStack[][] contentsPartitioned = Lists.partition(Arrays.asList(contents), size - 9).stream().map(it -> it.toArray(new ItemStack[0])).toArray(ItemStack[][]::new);
    if (contentsPartitioned.length != 0) {
      final GUI firstPage = guiCreator.apply(paginateInfo.titleForPage(1), size);
      firstPage.addItems(contentsPartitioned[0]);
      GUI parent = firstPage;
      for (int i = 2; i <= contentsPartitioned.length; i++) {
        final GUI child = parent.createChild(paginateInfo.titleForPage(i), size);
        child.addAllTimeListener(listener);
        child.addItems(contents[i - 1]);
        child.set(size - 9, paginateInfo.backwards(), (InventoryClickEvent event) -> child.openParent((Player) event.getWhoClicked()));
        child.set(paginateInfo.pageIdentifier(i), size - 5);
        parent.set(size - 5, paginateInfo.forwards(), (InventoryClickEvent event) -> child.open((Player) event.getWhoClicked()));
        parent = child;
      }
      return of(firstPage);
    }
    return empty();
  }
}
