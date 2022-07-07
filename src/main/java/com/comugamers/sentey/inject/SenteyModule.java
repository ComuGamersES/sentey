package com.comugamers.sentey.inject;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.inject.submodules.*;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class SenteyModule extends AbstractModule {

    private final Sentey plugin;

    public SenteyModule(Sentey plugin) {
        this.plugin = plugin;
    }

    @Singleton
    @Provides
    public YamlFile provideConfiguration() {
        return new YamlFile(plugin, "config.yml");
    }

    @Override
    protected void configure() {
        // Get the main configuration file
        YamlFile config = provideConfiguration();

        // Bind the plugin so it can be injected
        this.bind(Sentey.class).toInstance(plugin);

        // Install the message module
        this.install(new MessageModule(plugin));

        // Install the metrics module
        this.install(new MetricsModule(plugin, config));

        // Install the service module
        this.install(new ServiceModule());

        // Install the abuse database module
        this.install(new AbuseDatabaseModule(plugin, config));

        // Install the login module which will register all internal login filters and actions
        this.install(new LoginModule());

        // Install the ping module which will register all internal ping filters and actions
        this.install(new PingModule());

        // Install the listener module
        this.install(new ListenerModule());

        // Install the update checker module
        this.install(new UpdateCheckerModule());
    }
}
