package monotheistic.mongoose.core.components.commands;

public interface HasCommandPartInfo {
    CommandPartInfo getInfo();

    default String getPartName() {
        return getInfo().getName();
    }

    default int getArgsToUseAsArgs() {
        return getInfo().getArgsToInitiallyUtilize();
    }

    default String getPartUsage() {
        return getInfo().getUsage();
    }

    default boolean isSendMessageIfNoChildFound() {
        return getInfo().isSendUsageMessageIfNoChildFound();
    }

    default boolean isSendMessageIfNoChildInputted() {
        return getInfo().isSendUsageMessageIfNoChildInputted();
    }

    default String getPartDescription() {
        return getInfo().getDescription();
    }
}
