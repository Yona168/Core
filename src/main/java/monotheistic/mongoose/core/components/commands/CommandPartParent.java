package monotheistic.mongoose.core.components.commands;

import java.util.stream.Stream;

public interface CommandPartParent {
  Stream<CommandPart> getCommandPartChildren();
}
