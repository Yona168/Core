package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;
import monotheistic.mongoose.core.strings.PluginStrings;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public abstract class SubCommand extends Component {
    private final String name;
    private final String desription;
    private final String[] aliases;
    private final String requiredPermissions;
    private final String usage;
    private final String invalidSyntax;
    int id;

    public SubCommand(String name, String desription, String usage, String[] aliases, int id) {
        this.desription = desription;
        this.name = name;
        this.aliases = aliases;
        requiredPermissions = PluginStrings.mainCmdLabel(false) + ".commands." + name();
        this.usage = PluginStrings.mainCmdLabel(true) + " " + usage;
        invalidSyntax = PluginStrings.invalidSyntax() + usage();
        this.id = id;
    }

    public SubCommand(String name, String desription, String usage, int id) {
        this(name, desription, usage, null, id);
    }

    protected abstract boolean onCommand(CommandSender sender, String[] args);

    public String name() {
        return name;
    }

    public String description() {
        return desription;
    }

    public Optional<String[]> aliases() {
        return Optional.ofNullable(aliases);
    }

    public String usage() {
        return usage;
    }

    public String requiredPermissions() {
        return requiredPermissions;
    }

    public String invalidSyntax() {
        return invalidSyntax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCommand that = (SubCommand) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(desription, that.desription) &&
                Arrays.equals(aliases, that.aliases) &&
                Objects.equals(requiredPermissions, that.requiredPermissions) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(invalidSyntax, that.invalidSyntax);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, desription, requiredPermissions, usage, invalidSyntax);
        result = 31 * result + Arrays.hashCode(aliases);
        return result;
    }
}
