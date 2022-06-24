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
        // Check if we should run commands
        if(config.getBoolean("config.server-list-ping.actions.commands.enabled")) {
            // If so, don't do this async
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                // Loop through the list of commands
                config.getStringList("config.login.actions.commands.list").forEach(
                        // And dispatch each one of them
                        command ->
                                plugin.getServer().dispatchCommand(
                                        plugin.getServer().getConsoleSender(),
                                        PlaceholderUtil.applyPlaceholdersFromPingAddress(
                                                command, address
                                        )
                                )
                );
            });
        }
    }
}
