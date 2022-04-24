package com.comugamers.sentey.core;

import com.comugamers.sentey.common.Service;
import com.comugamers.sentey.core.guice.SenteyModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public class Sentey extends JavaPlugin {

    @Inject
    private Service service;

    @Override
    public void onEnable() {
        // Create a new Injector based on the Sentey module
        Injector injector = Guice.createInjector(
                new SenteyModule(this)
        );

        // Inject members
        injector.injectMembers(this);

        // Start the main service
        service.start();
    }

    @Override
    public void onDisable() {
        // Stop the main service
        service.stop();
    }
}
