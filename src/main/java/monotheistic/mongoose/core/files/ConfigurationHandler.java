package monotheistic.mongoose.core.files;


import java.util.HashMap;
import java.util.Map;

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
}
