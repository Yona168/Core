package monotheistic.mongoose.core.components.playerdata.modules.xp;


import monotheistic.mongoose.core.components.playerdata.modules.PlayerDataModifier;
import org.jetbrains.annotations.NotNull;

public interface XPPlayer extends PlayerDataModifier {
    @NotNull
    XPModule getXpModule();

    default int getLevel() {
        return getXpModule().getLevel();
    }

    default int getCurrentThreshold() {
        return getXpModule().getCurrentThreshold();
    }

    default int getCurrentXp() {
        return getXpModule().getCurrentXp();
    }

    default int getIncrease() {
        return getXpModule().getIncrease();
    }

    default void giveXp(int amt) {
        getXpModule().giveXp(amt);
    }

    default void takeXp(int amt) {
        getXpModule().takeXp(amt);
    }
}
