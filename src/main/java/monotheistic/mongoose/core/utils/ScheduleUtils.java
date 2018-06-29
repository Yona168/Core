package monotheistic.mongoose.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleUtils {
    private static JavaPlugin main;

    public static void set(JavaPlugin plugin) {
        ScheduleUtils.main = plugin;
    }

    public static BukkitTask sync(Runnable runnable) {
        return Bukkit.getScheduler().runTask(main, runnable);
    }

    static BukkitTask async(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(main, runnable);
    }

    public static BukkitTask repeatingSync(Runnable runnable, long everyTicks, boolean startingNow) {
        return Bukkit.getScheduler().runTaskTimer(main, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    public static BukkitTask repeatingAsync(Runnable runnable, long everyTicks, boolean startingNow) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(main, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    public static BukkitTask laterSync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(main, runnable, delay);
    }

    public static BukkitTask laterAsync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(main, runnable, delay);
    }
}