package com.comugamers.sentey.util;

public class VersionUtil {

    /**
     * Checks if a server is running Spigot or a derivative fork by
     * trying to get the <code>org.bukkit.entity.Player$Spigot</code> class.
     *
     * @return false if not, true if so.
     */
    public static boolean isSpigot() {
        try {
            Class.forName("org.bukkit.entity.Player$Spigot");
            return true;
        } catch (Throwable tr) {
            return false;
        }
    }
}
