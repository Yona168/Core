package monotheistic.mongoose.core;

import com.gitlab.avelyn.core.components.ComponentPlugin;
import monotheistic.mongoose.core.gui.GUIListener;
import monotheistic.mongoose.core.strings.PluginStrings;
import monotheistic.mongoose.core.utils.ScheduleUtils;
import org.bukkit.ChatColor;

public class CorePlugin extends ComponentPlugin {

    public CorePlugin() {
        setup();
        onDisable(PluginStrings::nullifyEverything);
        onDisable(() -> ScheduleUtils.set(null));
        addChild(new GUIListener());
    }

    public void setup() {
        PluginStrings.setup(this, ChatColor.RED, ChatColor.AQUA, "Undefined", "None");
        ScheduleUtils.set(this);
    }


}


