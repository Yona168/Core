package monotheistic.mongoose.core.components.playerdata;

import org.bukkit.entity.Player;

public abstract class PlayerData {
    private transient Player player;
    private transient boolean setPlayerLock = false;

    public Player getPlayer() {
        return player;
    }

    public PlayerData setTransientFields(Player player) {
        if (setPlayerLock) {
            try {
                throw new IllegalAccessException("Cannot set player at this time!");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.player = player;
        setPlayerLock = true;
        return this;
    }

    public PlayerData(Player player) {
        this.player = player;
    }
}
