package com.comugamers.sentey.common.string;

import org.bukkit.ChatColor;

import java.util.List;

public class StringUtil {

    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<String> colorize(List<String> list) {
        for(int i = 0; i < list.size(); i++) {
            list.set(i, colorize(list.get(i)));
        }

        return list;
    }
}
