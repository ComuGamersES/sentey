package com.comugamers.sentey.listeners.server;

import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.ping.filter.PingFilter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import javax.inject.Inject;
import java.net.InetAddress;

public class ServerListPingListener implements Listener {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @EventHandler
    public void onServerListPingEvent(ServerListPingEvent event) {
        InetAddress address = event.getAddress();

        if (!config.getBoolean("config.server-list-ping.enabled")) {
            return;
        }

        if (address == null) {
            return;
        }

        for (PingFilter filter : plugin.getPingFilters()) {
            if (!filter.isClean(address)) {
                return;
            }
        }

        for (PingAction action : plugin.getPingActions()) {
            action.handle(address);
        }
    }
}
