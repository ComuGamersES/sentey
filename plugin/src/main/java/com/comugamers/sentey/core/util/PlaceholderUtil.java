package com.comugamers.sentey.core.util;

import com.comugamers.sentey.core.login.context.LoginContext;

public class PlaceholderUtil {

    public static String applyPlaceholdersFromContext(String string, String detection, LoginContext context) {
        return string.replace("%player%", context.getPlayer().getName())
                .replace("%uuid%", context.getPlayer().toString())
                .replace("%proxyAddress%", context.getHandshakeAddress().getHostAddress())
                .replace("%address%", context.isValidSpoofedAddress() ? context.getSpoofedAddress().getHostAddress() : "null")
                .replace("%detectionType%", detection);
    }
}
