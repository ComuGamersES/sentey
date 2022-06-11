package com.comugamers.sentey.util;

import com.comugamers.sentey.login.structure.LoginContext;
import org.bukkit.Bukkit;

import java.net.InetAddress;

import static com.comugamers.sentey.util.NetworkUtil.getIPv4;

public class PlaceholderUtil {

    public static String applyPlaceholdersFromLoginContext(String string, String detection, LoginContext context) {
        return string.replace("%player%", context.getPlayer().getName())
                .replace("%uuid%", context.getPlayer().getUniqueId().toString())
                .replace("%proxyAddress%", context.getHandshakeAddress().getHostAddress())
                .replace("%address%", context.isValidSpoofedAddress() ? context.getSpoofedAddress().getHostAddress() : "null")
                .replace("%detectionType%", detection)
                .replace("%serverAddress%", getIPv4() + ":" + Bukkit.getPort())
                .replace("%serverPort%", String.valueOf(Bukkit.getPort()));
    }

    public static String applyPlaceholdersFromPingAddress(String string, InetAddress address) {
        return string.replace("%address%", address.getHostAddress())
                .replace("%serverAddress%", getIPv4() + ":" + Bukkit.getPort())
                .replace("%serverPort%", String.valueOf(Bukkit.getPort()));
    }
}
