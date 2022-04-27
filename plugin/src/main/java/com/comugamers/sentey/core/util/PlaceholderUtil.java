package com.comugamers.sentey.core.util;

import com.comugamers.sentey.core.login.context.LoginContext;
import org.bukkit.Bukkit;

import java.net.InetAddress;

import static com.comugamers.sentey.common.util.NetworkUtil.getIPv4;

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

    public static String applyPlaceholdersFromPingContext(String string, InetAddress address) {
        return string.replace("%address%", address.getHostAddress())
                .replace("%serverAddress%", getIPv4() + ":" + Bukkit.getPort())
                .replace("%serverPort%", String.valueOf(Bukkit.getPort()));
    }
}
