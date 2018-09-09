package monotheistic.mongoose.core.components.playerdata.database;

import java.util.Map;
import java.util.Optional;

public interface StorerCache<K, V> extends Storer<K, V> {

    Map<K, V> cache();

    default Optional<V> fromCache(K entry) {
        return Optional.ofNullable(cache().get(entry));
    }

    default void backup() {
        cache().forEach(this::updateDatabaseFor);
    }

    default V fromDatabaseOrLoadNewToCache(K key) {
        return fromDatabase(key).orElseGet(() -> {
            final V data = dataSupplier().apply(key);
            cache().put(key, data);
            return data;
        });
    }
}
