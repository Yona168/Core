package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class CommandPart extends Component implements ExecutableCommand, HasCommandPartInfo {
    private final CommandPartInfo info;
    private String fullUsage, permissionNodes;

    public CommandPart(CommandPartInfo info) {
        this.info = info;
        this.fullUsage = info.getUsage();
        this.permissionNodes = info.getName();
    }

    @Override
    public final boolean execute(CommandSender sender, String cmd, String[] args, PluginInfo pluginInfo, List<Object> objs) {

        return initExecute(sender, cmd, args, pluginInfo, objs).orElseGet(() -> {
            if (args.length <= this.info.getArgsToInitiallyUtilize()) {
                if (this.isSendMessageIfNoChildInputted())
                    sender.sendMessage(inputValidSubCommand(pluginInfo));
                return true;
            } else
                return executeChildIfPossibleWith(sender, Arrays.copyOfRange(args, this.info.getArgsToInitiallyUtilize(), args.length), pluginInfo, objs).orElseGet(() -> {
                    if (this.isSendMessageIfNoChildFound())
                        sender.sendMessage(inputValidSubCommand(pluginInfo));
                    return false;
                });
        });
    }

    @NotNull
    protected abstract Optional<Boolean> initExecute(CommandSender sender, String cmd, String[] args, PluginInfo info, List<Object> objs);

    private Optional<Boolean> executeChildIfPossibleWith(CommandSender sender, String[] args, PluginInfo pluginInfo, List<Object> objs) {
        return getChildren().stream().filter(it -> it instanceof CommandPart).map(it -> (CommandPart) it)
                .filter(it -> it.getInfo().getName().equalsIgnoreCase(args[0])).findFirst()
                .filter(it -> {
                    if (!it.canBeExecutedBy(sender)) {
                        sender.sendMessage(noPerms(pluginInfo));
                        return false;
                    }
                    return true;
                })
                .map(cmd ->
                        cmd.execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length), pluginInfo, objs)
                );
    }

    @Override
    public CommandPartInfo getInfo() {
        return this.info;
    }

    protected String incorrectUsageMessage(PluginInfo info) {
        return info.getDisplayName() + ChatColor.RED + " Incorrect usage! Correct fullUsage is: " + fullUsage;
    }

    protected static String noPerms(PluginInfo info) {
        return info.getDisplayName() + ChatColor.RED + " You do not have the correct permission(s) to use this command!";
    }

    private static String inputValidSubCommand(PluginInfo info) {
        return info.getDisplayName() + ChatColor.RED + " Please input a valid subcommand!";
    }

    public String getFullUsage() {
        return this.fullUsage;
    }

    public void setFullUsage(String val) {
        this.fullUsage = val;
    }

    public String getPermissionNodes() {
        return this.permissionNodes;
    }

    public void setPermissionNodes(String val) {
        this.permissionNodes = val;
    }

    boolean canBeExecutedBy(CommandSender sender) {
        return (sender.isOp() || sender.hasPermission(this.permissionNodes));
    }
}
