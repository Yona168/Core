package monotheistic.mongoose.core.files;

import monotheistic.mongoose.core.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.*;

public class Configuration {
    private final FileConfiguration config;
    private final Path file;

    public Configuration(Path folder, String fileName, ClassLoader classLoader) throws IOException {
        Path path = Paths.get(folder.toAbsolutePath().toString(), fileName);
        if (!Files.exists(path)) {
            this.file = createFileWithParents(path).orElseThrow(IOException::new);
            final InputStream nioFromFileAccess = classLoader.getResourceAsStream(fileName);
            final ByteBuffer buf = ByteBuffer.allocate(100);
            ReadableByteChannel nioFromFile = Channels.newChannel(nioFromFileAccess);
            RandomAccessFile nioToFile = new RandomAccessFile(file.toFile(), "rw");
            int read = nioFromFile.read(buf);
            while (read != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    nioToFile.write(buf.get());
                }
                buf.clear();
                read = nioFromFile.read(buf);
            }
            nioFromFileAccess.close();
            nioFromFile.close();
            nioToFile.close();
        } else file = path;
        this.config = FileUtils.loadConfig(this.file.toFile());
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

    public Configuration save() {
        FileUtils.save(file.toFile(), config);
        return this;
    }

    public Configuration reload() {
        FileUtils.reload(file.toFile(), config);
        return this;
    }

    public FileConfiguration configuration() {
        return config;
    }

    public Path file() {
        return file;
    }

    public Optional<Object> get(String key) {
        return ofNullable(config.get(key));
    }

    public int getInt(String key) {
        return config.getInt(key);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public long getLong(String key) {
        return config.getLong(key);
    }

    public Optional<List<String>> getStringList(String key) {
        return ofNullable(config.getStringList(key));
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
