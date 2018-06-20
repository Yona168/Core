package monotheistic.mongoose.core.components.playerdata.transactions;

import monotheistic.mongoose.core.components.playerdata.PlayerData;
import monotheistic.mongoose.core.components.playerdata.database.Database;
import monotheistic.mongoose.core.components.playerdata.modifiers.economy.EcoPlayer;
import monotheistic.mongoose.core.strings.PluginStrings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class EconomyTransaction extends Transaction {


    public EconomyTransaction(Player giver, Player taker, double amt, Database database) {
        super(giver, taker, amt, database);
    }

    @Override
    public boolean execute() {
        final PlayerData giver = super.getGiver() == null ? null : getDatabase().fromCache(super.getGiver()).orElseThrow(() -> new RuntimeException("Player data not found in cache!"));
        final PlayerData taker = super.getTaker() == null ? null : getDatabase().fromCache(super.getTaker()).orElseThrow(() -> new RuntimeException("Player data not found in cache!"));
        EcoPlayer ecoGiver;
        EcoPlayer ecoTaker;
        if (!((giver==null||giver instanceof EcoPlayer)&&(taker==null||taker instanceof EcoPlayer))) {
            throw new IllegalArgumentException("Non-EcoPlayers cannot be used in an Economy Transaction!");
        } else {
            ecoGiver = (EcoPlayer) giver;
            ecoTaker = (EcoPlayer) taker;
        }
        if (allAreNull()) {
            throw new IllegalArgumentException("Both objects implementing EcoPlayer cannot be null!");
        } else if (ecoGiver != null) {
            ecoGiver.take(super.getAmt());
            super.getGiver().sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        } else if (taker != null) {
            ecoTaker.give(super.getAmt());
            super.getTaker().sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        }
        return true;
    }
}
