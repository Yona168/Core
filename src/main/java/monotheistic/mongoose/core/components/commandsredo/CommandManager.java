package monotheistic.mongoose.core.components.commandsredo;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommandManager extends Component implements CommandExecutor {
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
            return ((CommandPart) it).getInfo().getTrigger().equalsIgnoreCase(strings[0]);
        }).findFirst()
                .map(it -> ((CommandPart) it).execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), new ArrayList<Object>()))
                .orElseGet(() -> defaultExec.execute(commandSender, strings[0], Arrays.copyOfRange(strings, 1, strings.length), new ArrayList<>()));
    }
}
