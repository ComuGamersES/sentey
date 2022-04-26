package com.comugamers.sentey.core.ping.action.internal;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.comugamers.sentey.core.ping.action.PingAction;
import com.comugamers.sentey.core.util.PlaceholderUtil;
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
                            PlaceholderUtil.applyPlaceholdersFromPingContext(
                                    config.getString("config.server-list-ping.actions.alerts.message"), address
                            )
                    );
                }
            });
        }
    }
}
