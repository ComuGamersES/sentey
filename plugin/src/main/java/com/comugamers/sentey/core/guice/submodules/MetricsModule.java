package com.comugamers.sentey.core.guice.submodules;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.Sentey;
import com.google.inject.AbstractModule;
import org.bstats.bukkit.Metrics;

public class MetricsModule extends AbstractModule {

    private final Sentey plugin;
    private final YamlFile config;

    public MetricsModule(Sentey plugin, YamlFile config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    protected void configure() {
        // Check if we should enable metrics
        if(!config.getBoolean("config.bstats")) {
            // If not, return
            return;
        }

        // Bind a new bStats Metrics instance
        this.bind(Metrics.class).toInstance(new Metrics(plugin, 15027));
    }
}
