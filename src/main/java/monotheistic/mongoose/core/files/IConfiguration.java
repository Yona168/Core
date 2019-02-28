package monotheistic.mongoose.core.files;

import java.util.Optional;
import java.util.function.Function;

public interface IConfiguration {
  <T> Optional<T> get(String key, Class<T> clazz);

  <T> Optional<T> getAndDeserialize(String key, Function<Object, T> func);

  IConfiguration reload();

  IConfiguration save();

}
