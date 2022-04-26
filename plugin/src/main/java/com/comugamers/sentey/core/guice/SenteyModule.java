package com.comugamers.sentey.core.guice;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.comugamers.sentey.core.guice.submodules.*;
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
        YamlFile config = new YamlFile(plugin, "config.yml");

        // Bind it
        this.bind(YamlFile.class).toInstance(config);

        // Install the metrics module
        this.install(new MetricsModule(plugin, config));

        // Install the service module
        this.install(new ServiceModule());

        // Install the modifier module
        this.install(new ModifierModule());

        // Install the action module
        this.install(new ActionModule());

        // Install the listener module
        this.install(new ListenerModule());
    }
}
