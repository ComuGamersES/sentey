package com.comugamers.sentey.ping.filter;

import com.comugamers.sentey.ping.action.PingAction;

import java.net.InetAddress;

/**
 * Represents a source IP address filter for server list pings.
 * @author Pabszito
 */
public interface PingFilter {

    /**
     * Checks if a source IP address of a server list ping should be sent
     * to all registered {@link PingAction ping actions} or not.
     * @param address The source address as a {@link InetAddress}.
     * @return true if the source address is clean.
     */
    boolean isClean(InetAddress address);
}
