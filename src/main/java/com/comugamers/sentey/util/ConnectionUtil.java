package com.comugamers.sentey.util;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConnectionUtil {

    /**
     * Gets the remote address of a {@link Player} as a {@link String} using reflection.
     * @param player The {@link Player}
     * @return their IP address as a {@link String}.
     */
    public static String getRemoteAddress(Player player) {
        try {
            // Get the CraftPlayer#getHandle() method
            Method getHandle = player.getClass().getMethod("getHandle", (Class<?>[]) null);

            // Get the EntityPlayer
            Object entityPlayer = getHandle.invoke(player);

            // Get the player connection
            Object playerConnection = entityPlayer.getClass()
                    .getDeclaredField("playerConnection")
                    .get(entityPlayer);

            // Get the network manager
            Object networkManager = playerConnection
                    .getClass()
                    .getDeclaredField("networkManager")
                    .get(playerConnection);

            // Get the NetworkManager#getRawAddress() method
            Method getRawAddress = networkManager
                    .getClass()
                    .getMethod("getRawAddress");

            // Invoke it and send the string as the result
            return getRawAddress.invoke(networkManager).toString();
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | IllegalAccessException
                 | NoSuchFieldException ex) {
            ex.printStackTrace();
            return "unknown";
        }
    }
}
