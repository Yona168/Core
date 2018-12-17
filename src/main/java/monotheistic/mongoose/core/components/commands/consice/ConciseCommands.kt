package monotheistic.mongoose.core.components.commands.consice

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import java.util.*

//This is a WIP. The current commands system is a bit bulky and really only feasible when working with large amts of data. This should help when its done

private class NO_ACCESS_YET_MUAHAHAHAHA {
    class Arguments(private val args: Array<String>) : Iterator<String> {
        var pointer = -1
        override fun hasNext() = args.lastIndex != pointer
        override fun next() = args[++pointer]
        fun current() = args[pointer]
    }

    fun String.toOnlinePlayerOrNull(): Player? = Bukkit.getPlayer(this)
    fun UUID.toOnlinePlayerOrNull(): Player? = Bukkit.getPlayer(this)
    fun UUID.toOfflinePlayerOrNull(): OfflinePlayer? = Bukkit.getOfflinePlayer(this)


    class CommandScope private constructor(val args: Arguments, val sender: CommandSender, val cmd: String) {
        inline fun onSub(str: String, func: (CommandScope) -> Boolean) {
            if (args.current().toLowerCase() == str.toLowerCase()) {
                func(this)
            }
        }
    }

    fun registerCommand(cmd: String, command: PluginCommand) {

    }
}