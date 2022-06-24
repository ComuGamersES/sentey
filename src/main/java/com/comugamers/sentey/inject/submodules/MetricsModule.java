package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.Sentey;
import org.bstats.bukkit.Metrics;
import team.unnamed.inject.AbstractModule;

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
