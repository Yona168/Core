package monotheistic.mongoose.core.gui;

import monotheistic.mongoose.core.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PaginatorGUI {
    private List<MyGUI> pages;
    static ItemStack REDSTONE_BACK = new ItemBuilder(Material.REDSTONE_BLOCK).name(ChatColor.GOLD + "<- Back").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();
    static ItemStack EMERALD_FORWARDS = new ItemBuilder(Material.EMERALD_BLOCK).name(ChatColor.GOLD + "Next ->").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();

    private int currentPage;

    public PaginatorGUI(ItemStack[] contents, String title, int size, Consumer<InventoryClickEvent> listener) {
        pages = new ArrayList<>();
        if (size == 9)
            size = 18;
        final int amtOfPages = contents.length / (size - 9);
        final int margin = contents.length % (size - 9);

        for (int i = 1; i <= amtOfPages; i++) {
            final MyGUI gui;
            pages.add(createPage(title, i, size, listener, contents, (i - 1) * (size - 9), (i * (size - 9))));
            //pages.add(gui = new MyGUI(titleForPage(title, i), size));
           /* if (listener != null)
                gui.addAllTimeListener(listener);
            gui.set(size - 9, REDSTONE_BACK, (InventoryClickEvent event) ->
                    back((Player) event.getWhoClicked()))
                    .set(size - 1, EMERALD_FORWARDS, (InventoryClickEvent event) -> next((Player) event.getWhoClicked())).set(pageIdentifier(i), size - 5)
                    .addItems(Arrays.copyOfRange(contents, (i - 1) * (size - 9), (i * (size - 9))));*/
        }
        if (margin > 0) {
            /*final MyGUI gui;
            pages.add(gui = new MyGUI(titleForPage(title, pages.size() + 1), size));
            if (listener != null)
                gui.addAllTimeListener(listener);
            gui.set(size - 9, REDSTONE_BACK, (InventoryClickEvent event) ->
                    back((Player) event.getWhoClicked()))
                    .set(size - 1, EMERALD_FORWARDS, (InventoryClickEvent event) -> next((Player) event.getWhoClicked())).set(pageIdentifier(pages.size() + 1), size - 5)
                    .addItems(Arrays.copyOfRange(contents, contents.length - margin, contents.length));*/

            pages.add(createPage(title, pages.size(), size, listener, contents, contents.length - margin, contents.length));
        }

    }

    private MyGUI createPage(String title, int page, int size, Consumer<InventoryClickEvent> listener, ItemStack[] contents, int copyStart, int copyEnd) {
        final MyGUI gui = new MyGUI(titleForPage(title, page), size);
        if (listener != null)
            gui.addAllTimeListener(listener);
        gui.set(size - 9, REDSTONE_BACK, (InventoryClickEvent event) ->
                back((Player) event.getWhoClicked()))
                .set(size - 1, EMERALD_FORWARDS, (InventoryClickEvent event) -> next((Player) event.getWhoClicked())).set(pageIdentifier(page), size - 5)
                .addItems(Arrays.copyOfRange(contents, copyStart, copyEnd));
        return gui;
    }


    private void next(Player player) {
        currentPage++;
        if (currentPage >= pages.size()) {
            currentPage--;
        } else {
            pages.get(currentPage).open(player);
        }
    }

    private void back(Player player) {
        currentPage--;
        if (currentPage < 0) {
            currentPage++;
        } else {
            pages.get(currentPage).open(player);
        }
    }

    private static ItemStack pageIdentifier(int page) {
        return new ItemBuilder(Material.PAPER).name(ChatColor.GOLD + "Page: " + page).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();
    }

    private static String titleForPage(String title, int page) {
        return title + " " + page;
    }

    public void open(Player player) {
        pages.get(0).open(player);
    }
}
