package monotheistic.mongoose.core.files;

import monotheistic.mongoose.core.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Configuration {
    private final FileConfiguration config;
    private Path file;

    public Configuration(JavaPlugin plugin, String file) {
        this.file = Paths.get(plugin.getDataFolder() + File.separator + file);
        if (!Files.exists(this.file))
            plugin.saveResource(file, false);

        this.config = FileUtils.loadConfig(this.file.toFile());
    }

    public void save() {
        FileUtils.save(file.toFile(), config);
    }

    public void reload() {
        FileUtils.reload(file.toFile(), config);
    }

    public FileConfiguration configuration() {
        return config;
    }

    public Path file() {
        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(config, that.config) &&
                Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config, file);
    }
}
