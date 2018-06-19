package monotheistic.mongoose.core.components.commands;

import monotheistic.mongoose.core.strings.PluginStrings;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public abstract class SubCommand {
    private final String name;
    private final String desription;
    private final String[] aliases;
    private final String requiredPermissions;
    private final String usage;
    private final String invalidSyntax;

    public SubCommand(String name, String desription, String usage, String[] aliases) {
        this.desription=desription;
        this.name=name;
        this.aliases=aliases;
        requiredPermissions = PluginStrings.mainCmdLabel(false) + ".commands." + name();
        this.usage = PluginStrings.mainCmdLabel(true) + " "+usage;
        invalidSyntax = PluginStrings.invalidSyntax() + usage();
    }

    public SubCommand(String name, String desription, String usage) {
        this(name, desription, usage, null);
    }

    protected abstract boolean onCommand(CommandSender sender, String[] args);

    public String name() {
        return name;
    }

    public String description() {
        return desription;
    }

    public Optional<String[]> aliases() {
        return aliases == null ? Optional.empty() : Optional.of(aliases);
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
