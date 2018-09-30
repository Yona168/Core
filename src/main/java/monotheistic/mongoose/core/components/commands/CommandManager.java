package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommandManager extends Component implements CommandExecutor, HasPluginInfo {
    private final Executable defaultExec;
    private final PluginInfo pluginInfo;

    public CommandManager(Executable defaultExec, PluginInfo pluginInfo) {
        this.defaultExec = defaultExec;
        this.pluginInfo = pluginInfo;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1)
            return false;
        return getChildren().stream().filter(it ->
                it instanceof CommandRoot
        ).map(it -> (CommandRoot) it).filter(part ->
                part.getName().equalsIgnoreCase(strings[0])
        ).findFirst().filter(root -> {
            if (root.canBeExecutedBy(this.getPluginName(), commandSender)) {
                commandSender.sendMessage(CommandPart.noPerms(pluginInfo));
                return false;
            }
            return true;
        })
                .map(it -> it.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), pluginInfo, new ArrayList<Object>()))
                .orElseGet(() -> defaultExec.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), pluginInfo, new ArrayList<>()));
    }

    @Override
    public @NotNull PluginInfo getPluginInfo() {
        return pluginInfo;
    }
}
