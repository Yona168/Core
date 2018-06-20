package monotheistic.mongoose.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public interface ScheduleUtils {
    static void sync(JavaPlugin main, Runnable runnable) {
        Bukkit.getScheduler().runTask(main, runnable);
    }

    static BukkitTask async(JavaPlugin main, Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(main, runnable);
    }

    static void repeatingSync(JavaPlugin main, Runnable runnable, long everyTicks, boolean startingNow) {
        Bukkit.getScheduler().runTaskTimer(main, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    static void repeatingAsync(JavaPlugin main, Runnable runnable, long everyTicks, boolean startingNow) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(main, runnable, startingNow ? 0 : everyTicks, everyTicks);
    }

    static void laterSync(JavaPlugin main, Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(main, runnable, delay);
    }

    static void laterAsync(JavaPlugin main, Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, runnable, delay);
    }
}