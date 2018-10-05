package monotheistic.mongoose.core.components.commands;

import java.util.Objects;

public class CommandInfo {
    private final String description, usage, name;
    private final int argsToInitiallyUtilize;
    private final boolean sendUsageMessageIfNoChildFound, sendUsageMessageIfNoChildInputted;

    public CommandInfo(String description, String usage, String name, int argsToInitiallyUtilize, boolean sendUsageMessageIfNoChildFound, boolean sendUsageMessageIfNoChildInputted) {
        this.description = description;
        this.usage = usage;
        this.name = name;
        this.argsToInitiallyUtilize = argsToInitiallyUtilize;
        this.sendUsageMessageIfNoChildFound = sendUsageMessageIfNoChildFound;
        this.sendUsageMessageIfNoChildInputted = sendUsageMessageIfNoChildInputted;
    }


    String getDescription() {
        return description;
    }

    String getUsage() {
        return usage;
    }

    String getName() {
        return name;
    }

    boolean isSendUsageMessageIfNoChildFound() {
        return sendUsageMessageIfNoChildFound;
    }

    int getArgsToInitiallyUtilize() {
        return argsToInitiallyUtilize;
    }

    boolean isSendUsageMessageIfNoChildInputted() {
        return sendUsageMessageIfNoChildInputted;
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
