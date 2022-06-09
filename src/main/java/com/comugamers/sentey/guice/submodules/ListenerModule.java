package com.comugamers.sentey.guice.submodules;

import com.comugamers.sentey.listeners.player.PlayerJoinListener;
import com.comugamers.sentey.listeners.player.PlayerLoginListener;
import com.comugamers.sentey.listeners.server.ServerListPingListener;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.bukkit.event.Listener;

public class ListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        // Create a new set multibinder for the listener interface
        Multibinder<Listener> multibinder = Multibinder.newSetBinder(binder(), Listener.class);

        // Bind listeners
        multibinder.addBinding().to(PlayerJoinListener.class);
        multibinder.addBinding().to(PlayerLoginListener.class);
        multibinder.addBinding().to(ServerListPingListener.class);
    }
}
