package monotheistic.mongoose.core;

import com.gitlab.avelyn.core.components.ComponentPlugin;
import monotheistic.mongoose.core.components.playerdata.database.Database;
import monotheistic.mongoose.core.files.Configuration;
import monotheistic.mongoose.core.gui.GUIListener;
import monotheistic.mongoose.core.strings.PluginStrings;
import monotheistic.mongoose.core.utils.ScheduleUtils;
import org.bukkit.ChatColor;

public abstract class CorePlugin extends ComponentPlugin {

    public CorePlugin() {
        setup();
        onDisable(PluginStrings::nullifyEverything);
        onEnable(() -> ScheduleUtils.set(this));
        onDisable(() -> ScheduleUtils.set(null));
        addChild(new GUIListener());
    }

    public void setup() {
        PluginStrings.setup(this, ChatColor.RED, ChatColor.AQUA, "Undefined", "None");
    }

    public Database setupDb(Database database) {
        Configuration dbOptions = new Configuration(this, "dboptions.yml");
        if (dbOptions.configuration().getBoolean("database.do backup"))
            database.setBackup((short) dbOptions.configuration().getInt("database.every"), this);
        addChild(database);
        return database;
    }

}


