package monotheistic.mongoose.core.components.playerdata.modifiers.economy;

import monotheistic.mongoose.core.components.playerdata.modifiers.PlayerDataModifier;

public interface EcoPlayer extends PlayerDataModifier {
    double getBalance();

    void setBalance(double amt);

    default void give(double amt) {
        setBalance(getBalance() + amt);
    }


    default void take(double amt) {
        setBalance(getBalance() - amt >= 0 ? getBalance() - amt : 0);
    }

}
