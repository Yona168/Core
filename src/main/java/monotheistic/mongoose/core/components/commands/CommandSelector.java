package monotheistic.mongoose.core.components.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public final class CommandSelector extends NameCommandPartMapper implements CommandExecutor {
    private final ExecutableCommand defaultExec;
    private final PluginInfo pluginInfo;

    public CommandSelector(@NotNull PluginInfo pluginInfo, @NotNull ExecutableCommand defaultExec) {
        this.defaultExec = defaultExec;
        this.pluginInfo = pluginInfo;
        onEnable(() ->
                getCommandPartChildren().forEach(it -> {
                    it.setPermissionNodes(pluginInfo.getPluginNameForPermission() + ".commands." + it.getPermissionNodes());
                    it.setFullUsage("/" + pluginInfo.getTag().toLowerCase() + " " + it.getFullUsage());
                    walkAndSetPermissionsAndUsage(it);
                })
        );
    }

    private void walkAndSetPermissionsAndUsage(CommandPart root) {
        root.getCommandPartChildren().forEach(child -> {
            child.setPermissionNodes(root.getPermissionNodes() + "." + child.getPermissionNodes());
            child.setFullUsage(root.getFullUsage() + " " + child.getFullUsage());
            walkAndSetPermissionsAndUsage(child);
        });
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            return defaultExec.execute(commandSender, s, strings, pluginInfo, new ArrayList<Object>());
        }
        Optional<CommandPart> toExecute = getByName(strings[0]);
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
}
