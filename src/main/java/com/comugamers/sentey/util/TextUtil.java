package com.comugamers.sentey.util;

import org.bukkit.ChatColor;

import java.util.List;

public class TextUtil {

    /**
     * Colorizes a {@link String} by using {@link ChatColor#translateAlternateColorCodes(char, String)}.
     *
     * @param str The {@link String} to colorize.
     * @return The colorized {@link String}.
     */
    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    /**
     * Colorizes a {@link List<String>} by using {@link ChatColor#translateAlternateColorCodes(char, String)}.
     *
     * @param list The {@link List<String>} to colorize
     * @return The colorized {@link List<String>}
     */
    public static List<String> colorize(List<String> list) {
        list.replaceAll(TextUtil::colorize);
        return list;
    }
}
