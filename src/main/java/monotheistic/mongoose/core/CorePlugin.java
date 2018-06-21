package monotheistic.mongoose.core;

import com.gitlab.avelyn.core.base.Server;
import monotheistic.mongoose.core.components.playerdata.database.Database;
import monotheistic.mongoose.core.files.Configuration;
import monotheistic.mongoose.core.strings.PluginStrings;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CorePlugin extends JavaPlugin {

    public CorePlugin() {
        setup();
    }
    public void setup() {
        Server.initialize(this);
        PluginStrings.setup(this, ChatColor.RED, ChatColor.AQUA, "Undefined", "None");
    }

    public Database setupDb(Database database) {
        Configuration dbOptions = new Configuration(this, "dboptions.yml");
        if (dbOptions.configuration().getBoolean("database.do backup"))
            database.setBackup((short) dbOptions.configuration().getInt("database.every"), this);
        return database;
    }

}


