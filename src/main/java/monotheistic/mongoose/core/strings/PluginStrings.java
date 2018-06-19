package monotheistic.mongoose.core.strings;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public abstract class PluginStrings {

    private static ChatColor MAIN_COLOR;
    private static ChatColor SECONDARY_COLOR;

    private static String TAG;
    private static String INVALID_SYNTAX;
    private static String NO_PERMS;
    private static String CMD_LABEL;
    private static String CMD_LABEL_WITH_SLASH;


    public static void setup(JavaPlugin plugin, ChatColor main, ChatColor secondary, @Nullable String nameForTag, String commandLabel) {
        MAIN_COLOR = main;
        SECONDARY_COLOR = secondary;
        TAG = secondary + "[" + main + (nameForTag == null ? plugin.getDescription().getName() : nameForTag) + secondary + "]" + ChatColor.RESET;
        INVALID_SYNTAX = TAG + ChatColor.RED + " Invalid syntax! Correct syntax is ";
        NO_PERMS = TAG + ChatColor.RED + " You do not have permission to use this command!";
        CMD_LABEL =commandLabel;
        CMD_LABEL_WITH_SLASH="/"+CMD_LABEL;
    }

    public static String tag() {
        return TAG;
    }

    public static String mainCmdLabel(boolean withSlash) {
        return withSlash ? CMD_LABEL_WITH_SLASH : CMD_LABEL;
    }

    public static ChatColor mainColor() {
        return MAIN_COLOR;
    }

    public static ChatColor secondaryColor() {
        return SECONDARY_COLOR;
    }

    public static String invalidSyntax() {
        return INVALID_SYNTAX;
    }

    public static String noPerms() {
        return NO_PERMS;
    }
}
