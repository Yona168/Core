package monotheistic.mongoose.core.components.commandsredo;

import java.util.Objects;

public class CommandInfo {
    private final String description, usage, name, trigger;
    private final int argsToInitiallyUtilize;

    public CommandInfo(String description, String usage, String name, String trigger, int argsToInitiallyUtilize) {
        this.description = description;
        this.usage = usage;
        this.name = name;
        this.trigger = trigger;
        this.argsToInitiallyUtilize = argsToInitiallyUtilize;
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

    public String getTrigger() {
        return trigger;
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
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getTrigger(), that.getTrigger());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getUsage(), getName(), getTrigger(), getArgsToInitiallyUtilize());
    }
}
