package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Toggleable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface HasCommandPartChildren {
    List<Toggleable> getChildren();

    default List<CommandPart> getCommandPartChildren() {
        List<CommandPart> commandPartChildren = new ArrayList<>();
        for (Toggleable child : getChildren()) {
            if (child instanceof CommandPart)
                commandPartChildren.add((CommandPart) child);
        }
        return commandPartChildren;
    }

    default Stream<CommandPart> getCommandPartChildrenAsStream() {
        return getCommandPartChildren().stream();
    }
}
