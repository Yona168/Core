package monotheistic.mongoose.core.files;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.util.Optional.*;

public class Configuration extends YamlConfiguration implements IConfiguration {
  private Path file;

  private Configuration() {
  }

  public static Configuration loadConfiguration(Path folder, String fileName) throws IOException {
        Path path = Paths.get(folder.toAbsolutePath().toString(), fileName);
    final Configuration config = new Configuration();
        if (!Files.exists(path)) {
          config.file = createFileWithParents(path).orElseThrow(IOException::new);
          try {
            config.load(config.file.toFile());
          } catch (InvalidConfigurationException e) {
            e.printStackTrace();
          }
        } else config.file = path;
    return config;
    }

    private static Optional<Path> createFileWithParents(Path file) {
        if (Files.exists(file))
            return of(file);
        Path parent = file.getParent();
        try {
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(file);
            return of(file);
        } catch (IOException e) {
            e.printStackTrace();
            return empty();
        }
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
