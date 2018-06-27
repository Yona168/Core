package monotheistic.mongoose.core.components.playerdata.database;

import com.gitlab.avelyn.architecture.base.Component;
import monotheistic.mongoose.core.components.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.gitlab.avelyn.core.base.Events.listen;

public abstract class Database extends Component {

    private final Supplier<PlayerData> playerPlayerDataFunction;
    private short backup;
    private Map<Player, PlayerData> cache;
    private BukkitRunnable backupRunnable;
    private boolean first = true;

    public Database(JavaPlugin main, Supplier<PlayerData> function) {
        this.playerPlayerDataFunction = function;
        cache = new IdentityHashMap<>();
        addChild(listen((PlayerJoinEvent event) -> {
            final Player player = event.getPlayer();
            final PlayerData data = fromStorage(player).orElseGet(() ->
                    write(player, createData()));
            cache.put(player, data);
        }));
        addChild(listen((PlayerQuitEvent event) ->
                fromCache(event.getPlayer()).ifPresent(playerData -> {
                    write(event.getPlayer(), playerData);
                    cache.remove(event.getPlayer());
                })));

        backupRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                backup();
            }
        };
        onEnable(() -> scheduleBackups(main));
        onDisable(this::disableBackup);
    }

    public void onDisable(JavaPlugin plugin) {
        backup();
    }

    private void backup() {
        cache.forEach(Database.this::write);
    }

    public Optional<PlayerData> fromCache(Player player) {
        final PlayerData data = cache.get(player);
        return Optional.ofNullable(data);
    }

    public abstract Optional<PlayerData> fromStorage(Player player);

    private PlayerData createData() {
        return playerPlayerDataFunction.get();
    }

    public Optional<PlayerData> fromStorage(UUID uuid) {
        return fromStorage(Bukkit.getPlayer(uuid));
    }

    public abstract PlayerData write(Player player, PlayerData data);

    public abstract boolean isRegistered(UUID uuid);

    private void scheduleBackups(JavaPlugin main) {
        if (backupIsEnabled()) {
            if (!first && backupRunnable.isCancelled())
                backupRunnable.cancel();
            else first = false;
            backupRunnable.runTaskTimer(main, 0, backup * 20 * 60);
        }
    }


    public void setBackup(short backupMins, JavaPlugin main) {
        this.backup = backupMins;
        scheduleBackups(main);
    }

    void disableBackup() {
        backupRunnable.cancel();
        this.first = true;
    }

    public boolean backupIsEnabled() {
        return backup != 0;
    }

    public Supplier<PlayerData> playerDataSupplier() {
        return this.playerPlayerDataFunction;
    }
}