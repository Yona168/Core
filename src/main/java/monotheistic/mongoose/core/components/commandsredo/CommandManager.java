package monotheistic.mongoose.core.components.commandsredo;

import com.gitlab.avelyn.architecture.base.Component;
import org.bukkit.Bukkit;
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
        Bukkit.broadcastMessage(getChildren().size() + "   ");
        return getChildren().stream().filter(it -> {
            if (!(it instanceof CommandPart))
                return false;
            Bukkit.broadcastMessage(((CommandPart) it).getInfo().getTrigger());
            return ((CommandPart) it).getInfo().getTrigger().equalsIgnoreCase(strings[0]);
        }).peek(it -> Bukkit.broadcastMessage("FOUND MATCH")).findFirst()
                .map(it -> ((CommandPart) it).execute(commandSender, strings[0], strings.length == 1 ? null : Arrays.copyOfRange(strings, 1, strings.length), new ArrayList<Object>()))
                .orElse(defaultExec.execute(commandSender, strings[0], strings.length == 1 ? null : Arrays.copyOfRange(strings, 1, strings.length), new ArrayList<>()));
    }
}
