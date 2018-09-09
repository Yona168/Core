package monotheistic.mongoose.core.components.playerdata.database;

import java.util.Optional;
import java.util.function.Function;

public interface Storer<K, V> {

    Function<K, V> dataSupplier();

    Optional<V> fromDatabase(K key);

    void updateDatabaseFor(K key, V newEntry);


}
