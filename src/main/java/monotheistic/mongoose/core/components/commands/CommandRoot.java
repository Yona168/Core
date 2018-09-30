package monotheistic.mongoose.core.components.commands;

import org.bukkit.command.CommandSender;

public abstract class CommandRoot extends CommandPart {
    private final boolean isNeedsPermission;

    public CommandRoot(CommandInfo info, boolean isNeedsPermission) {
        super(info);
        this.isNeedsPermission = isNeedsPermission;
    }

    boolean canBeExecutedBy(String pluginName, CommandSender sender) {
        return (!isNeedsPermission || sender.isOp() || sender.hasPermission(pluginName + ".commands." + this.getName() + ".use"));
    }
}
