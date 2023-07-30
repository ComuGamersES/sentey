package com.comugamers.sentey.ping.action.internal;

import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.comugamers.sentey.util.file.YamlFile;

import javax.inject.Inject;
import java.net.InetAddress;

public class CommandPingAction implements PingAction {

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(InetAddress address) {
        if (config.getBoolean("config.server-list-ping.actions.commands.enabled")) {
            plugin.getServer().getScheduler().runTask(plugin, () ->
                config.getStringList("config.server-list-ping.actions.commands.list").forEach(command ->
                        plugin.getServer().dispatchCommand(
                                plugin.getServer().getConsoleSender(),
                                PlaceholderUtil.applyPlaceholdersFromPingAddress(
                                        command, address
                                )
                        )
                )
            );
        }
    }
}
