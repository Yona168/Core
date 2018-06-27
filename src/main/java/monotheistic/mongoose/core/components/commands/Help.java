package monotheistic.mongoose.core.components.commands;

import monotheistic.mongoose.core.strings.PluginStrings;
import monotheistic.mongoose.core.utils.MiscUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Help extends SubCommand {
    final AbstractCommandManager commandManager;

    public Help(AbstractCommandManager commandManager) {
        super("help", "Displays commands and their descriptions", "help [page]", 1);
        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(invalidSyntax());
            return false;
        }
        int num = MiscUtils.parseInt(args[0]).orElse(0);

        if (num <= 0) {
            sender.sendMessage(invalidSyntax());
            return false;
        }
        final ChatColor first = PluginStrings.mainColor();
        final ChatColor second = PluginStrings.secondaryColor();
        final StringBuilder sending = new StringBuilder(second + "---" + PluginStrings.tag() + first + "Page: " + num + second + "---");
        commandManager.getCommands().stream().skip((num-1) * 5).limit(5).map(command ->
                "\n"+first + command.name() + ChatColor.WHITE + ": " + second + command.description()).forEach(sending::append);
        sender.sendMessage(sending.toString());
        return true;
    }


}
