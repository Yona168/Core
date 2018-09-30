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

    String getName() {
        return name;
    }

    String getTag() {
        return tag;
    }

    ChatColor getMainColor() {
        return mainColor;
    }

    ChatColor getSecondaryColor() {
        return secondaryColor;
    }
}
