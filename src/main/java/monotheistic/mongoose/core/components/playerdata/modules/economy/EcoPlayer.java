package monotheistic.mongoose.core.components.playerdata.modules.economy;

import monotheistic.mongoose.core.components.playerdata.modules.PlayerDataModifier;
import org.jetbrains.annotations.NotNull;

public interface EcoPlayer extends PlayerDataModifier {
    @NotNull
    EconomyModule getEconomyModule();

    default double takeBalance(double amt) {
        return getEconomyModule().take(amt);
    }

    default double giveBalance(double amt) {
        return getEconomyModule().give(amt);
    }

    default double getBalance() {
        return getEconomyModule().getBalance();
    }
}
