package com.comugamers.sentey.service.metrics;

import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.service.Service;
import com.comugamers.sentey.util.file.YamlFile;
import org.bstats.bukkit.Metrics;

import javax.inject.Inject;

public class MetricsService implements Service {

    @Inject
    private Sentey plugin;

    @Inject
    private YamlFile config;

    @Override
    public void start() {
        if (!config.getBoolean("config.bstats", true)) {
            return;
        }

        new Metrics(plugin, 15027);
    }
}
