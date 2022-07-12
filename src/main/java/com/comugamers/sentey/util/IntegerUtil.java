package com.comugamers.sentey.util;

public class IntegerUtil {

    /**
     * Checks if a {@link String} is a valid {@link Integer int}.
     * @param str The {@link String} to check.
     * @return true if the provided value is a valid {@link Integer}, false if not.
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
