package com.comugamers.sentey.core.service.listener;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.Inject;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.Set;

import static com.comugamers.sentey.common.string.StringUtil.colorize;

public class ListenerService implements Service {

    @Inject
    private Set<Listener> listeners;

    @Inject
    private Sentey plugin;

    @Override
    public void start() {
        // Log that we're registering listeners
        plugin.getServer()
                .getConsoleSender()
                .sendMessage(
                        colorize("&a-> &fRegistering listeners...")
                );

        // Get the Bukkit plugin manager
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        // Loop through each listener and register it
        listeners.forEach(listener -> pluginManager.registerEvents(listener, plugin));
    }
}
