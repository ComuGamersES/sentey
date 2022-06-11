package com.comugamers.sentey.ping.action.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.google.inject.Inject;

import java.net.InetAddress;

public class AlertPingAction implements PingAction {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(InetAddress address) {
        // Check if we should send a message to online staff
        if(config.getBoolean("config.server-list-ping.actions.alerts.enabled")) {
            // If so, send the message
            plugin.getServer().getOnlinePlayers().forEach(target -> {
                if(target.hasPermission("sentey.alerts")) {
                    target.sendMessage(
                            PlaceholderUtil.applyPlaceholdersFromPingAddress(
                                    config.getString("config.server-list-ping.actions.alerts.message"), address
                            )
                    );
                }
            });
        }
    }
}
