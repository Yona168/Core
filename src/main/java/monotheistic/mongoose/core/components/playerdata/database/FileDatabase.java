package monotheistic.mongoose.core.components.playerdata.database;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import monotheistic.mongoose.core.components.playerdata.PlayerData;
import monotheistic.mongoose.core.utils.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class FileDatabase extends Database {
    private Kryo kryo;
    private final Path playerFolder;

    public FileDatabase(JavaPlugin main, Function<Player, PlayerData> function) {
        super(main, function);
        kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        BukkitSerializers.registerSerializer(kryo);
        this.playerFolder = Paths.get(main.getDataFolder().toPath() + File.separator + "players");
        if (!Files.exists(playerFolder))
            FileUtils.createDirectory(playerFolder);

    }


    @Override
    public Optional<PlayerData> fromStorage(Player player) {
        return FileUtils.list(playerFolder).filter(file -> file.getFileName().toString().equals(player.getUniqueId().toString()))
                .findAny().map(file -> ((PlayerData) thaw(kryo, file)).setTransientFields(player));
    }

    @Override
    public PlayerData write(PlayerData data) {
        try {
            freeze(kryo, Paths.get(playerFolder + File.separator + data.getPlayer().getUniqueId()), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }

    @Override
    public boolean isRegistered(UUID uuid) {
        return fromStorage(uuid).isPresent();
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
}
