package com.comugamers.sentey.guice.submodules;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.report.AbuseDatabase;
import com.comugamers.sentey.report.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.Sentey;
import com.google.inject.AbstractModule;

import static com.google.inject.name.Names.named;

public class AbuseDatabaseModule extends AbstractModule {

    private final Sentey plugin;
    private final YamlFile config;

    public AbuseDatabaseModule(Sentey plugin, YamlFile config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    protected void configure() {
        // Bind the AbuseDatabase instance based on the AbuseIPDB's implementation
        this.bind(AbuseDatabase.class)
                .annotatedWith(named("abuseipdb"))
                .toInstance(
                        new AbuseIPDB(
                                plugin, config.getString("config.integrations.abuseipdb.key")
                        )
                );
    }
}
