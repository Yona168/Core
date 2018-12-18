package monotheistic.mongoose.core;

import com.gitlab.avelyn.core.components.ComponentPlugin;
import monotheistic.mongoose.core.files.Configuration;
import monotheistic.mongoose.core.gui.GUIListener;

public class CorePlugin extends ComponentPlugin {
  public CorePlugin() {
    addChild(new GUIListener());
    onEnable(() -> {
      Configuration config = Configuration.loadConfiguration(getDataFolder().toPath(), "config.yml");
      System.out.println(config.get("test", String.class));
    });
  }

}
