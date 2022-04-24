package com.comugamers.sentey.common.util;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class NetworkUtil {

    public static String getIPv4() {
        try {
            URL url = new URL("https://checkip.amazonaws.com");
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                return in.readLine();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        } catch (IOException exception) {
            return "localhost";
        }
    }

    public static boolean isValidIPv4(String ip) {
        try {
            // Split the IP on different parts
            String[] parts = ip.split("\\.", -1);

            // Check if the IP is in the correct format
            if(parts.length != 4) {
                return false;
            }

            // Loop through each part of the IP looking for integers > 255 and < 0
            for(String part : parts) {
                int i = Integer.parseInt(part);

                if(i < 0 || i > 255) {
                    return false;
                }
            }
        } catch (Exception exception) {
            return false;
        }

        return true;
    }

    public static String getCurrentServerAddress() {
        return getIPv4() + ":" + Bukkit.getPort();
    }
}
