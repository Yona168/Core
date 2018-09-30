package monotheistic.mongoose.core.components.commands;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public interface HasPluginInfo {
    @NotNull
    PluginInfo getPluginInfo();

    default String getPluginName() {
        return getPluginInfo().getName();
    }

    default String getPluginTag() {
        return getPluginInfo().getTag();
    }

    default ChatColor getPrimaryChatColor() {
        return getPluginInfo().getMainColor();
    }

    default ChatColor getSecondaryColor() {
        return getPluginInfo().getSecondaryColor();
    }
}
