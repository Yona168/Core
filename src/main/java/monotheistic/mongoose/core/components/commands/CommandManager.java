package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommandManager extends Component implements CommandExecutor {
    private final Executable<CommandSender, String, String[]> defaultExec;
    private final String pluginName;

    public CommandManager(Executable<CommandSender, String, String[]> defaultExec, String pluginName) {
        this.defaultExec = defaultExec;
        this.pluginName = pluginName;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1)
            return false;
        return getChildren().stream().filter(it -> {
            if (!(it instanceof CommandRoot))
                return false;
            final CommandRoot part = (CommandRoot) it;
            return part.getName().equalsIgnoreCase(strings[0]) && part.canBeExecutedBy(pluginName, commandSender);
        }).findFirst()
                .map(it -> ((CommandRoot) it).execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), new ArrayList<Object>()))
                .orElseGet(() -> defaultExec.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), new ArrayList<>()));
    }
}
