package monotheistic.mongoose.core.components.commandsredo;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class CommandPart extends Component implements Executable<CommandSender, String, String[]> {
    private final String description, usage;
    final String name, trigger;
    private final int argsToInitiallyUse;


    CommandPart(String name, String description, String usage, int argsToInitiallyUse, String trigger) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.argsToInitiallyUse = argsToInitiallyUse;
        this.trigger = trigger;

    }

    @Override
    public final boolean execute(CommandSender sender, String cmd, String[] args, List<Object> objs) {
        boolean result = initExecute(sender, cmd, args, objs);
        return executeChildIfPossibleWith(sender, args, objs).orElse(result);

    }

    abstract boolean initExecute(CommandSender sender, String cmd, String[] args, List<Object> objs);

    private Optional<Boolean> executeChildIfPossibleWith(CommandSender sender, String[] args, List<Object> objs) {
        if (args.length <= this.argsToInitiallyUse)
            return Optional.empty();
        return getChildren().stream().filter(it -> it instanceof CommandPart).map(it -> (CommandPart) it)
                .filter(it -> it.trigger.equalsIgnoreCase(args[this.argsToInitiallyUse])).findFirst()
                .map(cmd -> {
                    int startIndex = this.argsToInitiallyUse + 1;
                    return cmd.execute(sender, args[this.argsToInitiallyUse], startIndex > args.length - 1 ? null : Arrays.copyOfRange(args, this.argsToInitiallyUse + 1, args.length), objs);


                });
    }

    public CommandPart addChild(String trigger, String description, String usage, int argsToInitiallyUse, Executable<CommandSender, String, String[]> initExecute) {
        return addChild(new CommandPart(this.name, description, usage, argsToInitiallyUse, trigger) {
            @Override
            boolean initExecute(CommandSender sender, String cmd, String[] args, List<Object> objs) {
                return initExecute.execute(sender, cmd, args, objs);
            }
        });
    }

    public CommandPart addChild(String trigger, String description, int argsToInitiallyUse, Executable<CommandSender, String, String[]> exec) {
        return addChild(trigger, description, this.usage, argsToInitiallyUse, exec);
    }

    public CommandPart addChild(String trigger, int argsToInitiallyUse, String usage, Executable<CommandSender, String, String[]> exec) {
        return addChild(trigger, this.description, usage, argsToInitiallyUse, exec);
    }


}
