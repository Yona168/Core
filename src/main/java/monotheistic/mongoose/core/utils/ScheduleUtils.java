package monotheistic.mongoose.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleUtils {
    private static Plugin SCHEDULER = new PluginImpl("SCHEDULER");


    public static BukkitTask sync(Runnable runnable) {
        return Bukkit.getScheduler().runTask(SCHEDULER, runnable);
    }

    static BukkitTask async(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(SCHEDULER, runnable);
    }

    public static BukkitTask repeatingSync(Runnable runnable, long everyTicks, boolean startingNow) {
        return Bukkit.getScheduler().runTaskTimer(SCHEDULER, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    public static BukkitTask repeatingAsync(Runnable runnable, long everyTicks, boolean startingNow) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(SCHEDULER, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    public static BukkitTask laterSync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(SCHEDULER, runnable, delay);
    }

    public static BukkitTask laterAsync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(SCHEDULER, runnable, delay);
    }
}