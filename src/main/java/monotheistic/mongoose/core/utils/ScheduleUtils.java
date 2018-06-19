package monotheistic.mongoose.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public interface ScheduleUtils {
    static void sync(JavaPlugin main, Runnable runnable) {
        Bukkit.getScheduler().runTask(main, runnable);
    }

    static void async(JavaPlugin main, Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(main, runnable);
    }

    static void repeatingSync(JavaPlugin main, Runnable runnable, long everyTicks) {
        Bukkit.getScheduler().runTaskTimer(main, runnable, 0, everyTicks);
    }
}