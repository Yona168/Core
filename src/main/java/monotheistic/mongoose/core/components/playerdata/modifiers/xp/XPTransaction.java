package monotheistic.mongoose.core.components.playerdata.modifiers.xp;

import monotheistic.mongoose.core.components.playerdata.economy.Transaction;
import monotheistic.mongoose.core.strings.PluginStrings;
import org.bukkit.ChatColor;

public class XPTransaction implements Transaction {
    private final XPPlayer giver;
    private final XPPlayer taker;
    private final int amt;

    public XPTransaction(XPPlayer giver, XPPlayer taker, int amt) {
        this.giver = giver;
        this.taker = taker;
        this.amt = amt;

    }

    @Override
    public void execute() {
        if (giver == null && taker == null)
            throw new IllegalArgumentException("Both objects implementing XPPlayer cannot be null!");
        if (giver != null) {
            giver.takeXp(-amt);
            giver.sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        }
        if (taker != null) {
            taker.giveXp(amt);
            taker.sendMessage(PluginStrings.tag() + ChatColor.GREEN + " Transaction complete.");
        }
    }
}
