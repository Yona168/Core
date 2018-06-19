package monotheistic.mongoose.core.components.playerdata.economy;

import monotheistic.mongoose.core.components.playerdata.modifiers.EcoPlayer;
import monotheistic.mongoose.core.strings.PluginStrings;
import org.bukkit.ChatColor;


public class EconomyTransaction implements Transaction {

    private EcoPlayer giver;
    private EcoPlayer taker;
    private double amount;

    public EconomyTransaction(EcoPlayer giver, EcoPlayer taker, double amount) {
        this.giver = giver;
        this.taker = taker;
        this.amount = amount;

    }

    @Override
    public void execute() {
        if (giver == null && taker == null) {
            throw new IllegalArgumentException("Both objects implementing EcoPlayer cannot be null!");

        }
        if (giver != null) {
            giver.take(amount);
            giver.sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        }
        if (taker != null) {
            taker.give(amount);
            taker.sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        }
    }
}
