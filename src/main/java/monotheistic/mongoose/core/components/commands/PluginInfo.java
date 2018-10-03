package monotheistic.mongoose.core.components.commands;

import org.bukkit.ChatColor;

public class PluginInfo {
    private final String name, tag;
    private final ChatColor mainColor;
    private final ChatColor secondaryColor;

    public PluginInfo(String name, String tag, ChatColor mainColor, ChatColor secondaryColor) {
        this.name = name;
        this.tag = tag;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public ChatColor getMainColor() {
        return mainColor;
    }

    public ChatColor getSecondaryColor() {
        return secondaryColor;
    }

    String getPluginNameForPermission() {
        return getName().toLowerCase();
    }

}
