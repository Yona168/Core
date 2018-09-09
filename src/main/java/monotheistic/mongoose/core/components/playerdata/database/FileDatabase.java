package monotheistic.mongoose.core.components.playerdata.database;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileDatabase<K, V> extends DatabaseComponent<K, V> {
    private final Path parentFolder;
    private final Kryo kryo;
    private final Function<K, String> pathIdentifierFunc;

    public FileDatabase(Path parentFolder, Function<K, V> supplier, Map<K, V> cache, Function<K, String> getPathIdentifier, Consumer<Kryo> registerKryoConsumer) {
        this(parentFolder, supplier, cache, getPathIdentifier);
        registerKryoConsumer.accept(kryo);
    }

    public FileDatabase(Path parentFolder, Function<K, V> supplier, Map<K, V> cache, Function<K, String> getPathIdentifier) {
        super(supplier, cache);
        this.parentFolder = parentFolder;
        this.pathIdentifierFunc = getPathIdentifier;
        kryo = new Kryo();
    }

    private void freeze(Kryo kryo, Path file, Object value) throws IOException {
        if (!Files.exists(file.getParent()))
            Files.createDirectory(file.getParent());
        try (UnsafeOutput out = new UnsafeOutput(new FileOutputStream(file.toFile()))) {
            kryo.writeClassAndObject(out, value);
        }
    }

    private <Type> Type thaw(Kryo kryo, Path file) {
        return thaw(kryo, file, null);
    }

    @SuppressWarnings("unchecked")
    private <Type> Type thaw(Kryo kryo, Path file, Type defaultValue) {
        if (Files.exists(file))
            try (UnsafeInput in = new UnsafeInput(new FileInputStream(file.toFile()))) {
                final Type value = (Type) kryo.readClassAndObject(in);
                return value != null ? value : defaultValue;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        return defaultValue;
    }


    @Override
    public Optional<V> fromDatabase(K key) {
        return Optional.ofNullable(thaw(kryo, Paths.get(parentFolder.toString(), pathIdentifierFunc.apply(key))));
    }

    @Override
    public void updateDatabaseFor(K key, V newEntry) {
        try {
            freeze(kryo, Paths.get(parentFolder.toString(), pathIdentifierFunc.apply(key)), newEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
