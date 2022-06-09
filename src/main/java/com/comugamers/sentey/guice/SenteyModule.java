package com.comugamers.sentey.guice;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.core.guice.submodules.*;
import com.comugamers.sentey.guice.submodules.*;
import com.google.inject.AbstractModule;
import org.bukkit.plugin.Plugin;

public class SenteyModule extends AbstractModule {

    private final Sentey plugin;

    public SenteyModule(Sentey plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        // Bind the plugin so it can be injected
        this.bind(Sentey.class).toInstance(plugin);
        this.bind(Plugin.class).toInstance(plugin);

        // Create a new YamlFile instance for the config.yml file
        YamlFile config = new YamlFile(plugin, "src/main/resources/config.yml");

        // Bind it
        this.bind(YamlFile.class).toInstance(config);

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
    }
}
