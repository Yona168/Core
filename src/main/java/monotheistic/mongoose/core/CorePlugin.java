package monotheistic.mongoose.core;

import com.gitlab.avelyn.core.components.ComponentPlugin;
import monotheistic.mongoose.core.gui.GUIListener;

public class CorePlugin extends ComponentPlugin {
  public CorePlugin() {
    addChild(new GUIListener());
  }
}
