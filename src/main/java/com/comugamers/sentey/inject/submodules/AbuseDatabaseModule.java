package com.comugamers.sentey.inject.submodules;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.report.AbuseDatabase;
import com.comugamers.sentey.report.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.Sentey;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

public class AbuseDatabaseModule extends AbstractModule {

    private final Sentey plugin;
    private final YamlFile config;

    public AbuseDatabaseModule(Sentey plugin, YamlFile config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Singleton
    @Provides
    @Named("abuseipdb")
    public AbuseDatabase provideAbuseIPDB() {
        return new AbuseIPDB(
                plugin, config.getString("config.integrations.abuseipdb.key")
        );
    }
}
