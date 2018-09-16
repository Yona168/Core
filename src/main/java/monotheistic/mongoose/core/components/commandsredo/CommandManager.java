package monotheistic.mongoose.core.components.commandsredo;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends Component implements CommandExecutor {
    private final Executable<CommandSender, String, String[]> defaultExec;

    public CommandManager(Executable<CommandSender, String, String[]> defaultExec) {
        this.defaultExec = defaultExec;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1)
            return false;
        return getChildren().stream().filter(it -> {
            if (!(it instanceof CommandPart))
                return false;
            return ((CommandPart) it).trigger.equalsIgnoreCase(strings[0]);
        }).findFirst().map(it -> ((CommandPart) it).execute(commandSender, strings[0], strings, new ArrayList())).orElse(defaultExec.execute(commandSender, strings[0], strings, new ArrayList<>()));
    }

    public CommandPart newCommandRoot(String name, String description, String usage, int argsToUse, Executable<CommandSender, String, String[]> executor) {

        return new CommandPart(name, description, usage, argsToUse, name) {
            @Override
            boolean initExecute(CommandSender sender, String cmd, String[] args, List<Object> objs) {

                return executor.execute(sender, cmd, args, objs);
            }
        };
    }
}
