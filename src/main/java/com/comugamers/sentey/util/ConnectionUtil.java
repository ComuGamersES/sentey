package com.comugamers.sentey.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class ConnectionUtil {

    /**
     * Gets the remote address of a {@link Player} as a {@link String}.
     * @param player The {@link Player}
     * @return their IP address as a {@link String}.
     */
    public static String getRemoteAddress(Player player) {
        try {
            return player.spigot().getRawAddress().toString();
        } catch (Throwable t) {
            return getRemoteAddressFromReflection(player);
        }
    }

    /**
     * Gets the remote address of a {@link Player} as a {@link String} by using reflection.
     * @param player The {@link Player}
     * @return their IP address as a {@link String}.
     */
    public static String getRemoteAddressFromReflection(Player player) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle", (Class<?>[]) null);
            Object entityPlayer = getHandle.invoke(player);

            Object playerConnection = entityPlayer.getClass()
                    .getDeclaredField("playerConnection")
                    .get(entityPlayer);

            Object networkManager = playerConnection
                    .getClass()
                    .getDeclaredField("networkManager")
                    .get(playerConnection);

            Method getRawAddress = networkManager
                    .getClass()
                    .getMethod("getRawAddress");

            return getRawAddress.invoke(networkManager).toString();
        } catch (Throwable t) {
            return "unknown";
        }
    }
}
