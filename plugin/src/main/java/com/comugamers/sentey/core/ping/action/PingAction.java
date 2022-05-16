package com.comugamers.sentey.core.ping.action;

import java.net.InetAddress;

/**
 * Represents an action that will be taken when a server list ping
 * is received.
 *
 * @author Pabszito
 */
public interface PingAction {

    /**
     * Called when a server list ping is received. The override of this method should do
     * something such as sending an alert through a webhook or to all online staff members
     * in the game chat.
     *
     * @param address The source address
     */
    void handle(InetAddress address);
}
