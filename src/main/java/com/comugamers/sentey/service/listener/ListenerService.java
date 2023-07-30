package com.comugamers.sentey.service.listener;

import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.Sentey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import javax.inject.Inject;
import java.util.Set;

import static com.comugamers.sentey.util.TextUtil.colorize;

public class ListenerService implements Service {

    @Inject
    private Set<Listener> listeners;

    @Inject
    private Sentey plugin;

    @Override
    public void start() {
        plugin.getServer()
                .getConsoleSender()
                .sendMessage(
                        colorize("&a-> &fRegistering listeners...")
                );

        listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}
