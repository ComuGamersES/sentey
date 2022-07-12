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
        // Bind main instance
        this.bind(Sentey.class).toInstance(plugin);

        // Get the main configuration file
        YamlFile config = provideConfiguration();

        // Install modules
        this.install(new CommandModule());
        this.install(new MessageModule(plugin));
        this.install(new ServiceModule());
        this.install(new AbuseDatabaseModule(plugin, config));
        this.install(new LoginModule());
        this.install(new PingModule());
        this.install(new ListenerModule());
        this.install(new UpdateCheckerModule());
    }
}
