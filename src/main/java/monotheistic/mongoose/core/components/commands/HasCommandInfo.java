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

    default boolean isSendUsageIfNoChildFound() {
        return getInfo().isSendUsageMessageIfNoChildFound();
    }
    default String getDescription() {
        return getInfo().getDescription();
    }
}
