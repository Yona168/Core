package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiPredicate;

public final class CommandSelector extends Component implements CommandExecutor, HasPluginInfo {
    private final BiPredicate<CommandSender, String> defaultExec;
    private final PluginInfo pluginInfo;

    public CommandSelector(BiPredicate<CommandSender, String> defaultExec, PluginInfo pluginInfo) {
        this.defaultExec = defaultExec;
        this.pluginInfo = pluginInfo;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage(getPluginTag() + ChatColor.RED + s + " is not a valid command");
            return false;
        }
        return getChildren().stream().filter(it ->
                it instanceof CommandRoot
        ).map(it -> (CommandRoot) it).filter(part ->
                part.getName().equalsIgnoreCase(strings[0])
        ).findFirst().filter(root -> {
            if (root.canBeExecutedBy(this.getPluginNameForPermission(), commandSender)) {
                commandSender.sendMessage(CommandPart.noPerms(pluginInfo));
                return false;
            }
            return true;
        })
                .map(it -> it.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), pluginInfo, new ArrayList<Object>()))
                .orElseGet(() -> defaultExec.test(commandSender, strings[0]));
    }

    @Override
    public @NotNull PluginInfo getPluginInfo() {
        return pluginInfo;
    }
}
