package com.comugamers.sentey.core.listeners.server;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.comugamers.sentey.core.ping.action.PingAction;
import com.comugamers.sentey.core.ping.filter.PingFilter;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.InetAddress;

public class ServerListPingListener implements Listener {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @EventHandler
    public void onServerListPingEvent(ServerListPingEvent event) {
        // Get the source address
        InetAddress address = event.getAddress();

        // Check if we should ignore the event
        if (!config.getBoolean("config.server-list-ping.enabled")) {
            // If so, return
            return;
        }

        // Check if the address is null
        if (address == null) {
            // If so, return
            return;
        }

        // Loop through each registered filter
        for (PingFilter filter : plugin.getPingFilters()) {
            // Check if the ping should be ignored
            if (!filter.isClean(address)) {
                // If so, return
                return;
            }
        }

        // Loop through each registered ping action
        for (PingAction action : plugin.getPingActions()) {
            // And execute it
            action.handle(address);
        }
    }
}
