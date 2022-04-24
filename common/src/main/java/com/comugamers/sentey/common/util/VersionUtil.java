package com.comugamers.sentey.common.util;

public class VersionUtil {

    public static boolean isSpigot() {
        try {
            Class.forName("org.bukkit.entity.Player$Spigot");
            return true;
        } catch (Throwable tr) {
            return false;
        }
    }
}
