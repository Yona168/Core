package monotheistic.mongoose.core.components.playerdata.database;

import com.gitlab.avelyn.architecture.base.Component;

import java.util.Map;
import java.util.function.Function;

public abstract class DatabaseComponent<K, V> extends Component implements StorerCache<K, V> {
  private Function<K, V> dataCreator;
  private Map<K, V> cache;

  public DatabaseComponent(Function<K, V> dataCreator, Map<K, V> cache) {
    this.dataCreator = dataCreator;
    this.cache = cache;
    onDisable(this::backup);
  }

  @Override
  public Map<K, V> cache() {
    return cache;
  }

  @Override
  public Function<K, V> dataSupplier() {
    return dataCreator;
  }


}