package monotheistic.mongoose.core.components.commands;


import com.gitlab.avelyn.architecture.base.Component;
import com.gitlab.avelyn.architecture.base.Toggleable;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractCommandManager extends Component implements CommandExecutor {
    private final Set<SubCommand> commands;
    private JavaPlugin main;

    public AbstractCommandManager(final JavaPlugin javaPlugin, final Collection<SubCommand> commands) {
        this(javaPlugin);
        this.commands.addAll(commands);
        onDisable(() -> commands.forEach(Toggleable::disable));
        onEnable(() -> commands.forEach(Toggleable::enable));
    }

    public AbstractCommandManager(final JavaPlugin javaPlugin) {
        this.main=javaPlugin;
        this.commands = Collections.newSetFromMap(new IdentityHashMap<>());
    }

    public AbstractCommandManager add(final SubCommand command) {
        this.commands.add(command);
        return this;
    }
    public AbstractCommandManager add(final Function<AbstractCommandManager, SubCommand> supplier){
        add(supplier.apply(this));
        return this;
    }


    public void remove(final String nameOfCommand) {
        commands.stream().filter(command -> command.name().equalsIgnoreCase(nameOfCommand)).forEach(commands::remove);
    }

    public Set<SubCommand> getCommands() {
        return commands;
    }

    public JavaPlugin plugin() {
        return main;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCommandManager that = (AbstractCommandManager) o;
        return Objects.equals(commands, that.commands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commands);
    }
}
