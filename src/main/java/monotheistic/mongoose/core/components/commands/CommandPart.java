package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class CommandPart extends Component implements ExecutableCommand, HasCommandInfo {
    private final CommandInfo info;


    public CommandPart(CommandInfo info) {
        this.info = info;
    }

    @Override
    public final boolean execute(CommandSender sender, String cmd, String[] args, PluginInfo pluginInfo, List<Object> objs) {

        return initExecute(sender, cmd, args, pluginInfo, objs).orElseGet(() -> {
            if (args.length <= this.info.getArgsToInitiallyUtilize()) {
                if (this.isSendMessageIfNoChildInputted())
                    sender.sendMessage(inputValidSubCommand(pluginInfo));
                return true;
            }
            else
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
                .map(cmd ->
                        cmd.execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length), pluginInfo, objs)
                );
    }

    @Override
    public CommandInfo getInfo() {
        return this.info;
    }

    protected static String incorrectUsageMessage(PluginInfo info, String usage) {
        return info.getDisplayName() + ChatColor.RED + " Incorrect usage! Correct usage is: " + "/" + info.getTag() + " " + usage;
    }

    protected static String noPerms(PluginInfo info) {
        return info.getDisplayName() + ChatColor.RED + " You do not have the correct permission(s) to use this command!";
    }

    private static String inputValidSubCommand(PluginInfo info) {
        return info.getDisplayName() + ChatColor.RED + " Please input a valid subcommand!";
    }

}
