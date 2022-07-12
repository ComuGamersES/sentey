package com.comugamers.sentey.inject;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.inject.submodules.*;
import team.unnamed.inject.AbstractModule;

public class SenteyModule extends AbstractModule {

    private final Sentey plugin;

    public SenteyModule(Sentey plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        // Bind main instance
        this.bind(Sentey.class).toInstance(plugin);

        // Bind configuration files
        YamlFile config = new YamlFile(plugin, "config.yml");
        this.bind(YamlFile.class).toInstance(config);

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
