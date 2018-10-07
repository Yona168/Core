package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public final class CommandSelector extends Component implements CommandExecutor, HasPluginInfo {
    private final ExecutableCommand defaultExec;
    private final PluginInfo pluginInfo;

    public CommandSelector(ExecutableCommand defaultExec, PluginInfo pluginInfo) {
        this.defaultExec = defaultExec;
        this.pluginInfo = pluginInfo;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage(getDisplayName() + ChatColor.RED + " " + s + " is not a valid command");
            return false;
        }
        Optional<CommandRoot> toExecute = getChildren().stream().filter(it ->
                it instanceof CommandRoot
        ).map(it -> (CommandRoot) it).filter(part ->
                part.getName().equalsIgnoreCase(strings[0])
        ).findFirst();
        if (toExecute.isPresent()) {
            final CommandRoot root = toExecute.get();
            if (!root.canBeExecutedBy(this.getPluginNameForPermission(), commandSender)) {
                commandSender.sendMessage(CommandPart.noPerms(pluginInfo));
                return false;
            }
            return root.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), pluginInfo, new ArrayList<Object>());
        }
        return defaultExec.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), pluginInfo, new ArrayList<Object>());

    }


    @Override
    public @NotNull PluginInfo getPluginInfo() {
        return pluginInfo;
    }
}
