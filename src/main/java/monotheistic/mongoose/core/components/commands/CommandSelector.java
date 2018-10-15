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
import java.util.stream.Stream;

public final class CommandSelector extends Component implements CommandExecutor, HasPluginInfo {
    private final ExecutableCommand defaultExec;
    private final PluginInfo pluginInfo;

    public CommandSelector(PluginInfo pluginInfo, ExecutableCommand defaultExec) {
        this.defaultExec = defaultExec;
        this.pluginInfo = pluginInfo;
        onEnable(() ->
                getCommandPartChildren().forEach(it -> {
                    it.setPermissionNodes(getPluginNameForPermission() + "." + it.getPermissionNodes());
                    it.setFullUsage("/" + pluginInfo.getTag().toLowerCase() + " " + it.getFullUsage());
                    walkAndSetPermissionsAndUsage(it);
                })
        );
    }

    private void walkAndSetPermissionsAndUsage(CommandPart root) {
        getCommandPartChildrenOf(root).forEach(child -> {
            child.setPermissionNodes(root.getPermissionNodes() + "." + child.getPermissionNodes());
            child.setFullUsage(root.getFullUsage() + " " + child.getFullUsage());
            walkAndSetPermissionsAndUsage(child);
        });
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage(getDisplayName() + ChatColor.RED + " " + s + " is not a valid command");
            return false;
        }
        Optional<CommandPart> toExecute = getCommandPartChildren().filter(part ->
                part.getPartName().equalsIgnoreCase(strings[0])
        ).findFirst();
        if (toExecute.isPresent()) {
            final CommandPart root = toExecute.get();
            if (!root.canBeExecutedBy(commandSender)) {
                commandSender.sendMessage(CommandPart.noPerms(pluginInfo));
                return false;
            }
            return root.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), pluginInfo, new ArrayList<Object>());
        }
        return defaultExec.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), pluginInfo, new ArrayList<Object>());

    }


    private Stream<CommandPart> getCommandPartChildren() {
        return this.getChildren().stream().filter(it -> it instanceof CommandPart).map(it -> (CommandPart) it);
    }

    private static Stream<CommandPart> getCommandPartChildrenOf(CommandPart part) {
        return part.getChildren().stream().filter(it -> it instanceof CommandPart).map(it -> (CommandPart) it);
    }

    @Override
    public @NotNull PluginInfo getPluginInfo() {
        return pluginInfo;
    }
}
