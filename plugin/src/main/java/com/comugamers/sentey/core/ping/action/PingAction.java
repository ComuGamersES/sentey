package com.comugamers.sentey.core.ping.action;

import com.comugamers.sentey.core.ping.filter.PingFilter;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.InetAddress;

/**
 * Represents an action that will be taken when a {@link ServerListPingEvent} is fired and doesn't
 * get caught by any of the registered {@link PingFilter ping filters} of the plugin.
 * @author Pabszito
 */
public interface PingAction {

    /**
     * Called when a {@link ServerListPingEvent} is fired and the {@link InetAddress source address}
     * wasn't caught by any of our registered {@link PingFilter ping filters}. The override of this
     * method should do something such as sending an alert through a webhook or to all online staff
     * members in the game chat.
     * @param address The source address as a {@link InetAddress}.
     */
    void handle(InetAddress address);
}
