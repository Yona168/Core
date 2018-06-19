package monotheistic.mongoose.core.files;


import java.util.HashSet;
import java.util.Set;

public abstract class AbstractConfigurationHandler{
    private Set<Configuration> configurations;

    public AbstractConfigurationHandler() {
        configurations = new HashSet<>();
    }

    public void saveAll() {
        configurations.forEach(Configuration::save);
    }



}
