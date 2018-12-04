package monotheistic.mongoose.core.gui;

import monotheistic.mongoose.core.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public interface PaginateIcons {
  PaginateIcons DEFAULT = new PaginateIcons() {
    private final ItemStack BACK = new ItemBuilder(Material.EMERALD_BLOCK).name(ChatColor.GOLD + "Next ->").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();
    private final ItemStack FORWARDS = new ItemBuilder(Material.REDSTONE_BLOCK).name(ChatColor.GOLD + "<- Back").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();

    @Override
    public ItemStack forwards() {
      return FORWARDS.clone();
    }

    @Override
    public ItemStack backwards() {
      return BACK.clone();
    }

    @Override
    public ItemStack pageIdentifier(int page) {
      return new ItemBuilder(Material.PAPER).name(ChatColor.GOLD + "Page: " + (page + 1)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build();
    }

    @Override
    public String titleForPage(int page) {
      return ChatColor.GOLD + "" + ChatColor.BOLD + "Page " + page;
    }
  };

  ItemStack forwards();

  ItemStack backwards();

  ItemStack pageIdentifier(int page);

  String titleForPage(int page);
}
