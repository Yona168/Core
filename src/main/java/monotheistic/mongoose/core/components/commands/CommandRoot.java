package monotheistic.mongoose.core.components.commands;

import java.util.stream.Stream;

public abstract class CommandRoot extends CommandPart {

    public CommandRoot(CommandInfo info, PluginInfo pluginInfo) {
        super(info);
        permissionNode = pluginInfo.getName() + "." + info.getName();
        onEnable(() ->
                getCommandPartChildrenOf(this).forEach(this::setUsagesAndPermissionsForChildren)
        );
    }

    private void setUsagesAndPermissionsForChildren(CommandPart part) {
        final Stream<CommandPart> children = getCommandPartChildrenOf(part);
        children.forEach(child -> {
            child.usage = part.getUsage() + "." + child.usage;
            child.permissionNode = part.getName() + "." + child.getName();
            setUsagesAndPermissionsForChildren(child);
        });
    }

    private static Stream<CommandPart> getCommandPartChildrenOf(CommandPart part) {
        return part.getChildren().stream().filter(it -> it instanceof CommandPart).map(it -> (CommandPart) it);
    }

}
