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

    public AbstractCommandManager(final JavaPlugin plugin, SubCommand... commands) {
        this(plugin, Arrays.asList(commands));
    }

    public AbstractCommandManager(final JavaPlugin javaPlugin, final Collection<SubCommand> commands) {
        this(javaPlugin);
        this.commands.addAll(commands);
    }

    public AbstractCommandManager(final JavaPlugin javaPlugin) {
        this.main = javaPlugin;
        this.commands = Collections.newSetFromMap(new IdentityHashMap<>());
        onEnable(() -> commands.forEach(Toggleable::enable));
        onDisable(() -> commands.forEach(Toggleable::disable));
    }

    public AbstractCommandManager enableComponentCommands() {
        commands.forEach(Toggleable::enable);
        return this;
    }

    public AbstractCommandManager disableByName(String name) {
        commands.stream().filter(cmd -> cmd.name().equalsIgnoreCase(name)).findFirst().map(Component::disable);
        return this;
    }

    public AbstractCommandManager enableByName(String name) {
        commands.stream().filter(cmd -> cmd.name().equalsIgnoreCase(name)).findFirst().map(Component::disable);
        return this;
    }

    public AbstractCommandManager disableByID(int id) {
        commands.stream().filter(cmd -> cmd.id == id).findFirst().map(Component::disable);
        return this;
    }

    public AbstractCommandManager enableByID(int id) {
        commands.stream().filter(cmd -> cmd.id == id).findFirst().map(Component::disable);
        return this;
    }

    public AbstractCommandManager add(final SubCommand command) {
        this.commands.add(command);
        return this;
    }

    public AbstractCommandManager add(final Function<AbstractCommandManager, SubCommand> supplier) {
        add(supplier.apply(this));
        return this;
    }


    public void remove(final String nameOfCommand) {
        commands.stream().filter(command -> command.name().equalsIgnoreCase(nameOfCommand)).findFirst().map(commands::remove);
    }

    public void remove(final int id) {
        commands.stream().filter(command -> command.id == id).findFirst().map(commands::remove);
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
