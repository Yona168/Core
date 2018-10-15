package monotheistic.mongoose.core;

import com.gitlab.avelyn.core.components.ComponentPlugin;
import monotheistic.mongoose.core.components.commands.CommandSelector;
import monotheistic.mongoose.core.components.commands.ExecutableCommand;
import monotheistic.mongoose.core.components.commands.PluginInfo;
import monotheistic.mongoose.core.components.commands.Test;
import monotheistic.mongoose.core.gui.GUIListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CorePlugin extends ComponentPlugin {
    public CorePlugin() {
        addChild(new GUIListener());
        PluginInfo info = new PluginInfo("CorePlugin", "CP", ChatColor.YELLOW, ChatColor.BLACK);
        final CommandSelector selector = new CommandSelector(info, new ExecutableCommand() {
            @Override
            public boolean execute(CommandSender sender, String cmd, String[] args, PluginInfo pluginInfo, List<Object> objs) {
                sender.sendMessage("BEEP");
                return true;
            }
        });
        selector.addChild(new Test());
        addChild(selector);
        onEnable(() ->
                getCommand("tt").setExecutor(selector));

    }

}
