package com.comugamers.sentey.core.ping.filter;

import java.net.InetAddress;

/**
 * Represents a source IP address filter for server list pings.
 * @author Pabszito
 */
public interface PingFilter {

    /**
     * Checks if a source address of a ping should be sent to all registered actions or not.
     * @param address The source address
     * @return true if the address doesn't get caught by the filter.
     */
    boolean isClean(InetAddress address);
}
