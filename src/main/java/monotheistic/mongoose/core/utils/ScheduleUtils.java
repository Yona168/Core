package monotheistic.mongoose.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleUtils {
    private static JavaPlugin main;

    public static void set(JavaPlugin plugin) {
        ScheduleUtils.main = plugin;
    }

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(main, runnable);
    }

    static BukkitTask async(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(main, runnable);
    }

    public static void repeatingSync(Runnable runnable, long everyTicks, boolean startingNow) {
        Bukkit.getScheduler().runTaskTimer(main, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    public static void repeatingAsync(Runnable runnable, long everyTicks, boolean startingNow) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(main, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    public static void laterSync(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(main, runnable, delay);
    }

    public static void laterAsync(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, runnable, delay);
    }
}