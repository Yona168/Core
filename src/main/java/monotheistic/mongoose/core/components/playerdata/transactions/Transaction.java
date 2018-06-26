package monotheistic.mongoose.core.components.playerdata.transactions;

import monotheistic.mongoose.core.components.playerdata.database.Database;
import org.bukkit.entity.Player;

public abstract class Transaction {
    private final Player giver;
    private final Player taker;
    private final double amt;
    private final Database database;

    Transaction(Player giver, Player taker, double amt, Database database) {
        this.giver = giver;
        this.taker = taker;
        this.amt = amt;
        this.database = database;
    }

    boolean allAreNull() {
        return (giver == null && taker == null);
    }

    public abstract boolean execute();

    Player getGiver() {
        return giver;
    }

    Player getTaker() {
        return taker;
    }

    double getAmt() {
        return amt;
    }

    Database getDatabase() {
        return this.database;
    }
}
