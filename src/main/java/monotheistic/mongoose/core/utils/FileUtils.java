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


    static FileConfiguration save(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    static FileConfiguration reload(File file, FileConfiguration c) {
        c = YamlConfiguration.loadConfiguration(file);
        return save(file, c);
    }

    static void createDirectory(Path directory) {
        try {
            Files.createDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
