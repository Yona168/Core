package monotheistic.mongoose.core.files;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigurationHandler<T> {
    private final Map<T, Configuration> configurations;

    public ConfigurationHandler() {
        configurations = new HashMap<>();
    }

    public void saveAll() {
        configurations.values().forEach(Configuration::save);
    }

    public void reloadAll() {
        configurations.values().forEach(Configuration::reload);
    }

    public Configuration add(T key, Configuration configuration) {
        configurations.put(key, configuration);
        return configuration;
    }

    public Optional<Configuration> get(T key) {
        final Configuration result = configurations.get(key);
        return result != null ? Optional.of(result) : Optional.empty();
    }
}
