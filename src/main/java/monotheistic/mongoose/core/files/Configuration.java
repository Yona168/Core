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
import java.util.Objects;

public class Configuration {
    private final FileConfiguration config;
    private Path file;

    public Configuration(Path folder, String fileName, Class<?> classOfCaller) throws IOException {
        this.file = Paths.get(folder.toString(), fileName);
        if (!Files.exists(this.file)) {
            final InputStream nioFromFileAccess = classOfCaller.getClassLoader().getResourceAsStream(fileName);
            final ByteBuffer buf = ByteBuffer.allocate(100);
            ReadableByteChannel nioFromFile = Channels.newChannel(nioFromFileAccess);
            Files.createFile(file);
            RandomAccessFile nioToFile = new RandomAccessFile(file.toFile(), "rw");
            int read = nioFromFile.read(buf);
            while (read != 0) {
                buf.flip();
                nioToFile.write(buf.array());
                buf.clear();
                read = nioFromFile.read(buf);
            }
            nioFromFileAccess.close();
            nioFromFile.close();
            nioToFile.close();
        }


        this.config = FileUtils.loadConfig(this.file.toFile());
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

    public Object get(String key) {
        return config.get(key);
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
