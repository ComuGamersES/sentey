package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.listeners.player.PlayerJoinListener;
import com.comugamers.sentey.listeners.player.PlayerLoginListener;
import com.comugamers.sentey.listeners.server.ServerListPingListener;
import org.bukkit.event.Listener;
import team.unnamed.inject.AbstractModule;

public class ListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        this.multibind(Listener.class)
                .asSet()
                .to(PlayerJoinListener.class)
                .to(PlayerLoginListener.class)
                .to(ServerListPingListener.class)
                .singleton();
    }
}
