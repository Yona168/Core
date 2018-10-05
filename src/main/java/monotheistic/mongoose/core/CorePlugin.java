package monotheistic.mongoose.core;

import com.gitlab.avelyn.core.components.ComponentPlugin;
import monotheistic.mongoose.core.components.commands.CommandSelector;
import monotheistic.mongoose.core.components.commands.PluginInfo;
import monotheistic.mongoose.core.components.commands.Test;
import monotheistic.mongoose.core.gui.GUIListener;
import org.bukkit.ChatColor;

public class CorePlugin extends ComponentPlugin {
    public CorePlugin() {
        addChild(new GUIListener());
        CommandSelector selector = new CommandSelector((sender, cmd, args, pluginInfo, objs) -> {
            sender.sendMessage(pluginInfo.getDisplayName() + " " + cmd + " Is not valid");
            return false;
        }, new PluginInfo("TestPlugin", "tt", ChatColor.RED, ChatColor.AQUA));
        selector.addChild(new Test());
        onEnable(() -> {
            getCommand("tt").setExecutor(selector);
        });
    }


}


