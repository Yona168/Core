package monotheistic.mongoose.core.components.commands;

import com.gitlab.avelyn.architecture.base.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class NameCommandPartMapper extends Component implements CommandPartParent {
  private Map<String, CommandPart> childrenMap;

  public NameCommandPartMapper() {
    onEnable(() -> {
      childrenMap = getChildren().stream().filter(it -> it instanceof CommandPart).map(it -> (CommandPart) it).collect(Collectors.toMap(
              CommandPart::getPartName, Function.identity()
      ));
    });
  }

  Optional<CommandPart> getByName(final String str) {
    return Optional.ofNullable(childrenMap.get(str));
  }

  @Override
  public Stream<CommandPart> getCommandPartChildren() {
    return childrenMap.values().stream();
  }

}
