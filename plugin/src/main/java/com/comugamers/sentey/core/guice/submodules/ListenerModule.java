package com.comugamers.sentey.core.guice.submodules;

import com.comugamers.sentey.core.listeners.PlayerJoinListener;
import com.comugamers.sentey.core.listeners.PlayerLoginListener;
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
    }
}
