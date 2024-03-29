package com.comugamers.sentey.util;

import com.google.common.net.InetAddresses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class NetworkUtil {

    /**
     * Gets the IPv4 address of the machine running this Java application.
     *
     * @return The IPv4 address as a {@link String}.
     */
    public static String getIPv4() {
        try {
            URL url = new URL("https://checkip.amazonaws.com");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return in.readLine();
            }
        } catch (IOException ex) {
            return "localhost";
        }
    }

    /**
     * Checks if a {@link String} is a valid IPv4 address.
     *
     * @param ip The IP address to check as a {@link String}
     * @return True if the {@link String} is a valid IPv4 address.
     * @deprecated Please use Guava's {@link InetAddresses#isInetAddress(String)} instead.
     */
    @Deprecated
    public static boolean isValidIPv4(String ip) {
        try {
            // Split the IP on different parts
            String[] parts = ip.split("\\.", -1);

            // Check if the IP is in the correct format
            if (parts.length != 4) {
                return false;
            }

            // Loop through each part of the IP looking for integers > 255 and < 0
            for (String part : parts) {
                int i = Integer.parseInt(part);

                if (i < 0 || i > 255) {
                    return false;
                }
            }
        } catch (Exception exception) {
            return false;
        }

        return true;
    }

    /**
     * Gets the formatted current server address. Only works under Bukkit.
     *
     * @return The formatted server address as a string, for example <code>194.48.18.94:25565</code>.
     */
    public static String getCurrentServerAddress() {
        return getIPv4() + ":" + org.bukkit.Bukkit.getPort();
    }
}
