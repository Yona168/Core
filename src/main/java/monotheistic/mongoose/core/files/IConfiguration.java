package monotheistic.mongoose.core.files;

import java.util.Optional;

public interface IConfiguration {
  <T> Optional<T> get(String key, Class<T> clazz);

  IConfiguration reload();

  IConfiguration save();

}
