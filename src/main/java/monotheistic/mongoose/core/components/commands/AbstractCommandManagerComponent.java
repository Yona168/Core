package monotheistic.mongoose.core.components.commands;


import com.gitlab.avelyn.architecture.base.Component;
import com.gitlab.avelyn.architecture.base.Toggleable;
import org.bukkit.command.CommandExecutor;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractCommandManagerComponent extends Component implements CommandExecutor {

    public AbstractCommandManagerComponent(SubCommand... commands) {
        addChild(commands);
    }

    public AbstractCommandManagerComponent(final Collection<SubCommand> commands) {
        commands.forEach(this::addChild);
    }

    public Set<SubCommand> getCommands() {
        return getChildren().stream().filter(cmd -> cmd instanceof SubCommand).map(cmd -> (SubCommand) cmd).collect(Collectors.toSet());
    }

    private AbstractCommandManagerComponent doEachByFilter(Predicate<SubCommand> filter, Consumer<SubCommand> action) {
        getChildren().stream().filter(cmd -> cmd instanceof SubCommand).filter(cmd -> filter.test((SubCommand) cmd)).forEach(cmd -> action.accept((SubCommand) cmd));
        return this;
    }

    public AbstractCommandManagerComponent disableByName(String name) {
        doEachByFilter(cmd -> cmd.name().equalsIgnoreCase(name), Toggleable::disable);
        return this;
    }

    public AbstractCommandManagerComponent enableByName(String name) {
        doEachByFilter(cmd -> cmd.name().equalsIgnoreCase(name), Toggleable::enable);
        return this;
    }

    public AbstractCommandManagerComponent disableByID(int id) {
        doEachByFilter(cmd -> cmd.id == id, Toggleable::enable);
        return this;
    }

    public AbstractCommandManagerComponent enableByID(int id) {
        doEachByFilter(cmd -> cmd.id == id, Toggleable::enable);
        return this;
    }

    public AbstractCommandManagerComponent addChild(final Function<AbstractCommandManagerComponent, SubCommand> supplier) {
        addChild(supplier.apply(this));
        return this;
    }


    public void remove(final String nameOfCommand) {
        doEachByFilter(cmd -> cmd.name().equalsIgnoreCase(nameOfCommand), this::removeChild);
    }

    public void remove(final int id) {
        doEachByFilter(cmd -> cmd.id == id, this::removeChild);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCommandManagerComponent that = (AbstractCommandManagerComponent) o;
        return getChildren().equals(that.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChildren());

    }
}
