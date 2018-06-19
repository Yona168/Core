package monotheistic.mongoose.core.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileUtils {
    static Stream<Path> list(Path path) {
        try {
            return (Files.list(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static FileConfiguration loadConfig(File file) {
        return save(file, YamlConfiguration.loadConfiguration(file));

    }

    static FileConfiguration save(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    static void createDirectory(Path directory) {
        try {
            Files.createDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
