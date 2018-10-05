package monotheistic.mongoose.core.components.commands;

public interface HasCommandInfo {
    CommandInfo getInfo();

    default String getName() {
        return getInfo().getName();
    }

    default int getArgsToUseAsArgs() {
        return getInfo().getArgsToInitiallyUtilize();
    }

    default String getUsage() {
        return getInfo().getUsage();
    }

    default boolean isSendMessageIfNoChildFound() {
        return getInfo().isSendUsageMessageIfNoChildFound();
    }

    default boolean isSendMessageIfNoChildInputted() {
        return getInfo().isSendUsageMessageIfNoChildInputted();
    }
    default String getDescription() {
        return getInfo().getDescription();
    }
}
