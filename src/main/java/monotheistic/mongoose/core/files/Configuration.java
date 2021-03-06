package monotheistic.mongoose.core.files;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class Configuration extends YamlConfiguration implements IConfiguration {
  private final Path file;

  private Configuration(Path file) {
    this.file = file;
  }

  public static Configuration loadConfiguration(ClassLoader loader, Path folder, String fileName) {
    Path path = Paths.get(folder.toAbsolutePath().toString(), fileName);
    try {
      final Configuration config;
      if (!Files.exists(path)) {
        Path parent = path.getParent();
        if (!Files.exists(parent))
          Files.createDirectories(parent);
        final InputStream inputStream = loader.getResourceAsStream(fileName);
        Files.copy(inputStream, path);
      }
      config = new Configuration(path);
      try {
        config.load(config.file.toFile());
      } catch (InvalidConfigurationException e) {
        e.printStackTrace();
      }
      return config;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> Optional<T> deserialize(Object gotten, Function<Object, T> deserializer) {
    return Optional.ofNullable(deserializer.apply(gotten));
  }

  @Override
  public <T> Optional<T> getAndDeserialize(String key, Function<Object, T> translator) {
    final Object gotten = get(key);
    return deserialize(gotten, translator);
  }

  @Override
  public <T> Optional<T> get(String key, Class<T> clazz) {
    try {
      return ofNullable(clazz.cast(super.get(key)));
    } catch (ClassCastException e) {
      return empty();
    }
  }


  @Override
  public Configuration reload() {
    try {
      super.load(file.toFile());
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
    return this;
  }

  @Override
  public Configuration save() {
    try {
      super.save(file.toFile());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return this;
  }

}
