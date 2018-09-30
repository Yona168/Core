package monotheistic.mongoose.core.components.commands;

import java.util.Objects;

public class CommandInfo {
    private final String description, usage, name;
    private final int argsToInitiallyUtilize;
    private final boolean needsPermission;

    public CommandInfo(String description, String usage, String name, int argsToInitiallyUtilize, boolean needsPermission) {
        this.description = description;
        this.usage = usage;
        this.name = name;
        this.argsToInitiallyUtilize = argsToInitiallyUtilize;
        this.needsPermission = needsPermission;
    }

    public boolean isNeedsPermission() {
        return needsPermission;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getName() {
        return name;
    }

    public int getArgsToInitiallyUtilize() {
        return argsToInitiallyUtilize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandInfo that = (CommandInfo) o;
        return getArgsToInitiallyUtilize() == that.getArgsToInitiallyUtilize() &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getUsage(), that.getUsage()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getUsage(), getName(), getArgsToInitiallyUtilize());
    }
}
