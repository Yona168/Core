package monotheistic.mongoose.core.components.commandsredo;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class CommandPart extends Component implements Executable<CommandSender, String, String[]> {
    private final CommandInfo info;


    public CommandPart(CommandInfo info) {
        this.info = info;
    }

    @Override
    public final boolean execute(CommandSender sender, String cmd, String[] args, List<Object> objs) {
        return initExecute(sender, cmd, args, objs).orElseGet(() -> {
            if (args.length <= this.info.getArgsToInitiallyUtilize())
                return true;
            else
                return executeChildIfPossibleWith(sender, Arrays.copyOfRange(args, this.info.getArgsToInitiallyUtilize() + 1, args.length), objs).orElse(false);
        });
    }

    protected abstract Optional<Boolean> initExecute(CommandSender sender, String cmd, String[] args, List<Object> objs);

    private Optional<Boolean> executeChildIfPossibleWith(CommandSender sender, String[] args, List<Object> objs) {
        return getChildren().stream().filter(it -> it instanceof CommandPart).map(it -> (CommandPart) it)
                .filter(it -> it.getInfo().getTrigger().equalsIgnoreCase(args[0])).findFirst()
                .map(cmd -> {
                    if (args.length - 1 < cmd.getInfo().getArgsToInitiallyUtilize())
                        return false;
                    return cmd.execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length), objs);
                });
    }

    CommandInfo getInfo() {
        return info;
    }


}
